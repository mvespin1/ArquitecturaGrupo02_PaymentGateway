package ec.edu.espe.gateway.transaccion.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.comercio.repository.PosComercioRepository;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.transaccion.client.ValidacionTransaccionClient;
import ec.edu.espe.gateway.transaccion.controller.dto.ActualizacionEstadoDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.RespuestaValidacionDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.ValidacionTransaccionDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.BancoDTO;
import ec.edu.espe.gateway.transaccion.controller.mapper.TransaccionMapper;
import ec.edu.espe.gateway.transaccion.client.PosClient;
import ec.edu.espe.gateway.transaccion.exception.TransaccionNotFoundException;
import ec.edu.espe.gateway.transaccion.exception.TransaccionValidationException;
import ec.edu.espe.gateway.transaccion.exception.TransaccionStateException;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
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
    private final PosComercioRepository posComercioRepository;
    private final FacturacionComercioRepository facturacionComercioRepository;
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
            TransaccionMapper transaccionMapper) {
        this.transaccionRepository = transaccionRepository;
        this.comercioRepository = comercioRepository;
        this.posComercioRepository = posComercioRepository;
        this.facturacionComercioRepository = facturacionComercioRepository;
        this.validacionTransaccionClient = validacionTransaccionClient;
        this.posClient = posClient;
        this.objectMapper = objectMapper;
        this.transaccionMapper = transaccionMapper;
    }

    public Transaccion crearTransaccionPOS(Transaccion transaccion, String codigoPos) {
        try {
            LocalDateTime fechaActual = LocalDateTime.now();

            if (transaccion.getFecha() != null && transaccion.getFecha().isAfter(fechaActual)) {
                throw new TransaccionValidationException("fecha", "La fecha de la transacción no puede ser futura");
            }

            // Validar POS
            PosComercio pos = posComercioRepository.findById(new PosComercioPK(codigoPos, "POS"))
                    .orElseThrow(() -> new TransaccionValidationException("codigoPos", "POS no encontrado"));
            if (!"ACT".equals(pos.getEstado())) {
                throw new TransaccionStateException("INACTIVO", "crear transacción");
            }

            // Validar comercio
            Comercio comercio = pos.getComercio();
            if (!"ACT".equals(comercio.getEstado())) {
                throw new TransaccionStateException("INACTIVO", "crear transacción desde comercio");
            }

            // Obtener facturación activa
            FacturacionComercio facturacionActiva = facturacionComercioRepository
                    .findByComercioAndEstado(comercio, "ACT")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new TransaccionValidationException("comercio", "No existe facturación activa para el comercio"));

            // Configurar campos inmutables
            transaccion.setComercio(comercio);
            transaccion.setFacturacionComercio(facturacionActiva);
            transaccion.setFecha(fechaActual);
            transaccion.setEstado(ESTADO_ENVIADO);
            transaccion.setCodigoUnicoTransaccion(generarCodigoUnico());

            validarTransaccion(transaccion);
            return transaccionRepository.save(transaccion);
        } catch (TransaccionValidationException | TransaccionStateException e) {
            throw e;
        } catch (Exception e) {
            throw new TransaccionValidationException("general", "Error al crear transacción: " + e.getMessage());
        }
    }

    private void validarTransaccion(Transaccion transaccion) {
        // Validar tipo
        if (!"SIM".equals(transaccion.getTipo()) && !"REC".equals(transaccion.getTipo())) {
            throw new TransaccionValidationException("tipo", "Tipo de transacción debe ser 'SIM' o 'REC'");
        }

        // Validar marca
        if (transaccion.getMarca() == null || transaccion.getMarca().trim().isEmpty()) {
            throw new TransaccionValidationException("marca", "La marca es requerida");
        }

        // Validar monto
        if (transaccion.getMonto() == null || transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransaccionValidationException("monto", "El monto debe ser mayor a 0");
        }

        // Validar fecha
        LocalDateTime fechaActual = LocalDateTime.now();
        if (transaccion.getFecha() != null && transaccion.getFecha().isAfter(fechaActual)) {
            throw new TransaccionValidationException("fecha", "La fecha de la transacción no puede ser futura");
        }

        // Validar fechas para transacciones recurrentes
        if ("REC".equals(transaccion.getTipo())) {
            validarFechasRecurrencia(transaccion);
        }
    }

    private void validarFechasRecurrencia(Transaccion transaccion) {
        LocalDate fechaActual = LocalDate.now();

        if (transaccion.getFechaEjecucionRecurrencia() == null) {
            throw new TransaccionValidationException("fechaEjecucionRecurrencia", "La fecha de ejecución es requerida para transacciones recurrentes");
        }
        if (transaccion.getFechaFinRecurrencia() == null) {
            throw new TransaccionValidationException("fechaFinRecurrencia", "La fecha de fin es requerida para transacciones recurrentes");
        }
        if (transaccion.getFechaEjecucionRecurrencia().isBefore(fechaActual)) {
            throw new TransaccionValidationException("fechaEjecucionRecurrencia", "La fecha de ejecución no puede ser anterior a la fecha actual");
        }
        if (transaccion.getFechaFinRecurrencia().isBefore(transaccion.getFechaEjecucionRecurrencia())) {
            throw new TransaccionValidationException("fechaFinRecurrencia", "La fecha de fin debe ser posterior a la fecha de ejecución");
        }
    }

    private String generarCodigoUnico() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString();
        } while (transaccionRepository.existsByCodigoUnicoTransaccion(codigo));
        return codigo;
    }

    public void actualizarEstado(Integer codigo, String nuevoEstado) {
        try {
            Transaccion transaccion = transaccionRepository.findById(codigo)
                    .orElseThrow(() -> new TransaccionNotFoundException(codigo.toString()));

            validarCambioEstado(transaccion.getEstado(), nuevoEstado);
            transaccion.setEstado(nuevoEstado);
            transaccionRepository.save(transaccion);
        } catch (TransaccionNotFoundException | TransaccionStateException e) {
            throw e;
        } catch (Exception e) {
            throw new TransaccionValidationException("estado", "Error al actualizar estado: " + e.getMessage());
        }
    }

    private void validarCambioEstado(String estadoActual, String nuevoEstado) {
        if (ESTADO_AUTORIZADO.equals(estadoActual) && !ESTADO_REVERSADO.equals(nuevoEstado)) {
            throw new TransaccionStateException(estadoActual, "cambio a estado " + nuevoEstado);
        }
        if (ESTADO_RECHAZADO.equals(estadoActual) || ESTADO_REVERSADO.equals(estadoActual)) {
            throw new TransaccionStateException(estadoActual, "cambio de estado");
        }
    }

    public void detenerTransaccionesRecurrentes(Integer codigoComercio) {
        try {
            // Validar estado del comercio
            Comercio comercio = comercioRepository.findById(codigoComercio)
                    .orElseThrow(() -> new TransaccionValidationException("comercio", "Comercio no encontrado"));

            if (!"INA".equals(comercio.getEstado()) && !"SUS".equals(comercio.getEstado())) {
                throw new TransaccionStateException(comercio.getEstado(), 
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
        } catch (TransaccionValidationException | TransaccionStateException e) {
            throw e;
        } catch (Exception e) {
            throw new TransaccionValidationException("general", "Error al detener transacciones recurrentes: " + e.getMessage());
        }
    }

    public void procesarCambioEstadoComercio(Integer codigoComercio, String nuevoEstado) {
        if ("INA".equals(nuevoEstado) || "SUS".equals(nuevoEstado)) {
            Comercio comercio = comercioRepository.findById(codigoComercio)
                    .orElseThrow(() -> new TransaccionValidationException("comercio", "Comercio no encontrado"));

            List<Transaccion> transaccionesEnCurso = transaccionRepository.findByComercioAndEstado(comercio,
                    ESTADO_ENVIADO);

            // Rechazar transacciones en curso
            for (Transaccion transaccion : transaccionesEnCurso) {
                transaccion.setEstado(ESTADO_RECHAZADO);
                transaccionRepository.save(transaccion);
            }

            // Detener transacciones recurrentes
            detenerTransaccionesRecurrentes(codigoComercio);
        }
    }

    // Métodos de consulta
    public List<Transaccion> obtenerPorEstado(String estado) {
        return transaccionRepository.findByEstado(estado);
    }

    public List<Transaccion> obtenerPorComercioYFecha(Integer codigoComercio,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        return transaccionRepository.findByComercio_CodigoAndFechaBetween(
                codigoComercio, fechaInicio, fechaFin);
    }

    @Transactional
    public void procesarTransaccionPOS(Transaccion transaccion) {
        log.info("Iniciando procesamiento de transacción POS: {}", transaccion);

        try {
            // Fase 1: Validación y guardado inicial
            Transaccion transaccionInicial = guardarTransaccionInicial(transaccion);
            log.info("Transacción guardada inicialmente en gateway con ID: {} y estado: {}", 
                    transaccionInicial.getCodigo(), transaccionInicial.getEstado());

            // Fase 2: Procesamiento asíncrono con sistema externo
            new Thread(() -> {
                try {
                    procesarConSistemaExterno(transaccionInicial);
                } catch (Exception e) {
                    log.error("Error en procesamiento asíncrono de transacción: {}", e.getMessage());
                    actualizarEstado(transaccionInicial.getCodigo(), ESTADO_RECHAZADO);
                    notificarActualizacionAlPOS(transaccionInicial, "Error en procesamiento: " + e.getMessage());
                }
            }).start();

        } catch (Exception e) {
            log.error("Error al procesar transacción POS: {}", e.getMessage());
            throw new TransaccionValidationException("general", "Error al procesar transacción: " + e.getMessage());
        }
    }

    @Transactional
    public Transaccion guardarTransaccionInicial(Transaccion transaccion) {
        log.info("Guardando transacción inicial: {}", transaccion);

        try {
            // Validar comercio
            Comercio comercio = comercioRepository.findById(transaccion.getComercio().getCodigo())
                    .orElseThrow(() -> new TransaccionValidationException("comercio", "Comercio no encontrado"));

            if (!"ACT".equals(comercio.getEstado())) {
                throw new TransaccionStateException(comercio.getEstado(), "crear transacción");
            }

            // Validar facturación
            FacturacionComercio facturacionActiva = facturacionComercioRepository
                    .findByComercioAndEstado(comercio, "ACT")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new TransaccionValidationException("comercio", 
                        "No existe facturación activa para el comercio"));

            // Configurar campos inmutables
            transaccion.setComercio(comercio);
            transaccion.setFacturacionComercio(facturacionActiva);
            transaccion.setFecha(LocalDateTime.now());
            transaccion.setEstado(ESTADO_ENVIADO);
            transaccion.setCodigoUnicoTransaccion(generarCodigoUnico());

            validarTransaccion(transaccion);
            return transaccionRepository.save(transaccion);

        } catch (TransaccionValidationException | TransaccionStateException e) {
            throw e;
        } catch (Exception e) {
            throw new TransaccionValidationException("general", "Error al guardar transacción: " + e.getMessage());
        }
    }

    @Transactional
    public void procesarConSistemaExterno(Transaccion transaccion) {
        try {
            ValidacionTransaccionDTO validacionDTO = prepararValidacionDTO(transaccion);
            
            // Loguear el JSON que se enviará
            try {
                String jsonRequest = objectMapper.writeValueAsString(validacionDTO);
                log.info("JSON a enviar al sistema externo: {}", jsonRequest);
            } catch (Exception e) {
                log.warn("Error al serializar DTO a JSON: {}", e.getMessage());
            }
            
            try {
                ResponseEntity<RespuestaValidacionDTO> respuesta = validacionTransaccionClient.validarTransaccion(validacionDTO);
                log.info("Respuesta del sistema externo - Status: {}, Body: {}", 
                        respuesta.getStatusCode(), respuesta.getBody());
                
                String mensaje;
                if (respuesta.getStatusCode().value() == 201) {
                    transaccion.setEstado(ESTADO_AUTORIZADO);
                    mensaje = respuesta.getBody() != null ? respuesta.getBody().getMensaje() : "Transacción autorizada";
                    log.info("Transacción autorizada por sistema externo");
                } else {
                    transaccion.setEstado(ESTADO_RECHAZADO);
                    mensaje = respuesta.getBody() != null ? respuesta.getBody().getMensaje() : "Transacción rechazada";
                    log.info("Transacción rechazada por sistema externo");
                }

                transaccion = transaccionRepository.save(transaccion);
                log.info("Estado de transacción actualizado en gateway a: {}", transaccion.getEstado());
                notificarActualizacionAlPOS(transaccion, mensaje);
                
            } catch (feign.FeignException.BadRequest e) {
                log.error("Error de validación en sistema externo: {}", e.getMessage());
                throw new TransaccionValidationException("validacion_externa", "Error de validación en sistema externo: " + e.getMessage());
            } catch (Exception e) {
                log.error("Error en validación externa: {}", e.getMessage());
                throw new TransaccionStateException(transaccion.getEstado(), "validación externa: " + e.getMessage());
            }
        } catch (TransaccionValidationException | TransaccionStateException e) {
            transaccion.setEstado(ESTADO_RECHAZADO);
            transaccionRepository.save(transaccion);
            notificarActualizacionAlPOS(transaccion, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al procesar con sistema externo: {}", e.getMessage());
            transaccion.setEstado(ESTADO_RECHAZADO);
            transaccionRepository.save(transaccion);
            notificarActualizacionAlPOS(transaccion, "Error en procesamiento externo: " + e.getMessage());
            throw new TransaccionValidationException("procesamiento", "Error al procesar con sistema externo: " + e.getMessage());
        }
    }

    private void notificarActualizacionAlPOS(Transaccion transaccion, String mensaje) {
        try {
            ActualizacionEstadoDTO actualizacion = new ActualizacionEstadoDTO();
            actualizacion.setCodigoUnicoTransaccion(transaccion.getCodigoUnicoTransaccion());
            actualizacion.setEstado(transaccion.getEstado());
            actualizacion.setMensaje(mensaje);

            posClient.actualizarEstadoTransaccion(actualizacion);
            log.info("Notificación enviada al POS exitosamente");
        } catch (feign.FeignException e) {
            log.error("Error de comunicación con POS: {}", e.getMessage());
            throw new TransaccionStateException(transaccion.getEstado(), 
                "No se pudo notificar al POS: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error al notificar al POS: {}", e.getMessage());
            throw new TransaccionValidationException("notificacion", 
                "Error al notificar actualización al POS: " + e.getMessage());
        }
    }

    private ValidacionTransaccionDTO prepararValidacionDTO(Transaccion transaccion) {
        try {
            ValidacionTransaccionDTO dto = transaccionMapper.toDTO(transaccion);
            DatosTarjeta datosTarjeta = extraerDatosTarjeta(transaccion.getTarjeta());

            // Configurar datos del banco
            BancoDTO banco = new BancoDTO();
            banco.setCodigo(1);
            dto.setBanco(banco);

            // Configurar datos de tarjeta
            dto.setFechaExpiracionTarjeta(datosTarjeta.getExpiryDate());
            dto.setNombreTarjeta(datosTarjeta.getNombreTarjeta());
            dto.setNumeroTarjeta(datosTarjeta.getCardNumber());
            dto.setDireccionTarjeta(datosTarjeta.getDireccionTarjeta());
            dto.setCvv(datosTarjeta.getCvv());

            // Configurar valores por defecto
            dto.setCodigoMoneda("USD");
            dto.setPais("EC");
            dto.setNumeroCuenta("00000003");
            dto.setGtwComision("10.50");
            dto.setGtwCuenta("00000002");

            return dto;
        } catch (Exception e) {
            log.error("Error al preparar DTO de validación: {}", e.getMessage());
            throw new TransaccionValidationException("preparacion_dto", 
                "Error al preparar datos para validación: " + e.getMessage());
        }
    }

    private DatosTarjeta extraerDatosTarjeta(String jsonTarjeta) {
        try {
            return objectMapper.readValue(jsonTarjeta, DatosTarjeta.class);
        } catch (Exception e) {
            log.error("Error al procesar datos de tarjeta: {}", e.getMessage());
            throw new TransaccionValidationException("tarjeta", 
                "Error al procesar datos de tarjeta: " + e.getMessage());
        }
    }

    private static class DatosTarjeta {
        private String cardNumber;
        private String expiryDate;
        private String cvv;
        private String nombreTarjeta;
        private String direccionTarjeta;

        // Getters
        public String getCardNumber() { return cardNumber; }
        public String getExpiryDate() { return expiryDate; }
        public String getCvv() { return cvv; }
        public String getNombreTarjeta() { return nombreTarjeta; }
        public String getDireccionTarjeta() { return direccionTarjeta; }
    }
}
