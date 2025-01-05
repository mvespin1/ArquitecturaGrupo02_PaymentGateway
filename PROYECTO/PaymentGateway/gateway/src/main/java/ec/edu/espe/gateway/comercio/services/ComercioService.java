package ec.edu.espe.gateway.comercio.services;

import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.comercio.repository.PosComercioRepository;
import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.repository.ComisionRepository;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional.TxType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ComercioService {

    public static final String ESTADO_PENDIENTE = "PEN";
    public static final String ESTADO_ACTIVO = "ACT";
    public static final String ESTADO_INACTIVO = "INA";
    public static final String ESTADO_SUSPENDIDO = "SUS";

    private final ComercioRepository comercioRepository;
    private final PosComercioRepository posComercioRepository;
    private final ComisionRepository comisionRepository;
    private final FacturacionComercioRepository facturacionComercioRepository;
    private final TransaccionRepository transaccionRepository;

    public ComercioService(ComercioRepository comercioRepository,
            PosComercioRepository posComercioRepository,
            ComisionRepository comisionRepository,
            FacturacionComercioRepository facturacionComercioRepository,
            TransaccionRepository transaccionRepository) {
        this.comercioRepository = comercioRepository;
        this.posComercioRepository = posComercioRepository;
        this.comisionRepository = comisionRepository;
        this.facturacionComercioRepository = facturacionComercioRepository;
        this.transaccionRepository = transaccionRepository;
    }

    @Transactional(value = TxType.NEVER)
    public Comercio obtenerPorCodigo(Integer codigo) {
        return comercioRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("No existe comercio con el código: " + codigo));
    }

    public void registrarComercio(Comercio comercio) {
        try {
            validarNuevoComercio(comercio);
            comercio.setFechaCreacion(LocalDateTime.now());
            comercio.setEstado(ESTADO_PENDIENTE);
            comercioRepository.save(comercio);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar comercio: " + e.getMessage());
        }
    }

    private void validarNuevoComercio(Comercio comercio) {
        // Validar código interno
        if (comercio.getCodigoInterno() == null || comercio.getCodigoInterno().length() != 10) {
            throw new IllegalArgumentException("El código interno debe tener 10 caracteres");
        }
        if (comercioRepository.findByCodigoInterno(comercio.getCodigoInterno()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un comercio con el código interno: " + comercio.getCodigoInterno());
        }

        // Validar RUC
        if (comercio.getRuc() == null || comercio.getRuc().length() != 13 || !comercio.getRuc().matches("\\d{13}")) {
            throw new IllegalArgumentException("El RUC debe tener exactamente 13 dígitos numéricos");
        }
        if (comercioRepository.findByRuc(comercio.getRuc()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un comercio con el RUC: " + comercio.getRuc());
        }

        // Validar razón social y nombre comercial
        if (comercio.getRazonSocial() == null || comercio.getRazonSocial().trim().isEmpty()
                || comercio.getRazonSocial().length() > 100) {
            throw new IllegalArgumentException("La razón social no puede estar vacía ni exceder 100 caracteres");
        }

        if (comercio.getNombreComercial() == null || comercio.getNombreComercial().trim().isEmpty()
                || comercio.getNombreComercial().length() > 100) {
            throw new IllegalArgumentException("El nombre comercial no puede estar vacío ni exceder 100 caracteres");
        }
    }

    public void actualizarEstado(Integer codigo, String nuevoEstado) {
        try {
            Comercio comercio = obtenerPorCodigo(codigo);
            validarCambioEstado(comercio, nuevoEstado);
            validarFechasEstado(comercio, nuevoEstado);

            switch (nuevoEstado) {
                case ESTADO_ACTIVO:
                    comercio.setFechaActivacion(LocalDateTime.now());
                    comercio.setFechaSuspension(null);
                    break;
                case ESTADO_SUSPENDIDO:
                    validarSuspension(comercio);
                    comercio.setFechaSuspension(LocalDateTime.now());
                    cancelarTransaccionesActivas(comercio);
                    break;
                case ESTADO_INACTIVO:
                    validarInactivacion(comercio);
                    comercio.setFechaActivacion(null);
                    comercio.setFechaSuspension(null);
                    cancelarTransaccionesActivas(comercio);
                    break;
                case ESTADO_PENDIENTE:
                    validarRetornoAPendiente(comercio);
                    break;
            }

            comercio.setEstado(nuevoEstado);
            comercioRepository.save(comercio);
            actualizarEstadoDispositivos(comercio);

        } catch (Exception e) {
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

    private void validarRetornoAPendiente(Comercio comercio) {
        // Validar que no haya pagos pendientes
        List<FacturacionComercio> facturacionesPendientes = facturacionComercioRepository
                .findByComercioAndEstado(comercio, "FAC");
        if (!facturacionesPendientes.isEmpty()) {
            throw new IllegalStateException("El comercio tiene facturas pendientes de pago");
        }
    }

    private void cancelarTransaccionesActivas(Comercio comercio) {
        List<Transaccion> transaccionesActivas = transaccionRepository.findByComercioAndEstado(comercio, "ENV");
        for (Transaccion transaccion : transaccionesActivas) {
            transaccion.setEstado("REC");
            transaccionRepository.save(transaccion);
        }
    }

    private void actualizarEstadoDispositivos(Comercio comercio) {
        List<PosComercio> dispositivos = posComercioRepository.findByComercio(comercio);
        LocalDateTime fechaActivacionComercio = comercio.getFechaActivacion();

        for (PosComercio dispositivo : dispositivos) {
            if (ESTADO_INACTIVO.equals(comercio.getEstado()) || ESTADO_SUSPENDIDO.equals(comercio.getEstado())) {
                dispositivo.setEstado("INA");
            } else if (ESTADO_ACTIVO.equals(comercio.getEstado())) {
                // Validar que la fecha de activación del POS no sea anterior a la del comercio
                if (fechaActivacionComercio != null &&
                        dispositivo.getFechaActivacion().isBefore(fechaActivacionComercio)) {
                    dispositivo.setFechaActivacion(fechaActivacionComercio);
                }
                dispositivo.setEstado("ACT");
            }
            posComercioRepository.save(dispositivo);
        }
    }

    public void actualizarPagosAceptados(Integer codigo, String pagosAceptados) {
        try {
            Comercio comercio = obtenerPorCodigo(codigo);
            if (!ESTADO_ACTIVO.equals(comercio.getEstado())) {
                throw new IllegalStateException("Solo se pueden actualizar los pagos aceptados de comercios activos");
            }
            validarPagosAceptados(pagosAceptados);
            comercio.setPagosAceptados(pagosAceptados);
            comercioRepository.save(comercio);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar pagos aceptados: " + e.getMessage());
        }
    }

    private void validarPagosAceptados(String pagosAceptados) {
        if (!"SIM".equals(pagosAceptados) && !"REC".equals(pagosAceptados) && !"DOS".equals(pagosAceptados)) {
            throw new IllegalArgumentException("PAGOS_ACEPTADOS debe ser SIM, REC o DOS");
        }
    }

    public void asignarComision(Integer codigoComercio, Integer codigoComision) {
        try {
            Comercio comercio = obtenerPorCodigo(codigoComercio);
            Comision comision = comisionRepository.findById(codigoComision)
                    .orElseThrow(
                            () -> new EntityNotFoundException("No existe la comisión con código: " + codigoComision));

            comercio.setComision(comision);
            comercioRepository.save(comercio);
        } catch (Exception e) {
            throw new RuntimeException("Error al asignar comisión: " + e.getMessage());
        }
    }

    @Transactional(value = TxType.NEVER)
    public List<Comercio> listarComerciosPorEstado(String estado) {
        return comercioRepository.findByEstado(estado);
    }

    @Transactional(value = TxType.NEVER)
    public List<Comercio> buscarPorRazonSocialONombreComercial(String criterio) {
        return comercioRepository.findByRazonSocialContainingIgnoreCaseOrNombreComercialContainingIgnoreCase(criterio,
                criterio);
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

    public Optional<Comercio> findByCodigoInterno(String codigoInterno) {
        return comercioRepository.findByCodigoInterno(codigoInterno);
    }
}