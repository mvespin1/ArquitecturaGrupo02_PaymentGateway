package ec.edu.espe.gateway.comercio.services;

import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.comercio.repository.PosComercioRepository;
import ec.edu.espe.gateway.comision.repository.ComisionRepository;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import ec.edu.espe.gateway.facturacion.services.FacturaService;
import ec.edu.espe.gateway.comision.services.ComisionService;
import ec.edu.espe.gateway.comercio.exception.NotFoundException;
import ec.edu.espe.gateway.comercio.exception.DuplicateException;
import ec.edu.espe.gateway.comercio.exception.InvalidDataException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class ComercioService {

    private static final Logger logger = LoggerFactory.getLogger(ComercioService.class);

    public static final String ENTITY_NAME = "Comercio";
    public static final String ENTITY_FACTURACION = "Facturación";
    public static final String ENTITY_COMISION = "Comisión";

    public static final String ESTADO_PENDIENTE = "PEN";
    public static final String ESTADO_ACTIVO = "ACT";
    public static final String ESTADO_INACTIVO = "INA";
    public static final String ESTADO_SUSPENDIDO = "SUS";

    private final ComercioRepository comercioRepository;
    private final PosComercioRepository posComercioRepository;
    private final FacturacionComercioRepository facturacionComercioRepository;
    private final TransaccionRepository transaccionRepository;

    public ComercioService(ComercioRepository comercioRepository,
            PosComercioRepository posComercioRepository,
            ComisionRepository comisionRepository,
            FacturacionComercioRepository facturacionComercioRepository,
            TransaccionRepository transaccionRepository,
            FacturaService facturaService,
            ComisionService comisionService) {
        this.comercioRepository = comercioRepository;
        this.posComercioRepository = posComercioRepository;
        this.facturacionComercioRepository = facturacionComercioRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public List<Comercio> obtenerTodos() {
        logger.info("Iniciando obtención de todos los comercios");
        List<Comercio> comercios = comercioRepository.findAll();
        logger.info("Finalizada obtención de todos los comercios");
        return comercios;
    }

    @Transactional(value = TxType.NEVER)
    public Comercio obtenerPorCodigo(Integer codigo) {
        logger.info("Obteniendo comercio por código: {}", codigo);
        Comercio comercio = comercioRepository.findById(codigo).orElseThrow(() -> 
            new NotFoundException(codigo.toString(), ENTITY_NAME));
        logger.info("Finalizada obtención de comercio por código: {}", codigo);
        return comercio;
    }

    public void registrarComercio(Comercio comercio) {
        try {
            logger.info("Registrando comercio: {}", comercio);
            validarNuevoComercio(comercio);
            comercio.setFechaCreacion(LocalDateTime.now());
            comercio.setEstado(ESTADO_PENDIENTE);
            comercioRepository.save(comercio);
        } catch (DuplicateException | InvalidDataException e) {
            logger.error("Error al registrar comercio: {}", e.getMessage());
            throw e;
        }
    }

    private void validarNuevoComercio(Comercio comercio) {
        if (comercio.getCodigoInterno() == null || comercio.getCodigoInterno().length() != 10) {
            throw new InvalidDataException("El código interno debe tener 10 caracteres");
        }
        if (comercioRepository.findByCodigoInterno(comercio.getCodigoInterno()).isPresent()) {
            throw new DuplicateException(comercio.getCodigoInterno(), "Comercio");
        }

        if (comercio.getRuc() == null || comercio.getRuc().length() != 13 || !comercio.getRuc().matches("\\d{13}")) {
            throw new InvalidDataException("El RUC debe tener exactamente 13 dígitos numéricos");
        }
        if (comercioRepository.findByRuc(comercio.getRuc()).isPresent()) {
            throw new DuplicateException(comercio.getRuc(), "Comercio");
        }

        if (comercio.getRazonSocial() == null || comercio.getRazonSocial().trim().isEmpty()
                || comercio.getRazonSocial().length() > 100) {
            throw new InvalidDataException("La razón social no puede estar vacía ni exceder 100 caracteres");
        }

        if (comercio.getNombreComercial() == null || comercio.getNombreComercial().trim().isEmpty()
                || comercio.getNombreComercial().length() > 100) {
            throw new InvalidDataException("El nombre comercial no puede estar vacío ni exceder 100 caracteres");
        }
    }

    @Transactional
    public void actualizarEstado(Integer codigo, String nuevoEstado) {
        logger.info("Iniciando actualización de estado del comercio con código {}: nuevo estado {}", codigo, nuevoEstado);
        try {
            Comercio comercio = obtenerPorCodigo(codigo);
            validarCambioEstado(comercio, nuevoEstado);
            validarFechasEstado(comercio, nuevoEstado);

            switch (nuevoEstado) {
                case ESTADO_ACTIVO:
                    comercio.setFechaActivacion(LocalDateTime.now());
                    comercio.setFechaSuspension(null);
                    comercio.setEstado(nuevoEstado);
                    comercioRepository.saveAndFlush(comercio);
                    crearFacturacionInicial(comercio);
                    break;
                case ESTADO_SUSPENDIDO:
                    validarSuspension(comercio);
                    comercio.setFechaSuspension(LocalDateTime.now());
                    comercio.setEstado(nuevoEstado);
                    List<Transaccion> transaccionesActivas = transaccionRepository
                            .findByComercioAndEstado(comercio, "ENV");
                    for (Transaccion transaccion : transaccionesActivas) {
                        transaccion.setEstado("REC");
                        transaccionRepository.saveAndFlush(transaccion);
                    }
                    break;
                case ESTADO_INACTIVO:
                    validarInactivacion(comercio);
                    comercio.setFechaActivacion(null);
                    comercio.setFechaSuspension(null);
                    comercio.setEstado(nuevoEstado);
                    transaccionesActivas = transaccionRepository
                            .findByComercioAndEstado(comercio, "ENV");
                    for (Transaccion transaccion : transaccionesActivas) {
                        transaccion.setEstado("REC");
                        transaccionRepository.saveAndFlush(transaccion);
                    }
                    List<PosComercio> dispositivosComercio = posComercioRepository
                            .findByComercio_Codigo(comercio.getCodigo());
                    for (PosComercio pos : dispositivosComercio) {
                        pos.setEstado("INA");
                        pos.setFechaActivacion(null);
                        posComercioRepository.save(pos);
                    }
                    break;
                case ESTADO_PENDIENTE:
                    comercio.setEstado(nuevoEstado);
                    break;
            }

            comercioRepository.saveAndFlush(comercio);
            actualizarEstadoDispositivosPos(comercio);
            logger.info("Finalizada actualización de estado del comercio con código {}: nuevo estado {}", codigo, nuevoEstado);

        } catch (Exception e) {
            logger.error("Error al actualizar estado: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar estado: " + e.getMessage());
        }
    }

    private void validarFechasEstado(Comercio comercio, String nuevoEstado) {
        LocalDateTime fechaActual = LocalDateTime.now();

        if (ESTADO_ACTIVO.equals(nuevoEstado) && comercio.getFechaActivacion() != null
                && comercio.getFechaActivacion().isAfter(fechaActual)) {
            throw new IllegalStateException("La fecha de activación no puede ser posterior a la fecha actual");
        }

        if (ESTADO_SUSPENDIDO.equals(nuevoEstado)) {
            if (comercio.getFechaActivacion() == null) {
                throw new IllegalStateException("No se puede suspender un comercio que no ha sido activado");
            }
            if (comercio.getFechaSuspension() != null &&
                    (comercio.getFechaSuspension().isBefore(comercio.getFechaActivacion()) ||
                            comercio.getFechaSuspension().isAfter(fechaActual))) {
                throw new IllegalStateException(
                        "La fecha de suspensión debe ser posterior a la activación y no futura");
            }
        }
    }

    private void validarSuspension(Comercio comercio) {
        List<FacturacionComercio> facturacionesPendientes = facturacionComercioRepository
                .findByComercioAndEstado(comercio, "ACT");
        if (!facturacionesPendientes.isEmpty()) {
            throw new IllegalStateException("No se puede suspender un comercio con facturaciones activas pendientes");
        }
    }

    private void validarInactivacion(Comercio comercio) {
        List<FacturacionComercio> facturacionesPendientes = facturacionComercioRepository
                .findByComercioAndEstado(comercio, "ACT");
        if (!facturacionesPendientes.isEmpty()) {
            throw new IllegalStateException("No se puede inactivar un comercio con facturaciones activas pendientes");
        }
    }

    private void actualizarEstadoDispositivosPos(Comercio comercio) {
        List<PosComercio> dispositivosPos = posComercioRepository.findByComercio(comercio);

        for (PosComercio dispositivo : dispositivosPos) {
            if (ESTADO_INACTIVO.equals(comercio.getEstado()) || ESTADO_SUSPENDIDO.equals(comercio.getEstado())) {
                dispositivo.setEstado("INA");
            } else if (ESTADO_ACTIVO.equals(comercio.getEstado())) {
                dispositivo.setFechaActivacion(LocalDateTime.now());
                dispositivo.setEstado("ACT");
            }
            posComercioRepository.save(dispositivo);
        }
    }

    private void validarCambioEstado(Comercio comercio, String nuevoEstado) {
        if (comercio.getEstado().equals(nuevoEstado)) {
            throw new IllegalStateException("El comercio ya se encuentra en estado: " + nuevoEstado);
        }

        switch (comercio.getEstado()) {
            case ESTADO_INACTIVO:
                if (!ESTADO_PENDIENTE.equals(nuevoEstado)) {
                    throw new IllegalStateException("Un comercio inactivo solo puede pasar a estado pendiente");
                }
                break;
            case ESTADO_PENDIENTE:
                if (!ESTADO_ACTIVO.equals(nuevoEstado) && !ESTADO_INACTIVO.equals(nuevoEstado)) {
                    throw new IllegalStateException("Un comercio pendiente solo puede activarse o inactivarse");
                }
                break;
            case ESTADO_ACTIVO:
                if (!ESTADO_SUSPENDIDO.equals(nuevoEstado) && !ESTADO_INACTIVO.equals(nuevoEstado)) {
                    throw new IllegalStateException("Un comercio activo solo puede suspenderse o inactivarse");
                }
                break;
            case ESTADO_SUSPENDIDO:
                if (!ESTADO_ACTIVO.equals(nuevoEstado) && !ESTADO_INACTIVO.equals(nuevoEstado)) {
                    throw new IllegalStateException("Un comercio suspendido solo puede activarse o inactivarse");
                }
                break;
        }
    }

    private void crearFacturacionInicial(Comercio comercio) {
        if (!"ACT".equals(comercio.getEstado())) {
            throw new IllegalStateException("El comercio debe estar activo para iniciar la facturación");
        }

        boolean existeFacturaActiva = facturacionComercioRepository
                .findFacturaActivaPorComercio(comercio.getCodigo())
                .isPresent();

        if (!existeFacturaActiva) {
            LocalDate fechaInicio = comercio.getFechaActivacion().toLocalDate();
            LocalDate fechaFin = fechaInicio.plusMonths(1);

            FacturacionComercio nuevaFactura = new FacturacionComercio();
            nuevaFactura.setFechaInicio(fechaInicio);
            nuevaFactura.setFechaFin(fechaFin);
            nuevaFactura.setEstado("ACT");
            nuevaFactura.setCodigoFacturacion(
                    "FACT-" + comercio.getCodigo() + "-" + fechaFin.format(DateTimeFormatter.ofPattern("yyyyMM")));
            nuevaFactura.setComercio(comercio);
            nuevaFactura.setComision(comercio.getComision());
            nuevaFactura.setTransaccionesAutorizadas(0);
            nuevaFactura.setTransaccionesProcesadas(0);
            nuevaFactura.setTransaccionesRechazadas(0);
            nuevaFactura.setTransaccionesReversadas(0);
            nuevaFactura.setValor(BigDecimal.ZERO);

            facturacionComercioRepository.save(nuevaFactura);
        }
    }
}