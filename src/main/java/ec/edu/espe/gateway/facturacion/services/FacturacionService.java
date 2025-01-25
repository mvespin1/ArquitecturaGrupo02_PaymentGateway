package ec.edu.espe.gateway.facturacion.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.services.TransaccionService;
import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.services.ComisionService;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import ec.edu.espe.gateway.exception.NotFoundException;
import ec.edu.espe.gateway.exception.InvalidDataException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FacturacionService {

    private static final Logger logger = LoggerFactory.getLogger(FacturacionService.class);

    public static final String ENTITY_NAME = "Factura";
    public static final String ENTITY_COMERCIO = "Comercio";
    public static final String ESTADO_ACTIVO = "ACT";
    public static final String ESTADO_FACTURADO = "FAC";
    public static final String ESTADO_PAGADO = "PAG";
    private static final int MAX_TRANSACCIONES_DIGITOS = 9;
    private static final int MAX_VALOR_ENTERO = 16;
    private static final int MAX_VALOR_DECIMAL = 4;
    private static final int MAX_CODIGO_FACTURACION = 20;
    private static final String REGEX_CODIGO_FACTURACION = "^[a-zA-Z0-9]{1," + MAX_CODIGO_FACTURACION + "}$";

    private final TransaccionService transaccionService;
    private final ComisionService comisionService;
    private final FacturacionComercioRepository facturacionComercioRepository;

    public FacturacionService(TransaccionService transaccionService,
            ComisionService comisionService, FacturacionComercioRepository facturacionComercioRepository) {
        this.transaccionService = transaccionService;
        this.comisionService = comisionService;
        this.facturacionComercioRepository = facturacionComercioRepository;
    }

    @Transactional
    public void procesarFacturacionAutomatica() {
        logger.info("Iniciando procesamiento automático de facturación");
        List<FacturacionComercio> facturasActivas = facturacionComercioRepository.findByEstado(ESTADO_ACTIVO);
        if (facturasActivas.isEmpty()) {
            logger.warn("No se encontraron facturas activas para procesar");
            throw new NotFoundException(ESTADO_ACTIVO, ENTITY_NAME);
        }

        for (FacturacionComercio factura : facturasActivas) {
            if (factura.getFechaFin().isBefore(LocalDate.now())) {
                procesarFactura(factura);
            }
        }
    }

    @Transactional
    public void procesarFactura(FacturacionComercio factura) {
        try {
            logger.info("Procesando factura con código: {}", factura.getCodigoFacturacion());
            BigDecimal totalComisiones = calcularComisiones(factura.getComercio(), factura);
            factura.setValor(totalComisiones);
            factura.setEstado(ESTADO_FACTURADO);
            facturacionComercioRepository.save(factura);

            crearNuevaFactura(factura.getComercio(), factura.getFechaFin());
        } catch (NotFoundException e) {
            logger.error("Error al procesar factura: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado al procesar factura: {}", e.getMessage());
            throw new RuntimeException("Error al procesar factura: " + e.getMessage());
        }
    }

    private void crearNuevaFactura(Comercio comercio, LocalDate fechaInicio) {
        try {
            logger.info("Creando nueva factura para el comercio: {}", comercio.getCodigo());
            LocalDate fechaFin = fechaInicio.plusMonths(1);

            FacturacionComercio nuevaFactura = new FacturacionComercio();
            nuevaFactura.setFechaInicio(fechaInicio);
            nuevaFactura.setFechaFin(fechaFin);
            nuevaFactura.setEstado(ESTADO_ACTIVO);
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
        } catch (Exception e) {
            logger.error("Error al crear nueva factura: {}", e.getMessage());
            throw new RuntimeException("Error al crear nueva factura: " + e.getMessage());
        }
    }

    private BigDecimal calcularComisiones(Comercio comercio, FacturacionComercio factura) {
        logger.info("Calculando comisiones para el comercio: {}", comercio.getCodigo());
        List<Transaccion> transacciones = transaccionService.obtenerPorComercioYFecha(
                comercio.getCodigo(), factura.getFechaInicio(), factura.getFechaFin());
        if (transacciones.isEmpty()) {
            logger.warn("No se encontraron transacciones para el comercio: {}", comercio.getCodigo());
            throw new NotFoundException(
                    comercio.getCodigo().toString(),
                    "Transacciones del comercio");
        }

        Optional<Comision> comisionFacturacionComercio = comisionService.findById(comercio.getComision().getCodigo());
        if (!comisionFacturacionComercio.isPresent()) {
            logger.warn("No se encontró la comisión para el comercio: {}", comercio.getCodigo());
            throw new NotFoundException(
                    comercio.getComision().getCodigo().toString(),
                    "Comisión");
        }

        Comision comision = comisionFacturacionComercio.get();
        Integer totalTransacciones = transacciones.size();
        BigDecimal montoTotalTransacciones = transacciones.stream()
                .map(Transaccion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal comisionTotal = BigDecimal.ZERO;

        if (Boolean.TRUE.equals(comision.getManejaSegmentos())) {
            List<ComisionSegmento> segmentos = comisionService.obtenerSegmentosPorComision(comision.getCodigo());
            for (ComisionSegmento segmento : segmentos) {
                int transaccionesEnSegmento = calcularTransaccionesEnSegmento(totalTransacciones, segmento);
                if (transaccionesEnSegmento > 0) {
                    if ("FIJ".equals(comision.getTipo())) {
                        comisionTotal = comisionTotal.add(segmento.getMonto().multiply(new BigDecimal(transaccionesEnSegmento)));
                    } else if ("POR".equals(comision.getTipo())) {
                        BigDecimal montoSegmento = montoTotalTransacciones.multiply(
                                new BigDecimal(transaccionesEnSegmento).divide(new BigDecimal(totalTransacciones), RoundingMode.HALF_UP));
                        comisionTotal = comisionTotal.add(montoSegmento.multiply(segmento.getMonto()));
                    }
                }
            }
        } else {
            if ("FIJ".equals(comision.getTipo())) {
                comisionTotal = comision.getMontoBase().multiply(new BigDecimal(totalTransacciones));
            } else if ("POR".equals(comision.getTipo())) {
                comisionTotal = montoTotalTransacciones.multiply(comision.getMontoBase());
            } else {
                logger.error("Tipo de comisión desconocido: {}", comision.getTipo());
                throw new IllegalArgumentException("Tipo de comisión desconocido: " + comision.getTipo());
            }
        }

        logger.info("Comisiones calculadas: {}", comisionTotal);
        return comisionTotal;
    }

    private int calcularTransaccionesEnSegmento(int totalTransacciones, ComisionSegmento segmento) {
        int desde = segmento.getPk().getTransaccionesDesde();
        int hasta = segmento.getTransaccionesHasta() == 0 ? totalTransacciones : segmento.getTransaccionesHasta();
        return Math.max(0, Math.min(totalTransacciones, hasta) - desde);
    }

    public FacturacionComercio obtenerPorCodigo(Integer codigo) {
        logger.info("Obteniendo facturación por código: {}", codigo);
        return facturacionComercioRepository.findById(codigo)
            .orElseThrow(() -> new NotFoundException(
                codigo.toString(), 
                ENTITY_NAME
            ));
    }

    public List<FacturacionComercio> obtenerPorEstado(String estado) {
        logger.info("Obteniendo facturaciones por estado: {}", estado);
        List<FacturacionComercio> facturaciones = facturacionComercioRepository.findByEstado(estado);
        if (facturaciones.isEmpty()) {
            logger.warn("No se encontraron facturaciones en estado: {}", estado);
            throw new NotFoundException(estado, ENTITY_NAME);
        }
        return facturaciones;
    }

    public void crear(FacturacionComercio facturacionComercio) {
        try {
            logger.info("Creando nueva facturación: {}", facturacionComercio.getCodigoFacturacion());
            validarFacturacion(facturacionComercio);
            facturacionComercio.setEstado(ESTADO_ACTIVO);
            facturacionComercioRepository.save(facturacionComercio);
        } catch (InvalidDataException e) {
            logger.error("Error de validación al crear facturación: {}", e.getMessage());
            throw e;
        } catch (Exception ex) {
            logger.error("Error al crear la facturación: {}", ex.getMessage());
            throw new RuntimeException("No se pudo crear la facturación de comercio. Motivo: " + ex.getMessage());
        }
    }

    public void actualizar(FacturacionComercio facturacionComercio) {
        try {
            logger.info("Actualizando facturación: {}", facturacionComercio.getCodigoFacturacion());
            FacturacionComercio existente = obtenerPorCodigo(facturacionComercio.getCodigo());
            validarFacturacion(facturacionComercio);
            existente.setFechaInicio(facturacionComercio.getFechaInicio());
            existente.setFechaFin(facturacionComercio.getFechaFin());
            existente.setTransaccionesProcesadas(facturacionComercio.getTransaccionesProcesadas());
            existente.setTransaccionesAutorizadas(facturacionComercio.getTransaccionesAutorizadas());
            existente.setTransaccionesRechazadas(facturacionComercio.getTransaccionesRechazadas());
            existente.setTransaccionesReversadas(facturacionComercio.getTransaccionesReversadas());
            existente.setValor(facturacionComercio.getValor());
            existente.setEstado(facturacionComercio.getEstado());
            existente.setCodigoFacturacion(facturacionComercio.getCodigoFacturacion());
            existente.setFechaFacturacion(facturacionComercio.getFechaFacturacion());
            existente.setFechaPago(facturacionComercio.getFechaPago());
            facturacionComercioRepository.save(existente);
        } catch (NotFoundException e) {
            logger.error("Facturación no encontrada para actualizar: {}", e.getMessage());
            throw e;
        } catch (InvalidDataException e) {
            logger.error("Error de validación al actualizar facturación: {}", e.getMessage());
            throw e;
        } catch (Exception ex) {
            logger.error("Error al actualizar la facturación: {}", ex.getMessage());
            throw new RuntimeException("No se pudo actualizar la facturación de comercio. Motivo: " + ex.getMessage());
        }
    }

    private void validarFacturacion(FacturacionComercio facturacionComercio) {
        if (facturacionComercio.getFechaInicio().isAfter(facturacionComercio.getFechaFin())) {
            throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        if (facturacionComercio.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("El valor de la facturación debe ser mayor a cero.");
        }
        if (facturacionComercio.getValor().scale() > MAX_VALOR_DECIMAL) {
            throw new InvalidDataException("El valor de la facturación no puede tener más de " + MAX_VALOR_DECIMAL + " decimales.");
        }
        if (facturacionComercio.getValor().precision() - facturacionComercio.getValor().scale() > MAX_VALOR_ENTERO) {
            throw new InvalidDataException("El valor de la facturación no puede tener más de " + MAX_VALOR_ENTERO + " dígitos antes del punto decimal.");
        }
        if (!facturacionComercio.getCodigoFacturacion().matches(REGEX_CODIGO_FACTURACION)) {
            throw new InvalidDataException("El código de facturación debe ser alfanumérico y no exceder los " + MAX_CODIGO_FACTURACION + " caracteres.");
        }
        if (facturacionComercio.getTransaccionesProcesadas() < 0 || facturacionComercio.getTransaccionesProcesadas() > MAX_TRANSACCIONES_DIGITOS) {
            throw new InvalidDataException("El número de transacciones procesadas no puede ser negativo ni exceder los 9 dígitos.");
        }
        if (facturacionComercio.getTransaccionesAutorizadas() < 0 || facturacionComercio.getTransaccionesAutorizadas() > MAX_TRANSACCIONES_DIGITOS) {
            throw new InvalidDataException("El número de transacciones autorizadas no puede ser negativo ni exceder los 9 dígitos.");
        }
        if (facturacionComercio.getTransaccionesRechazadas() < 0 || facturacionComercio.getTransaccionesRechazadas() > MAX_TRANSACCIONES_DIGITOS) {
            throw new InvalidDataException("El número de transacciones rechazadas no puede ser negativo ni exceder los 9 dígitos.");
        }
        if (facturacionComercio.getTransaccionesReversadas() < 0 || facturacionComercio.getTransaccionesReversadas() > MAX_TRANSACCIONES_DIGITOS) {
            throw new InvalidDataException("El número de transacciones reversadas no puede ser negativo ni exceder los 9 dígitos.");
        }
    }

    public List<FacturacionComercio> obtenerFacturacionesPendientesPago() {
        logger.info("Obteniendo facturaciones pendientes de pago");
        return facturacionComercioRepository.findByEstado(ESTADO_FACTURADO);
    }

    public void marcarComoPagado(Integer codigo) {
        try {
            logger.info("Marcando como pagada la facturación con código: {}", codigo);
            FacturacionComercio existente = obtenerPorCodigo(codigo);
            if (!ESTADO_FACTURADO.equals(existente.getEstado())) {
                logger.warn("La facturación no está en estado 'FAC' para ser marcada como pagada");
                throw new IllegalStateException(
                        "Solo se pueden marcar como pagadas las facturaciones en estado 'FAC'.");
            }
            existente.setEstado(ESTADO_PAGADO);
            existente.setFechaPago(java.time.LocalDate.now());
            facturacionComercioRepository.save(existente);
        } catch (NotFoundException e) {
            logger.error("Facturación no encontrada para marcar como pagada: {}", e.getMessage());
            throw e;
        } catch (Exception ex) {
            logger.error("Error al marcar la facturación como pagada: {}", ex.getMessage());
            throw new RuntimeException("No se pudo marcar la facturación como pagada. Motivo: " + ex.getMessage());
        }
    }
} 