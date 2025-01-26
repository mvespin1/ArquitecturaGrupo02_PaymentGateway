package ec.edu.espe.gateway.transaccion.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.comercio.repository.PosComercioRepository;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import ec.edu.espe.gateway.transaccion.client.ValidacionTransaccionClient;
import ec.edu.espe.gateway.transaccion.controller.dto.ActualizacionEstadoDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.RespuestaValidacionDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.ValidacionTransaccionDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.DatosTarjetaDTO;
import ec.edu.espe.gateway.transaccion.controller.mapper.TransaccionMapper;
import ec.edu.espe.gateway.transaccion.client.PosClient;
import ec.edu.espe.gateway.exception.ValidationException;
import ec.edu.espe.gateway.comision.services.ComisionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;

@Service
@Transactional
public class TransaccionService {

    private static final Logger log = LoggerFactory.getLogger(TransaccionService.class);

    public static final String ESTADO_ENVIADO = "ENV";
    public static final String ESTADO_AUTORIZADO = "AUT";
    public static final String ESTADO_RECHAZADO = "REC";
    public static final String ESTADO_REVERSADO = "REV";

    private final TransaccionRepository transaccionRepository;
    private final ComercioRepository comercioRepository;
    private final ValidacionTransaccionClient validacionTransaccionClient;
    private final PosClient posClient;
    private final ObjectMapper objectMapper;
    private final TransaccionMapper transaccionMapper;

    public TransaccionService(TransaccionRepository transaccionRepository,
            ComercioRepository comercioRepository,
            PosComercioRepository posComercioRepository,
            FacturacionComercioRepository facturacionComercioRepository,
            ValidacionTransaccionClient validacionTransaccionClient,
            PosClient posClient,
            ObjectMapper objectMapper,
            TransaccionMapper transaccionMapper,
            ComisionService comisionService) {
        this.transaccionRepository = transaccionRepository;
        this.comercioRepository = comercioRepository;
        this.validacionTransaccionClient = validacionTransaccionClient;
        this.posClient = posClient;
        this.objectMapper = objectMapper;
        this.transaccionMapper = transaccionMapper;
    }

    @Transactional
    public void procesarTransaccionPOS(Transaccion transaccion) {
        log.info("Iniciando procesamiento de transacción POS: {}", transaccion);

        try {
            validarTransaccion(transaccion);

            transaccion.setEstado(ESTADO_ENVIADO);
            transaccion = transaccionRepository.save(transaccion);
            log.info("Transacción guardada inicialmente en gateway con ID: {} y estado: {}",
                    transaccion.getCodigo(), transaccion.getEstado());

            procesarConSistemaExterno(transaccion);

        } catch (ValidationException e) {
            log.error("Error de validación: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al procesar transacción POS: {}", e.getMessage());
            throw new ValidationException("general", "Error al procesar transacción POS: " + e.getMessage());
        }
    }

    private void procesarConSistemaExterno(Transaccion transaccion) {
        try {
            ValidacionTransaccionDTO validacionDTO = prepararValidacionDTO(transaccion);

            ResponseEntity<RespuestaValidacionDTO> respuesta = validacionTransaccionClient
                    .validarTransaccion(validacionDTO);
            log.info("Respuesta del sistema externo - Status: {}, Body: {}",
                    respuesta.getStatusCode(), respuesta.getBody());

            String nuevoEstado;
            String mensaje;

            if (respuesta.getStatusCode().value() == 201) {
                nuevoEstado = ESTADO_AUTORIZADO;
                mensaje = "Transacción autorizada por el sistema externo";
            } else {
                nuevoEstado = ESTADO_RECHAZADO;
                mensaje = "Transacción rechazada por el sistema externo";
            }

            transaccion.setEstado(nuevoEstado);
            transaccion = transaccionRepository.save(transaccion);

            notificarActualizacionAlPOS(transaccion, mensaje);

        } catch (Exception e) {
            log.error("Error en procesamiento con sistema externo: {}", e.getMessage());
            transaccion.setEstado(ESTADO_RECHAZADO);
            transaccion = transaccionRepository.save(transaccion);
            String mensajeError = "Error en procesamiento externo";
            notificarActualizacionAlPOS(transaccion, mensajeError);
        }
    }

    private ValidacionTransaccionDTO prepararValidacionDTO(Transaccion transaccion) {
        try {
            ValidacionTransaccionDTO dto = transaccionMapper.toDTO(transaccion);
            DatosTarjetaDTO datosTarjeta = extraerDatosTarjeta(transaccion.getTarjeta());

            dto.setCodigoBanco(1);
            dto.setCodigoMoneda("USD");
            dto.setFechaExpiracionTarjeta(datosTarjeta.getExpiryDate());
            dto.setNombreTarjeta(datosTarjeta.getNombreTarjeta());
            dto.setNumeroTarjeta(datosTarjeta.getCardNumber());
            dto.setDireccionTarjeta(datosTarjeta.getDireccionTarjeta());
            dto.setCvv(datosTarjeta.getCvv());
            dto.setPais("EC");
            dto.setNumeroCuenta("00000003");
            dto.setGtwComision("0");
            dto.setGtwCuenta("00000002");

            String codigoUnico = generarCodigoUnicoTransaccion(
                    transaccion.getCodigo(),
                    transaccion.getFecha(),
                    transaccion.getFacturacionComercio().getCodigo());
            dto.setCodigoUnicoTransaccion(codigoUnico);

            return dto;
        } catch (Exception e) {
            log.error("Error al preparar DTO de validación: {}", e.getMessage());
            throw new ValidationException("preparacion_dto",
                    "Error al preparar datos para validación: " + e.getMessage());
        }
    }

    private void notificarActualizacionAlPOS(Transaccion transaccion, String mensaje) {
        try {
            ActualizacionEstadoDTO actualizacion = new ActualizacionEstadoDTO();
            actualizacion.setCodigoUnicoTransaccion(transaccion.getCodigoUnicoTransaccion());
            actualizacion.setEstado(transaccion.getEstado());
            actualizacion.setMensaje(limitarLongitudMensaje(mensaje));

            ResponseEntity<Void> response = posClient.actualizarEstadoTransaccion(actualizacion);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Error al notificar al POS. Status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error al notificar al POS: {}", e.getMessage());
        }
    }

    private void validarTransaccion(Transaccion transaccion) {
        if (!"SIM".equals(transaccion.getTipo()) && !"REC".equals(transaccion.getTipo())) {
            throw new ValidationException("tipo", "Tipo de transacción debe ser 'SIM' o 'REC'");
        }
        if (transaccion.getMarca() == null || transaccion.getMarca().trim().isEmpty()) {
            throw new ValidationException("marca", "La marca es requerida");
        }
        if (transaccion.getMonto() == null || transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("monto", "El monto debe ser mayor a 0");
        }
        LocalDateTime fechaActual = LocalDateTime.now();
        if (transaccion.getFecha() != null && transaccion.getFecha().isAfter(fechaActual)) {
            throw new ValidationException("fecha", "La fecha de la transacción no puede ser futura");
        }
        if ("REC".equals(transaccion.getTipo())) {
            validarFechasRecurrencia(transaccion);
        }
    }

    private void validarFechasRecurrencia(Transaccion transaccion) {
        LocalDate fechaActual = LocalDate.now();

        if (transaccion.getFechaEjecucionRecurrencia() == null) {
            throw new ValidationException("fechaEjecucionRecurrencia",
                    "La fecha de ejecución es requerida para transacciones recurrentes");
        }
        if (transaccion.getFechaFinRecurrencia() == null) {
            throw new ValidationException("fechaFinRecurrencia",
                    "La fecha de fin es requerida para transacciones recurrentes");
        }
        if (transaccion.getFechaEjecucionRecurrencia().isBefore(fechaActual)) {
            throw new ValidationException("fechaEjecucionRecurrencia",
                    "La fecha de ejecución no puede ser anterior a la fecha actual");
        }
        if (transaccion.getFechaFinRecurrencia().isBefore(transaccion.getFechaEjecucionRecurrencia())) {
            throw new ValidationException("fechaFinRecurrencia",
                    "La fecha de fin debe ser posterior a la fecha de ejecución");
        }
    }

    public void detenerTransaccionesRecurrentes(Integer codigoComercio) {
        try {
            Comercio comercio = comercioRepository.findById(codigoComercio)
                    .orElseThrow(() -> new ValidationException("comercio", "Comercio no encontrado"));

            if (!"INA".equals(comercio.getEstado()) && !"SUS".equals(comercio.getEstado())) {
                throw new ValidationException(comercio.getEstado(),
                        "detener recurrencias (solo permitido para comercios inactivos o suspendidos)");
            }

            List<Transaccion> transaccionesRecurrentes = transaccionRepository
                    .findActiveRecurrentTransactionsByComercio(codigoComercio);

            LocalDate fechaDetencion = LocalDate.now();
            for (Transaccion transaccion : transaccionesRecurrentes) {
                transaccion.setEstado(ESTADO_RECHAZADO);
                transaccion.setFechaFinRecurrencia(fechaDetencion);
                transaccionRepository.save(transaccion);
            }
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationException("general", "Error al detener transacciones recurrentes: " + e.getMessage());
        }
    }

    private String limitarLongitudMensaje(String mensaje) {
        final int MAX_LENGTH = 200;
        if (mensaje == null) {
            return "";
        }
        return mensaje.length() > MAX_LENGTH ? mensaje.substring(0, MAX_LENGTH) : mensaje;
    }

    private DatosTarjetaDTO extraerDatosTarjeta(String jsonTarjeta) {
        try {
            return objectMapper.readValue(jsonTarjeta, DatosTarjetaDTO.class);
        } catch (Exception e) {
            log.error("Error al procesar datos de tarjeta: {}", e.getMessage());
            throw new ValidationException("tarjeta",
                    "Error al procesar datos de tarjeta: " + e.getMessage());
        }
    }

    private String generarCodigoUnicoTransaccion(int codigo, LocalDateTime fecha, int codigoFacturacion) {
        return String.format("TRX%06d-%d-%02d-%02d-%02d-%02d-%02d-%012d",
                codigo,
                fecha.getYear(),
                fecha.getMonthValue(),
                fecha.getDayOfMonth(),
                fecha.getHour(),
                fecha.getMinute(),
                fecha.getSecond(),
                codigoFacturacion);
    }

    public List<Transaccion> obtenerPorComercioYFecha(Integer codigoComercio,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        return transaccionRepository.findByComercio_CodigoAndFechaBetween(
                codigoComercio, fechaInicio, fechaFin);
    }

}
