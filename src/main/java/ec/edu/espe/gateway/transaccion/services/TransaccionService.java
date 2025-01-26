package ec.edu.espe.gateway.transaccion.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.controller.dto.ValidacionTransaccionDTO;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.comercio.repository.PosComercioRepository;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.transaccion.client.ValidacionTransaccionClient;
import ec.edu.espe.gateway.transaccion.controller.dto.DatosTarjetaDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.RespuestaValidacionDTO;
import ec.edu.espe.gateway.transaccion.client.PosClient;
import ec.edu.espe.gateway.transaccion.controller.dto.ActualizacionEstadoDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import feign.FeignException;

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

    public TransaccionService(TransaccionRepository transaccionRepository,
            ComercioRepository comercioRepository,
            PosComercioRepository posComercioRepository,
            FacturacionComercioRepository facturacionComercioRepository,
            ValidacionTransaccionClient validacionTransaccionClient,
            PosClient posClient,
            ObjectMapper objectMapper) {
        this.transaccionRepository = transaccionRepository;
        this.comercioRepository = comercioRepository;
        this.posComercioRepository = posComercioRepository;
        this.facturacionComercioRepository = facturacionComercioRepository;
        this.validacionTransaccionClient = validacionTransaccionClient;
        this.posClient = posClient;
        this.objectMapper = objectMapper;
    }

    public Transaccion crearTransaccionPOS(Transaccion transaccion, String codigoPos) {
        try {
            LocalDateTime fechaActual = LocalDateTime.now();

            if (transaccion.getFecha() != null && transaccion.getFecha().isAfter(fechaActual)) {
                throw new IllegalArgumentException("La fecha de la transacción no puede ser futura");
            }

            // Validar POS
            PosComercio pos = posComercioRepository.findById(new PosComercioPK(codigoPos, "POS"))
                    .orElseThrow(() -> new EntityNotFoundException("POS no encontrado"));
            if (!"ACT".equals(pos.getEstado())) {
                throw new IllegalStateException("El POS debe estar activo para crear transacciones");
            }

            // Validar comercio
            Comercio comercio = pos.getComercio();
            if (!"ACT".equals(comercio.getEstado())) {
                throw new IllegalStateException("El comercio debe estar activo para crear transacciones");
            }

            // Obtener facturación activa
            FacturacionComercio facturacionActiva = facturacionComercioRepository
                    .findByComercioAndEstado(comercio, "ACT")
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No existe facturación activa para el comercio"));

            // Configurar campos inmutables
            transaccion.setComercio(comercio);
            transaccion.setFacturacionComercio(facturacionActiva);
            transaccion.setFecha(fechaActual);
            transaccion.setEstado(ESTADO_ENVIADO);
            transaccion.setCodigoUnicoTransaccion(generarCodigoUnico());

            validarTransaccion(transaccion);
            return transaccionRepository.save(transaccion);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear transacción: " + e.getMessage());
        }
    }

    private void validarTransaccion(Transaccion transaccion) {
        // Validar tipo
        if (!"SIM".equals(transaccion.getTipo()) && !"REC".equals(transaccion.getTipo())) {
            throw new IllegalArgumentException("Tipo de transacción inválido");
        }

        // Validar marca
        if (transaccion.getMarca() == null || transaccion.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca es requerida");
        }

        // Validar monto
        if (transaccion.getMonto() == null || transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        // Validar fecha
        LocalDateTime fechaActual = LocalDateTime.now();
        if (transaccion.getFecha() != null && transaccion.getFecha().isAfter(fechaActual)) {
            throw new IllegalArgumentException("La fecha de la transacción no puede ser futura");
        }

        // Validar fechas para transacciones recurrentes
        if ("REC".equals(transaccion.getTipo())) {
            validarFechasRecurrencia(transaccion);
        }
    }

    private void validarFechasRecurrencia(Transaccion transaccion) {
        LocalDate fechaActual = LocalDate.now();

        if (transaccion.getFechaEjecucionRecurrencia() == null) {
            throw new IllegalArgumentException("La fecha de ejecución es requerida para transacciones recurrentes");
        }
        if (transaccion.getFechaFinRecurrencia() == null) {
            throw new IllegalArgumentException("La fecha de fin es requerida para transacciones recurrentes");
        }
        if (transaccion.getFechaEjecucionRecurrencia().isBefore(fechaActual)) {
            throw new IllegalArgumentException("La fecha de ejecución no puede ser anterior a la fecha actual");
        }
        if (transaccion.getFechaFinRecurrencia().isBefore(transaccion.getFechaEjecucionRecurrencia())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de ejecución");
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
                    .orElseThrow(() -> new EntityNotFoundException("Transacción no encontrada"));

            validarCambioEstado(transaccion.getEstado(), nuevoEstado);
            transaccion.setEstado(nuevoEstado);
            transaccionRepository.save(transaccion);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar estado: " + e.getMessage());
        }
    }

    private void validarCambioEstado(String estadoActual, String nuevoEstado) {
        if (ESTADO_AUTORIZADO.equals(estadoActual) && !ESTADO_REVERSADO.equals(nuevoEstado)) {
            throw new IllegalStateException("Una transacción autorizada solo puede ser reversada");
        }
        if (ESTADO_RECHAZADO.equals(estadoActual) || ESTADO_REVERSADO.equals(estadoActual)) {
            throw new IllegalStateException("No se puede cambiar el estado de una transacción rechazada o reversada");
        }
    }

    public void detenerTransaccionesRecurrentes(Integer codigoComercio) {
        try {
            // Validar estado del comercio
            Comercio comercio = comercioRepository.findById(codigoComercio)
                    .orElseThrow(() -> new EntityNotFoundException("Comercio no encontrado"));

            if (!"INA".equals(comercio.getEstado()) && !"SUS".equals(comercio.getEstado())) {
                throw new IllegalStateException(
                        "Solo se pueden detener recurrencias de comercios inactivos o suspendidos");
            }

            List<Transaccion> transaccionesRecurrentes = transaccionRepository
                    .findActiveRecurrentTransactionsByComercio(codigoComercio);

            LocalDate fechaDetencion = LocalDate.now();
            for (Transaccion transaccion : transaccionesRecurrentes) {
                transaccion.setEstado(ESTADO_RECHAZADO);
                transaccion.setFechaFinRecurrencia(fechaDetencion); // Actualizar fecha fin
                transaccionRepository.save(transaccion);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al detener transacciones recurrentes: " + e.getMessage());
        }
    }

    public void procesarCambioEstadoComercio(Integer codigoComercio, String nuevoEstado) {
        if ("INA".equals(nuevoEstado) || "SUS".equals(nuevoEstado)) {
            Comercio comercio = comercioRepository.findById(codigoComercio)
                    .orElseThrow(() -> new EntityNotFoundException("Comercio no encontrado"));

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
                    log.error("Error en procesamiento asíncrono: {}", e.getMessage());
                }
            }).start();

        } catch (EntityNotFoundException e) {
            log.error("Error al procesar transacción POS: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al procesar transacción POS: {}", e.getMessage());
            throw new RuntimeException("Error al procesar transacción", e);
        }
    }

    @Transactional
    public Transaccion guardarTransaccionInicial(Transaccion transaccion) throws EntityNotFoundException {
        // Validar y obtener comercio
        Comercio comercio = comercioRepository.findById(transaccion.getComercio().getCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Comercio no encontrado"));
        log.info("Comercio encontrado: {}", comercio);

        // Validar y obtener facturación
        FacturacionComercio facturacion = facturacionComercioRepository
                .findById(transaccion.getFacturacionComercio().getCodigo())
                .orElseThrow(() -> new EntityNotFoundException("Facturación no encontrada"));
        log.info("Facturación encontrada: {}", facturacion);

        // Establecer relaciones y estado inicial
        transaccion.setComercio(comercio);
        transaccion.setFacturacionComercio(facturacion);
        transaccion.setEstado(ESTADO_ENVIADO);

        // Guardar transacción inmediatamente
        return transaccionRepository.save(transaccion);
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
                log.error("Error al serializar DTO a JSON: {}", e.getMessage());
            }

            try {
                ResponseEntity<RespuestaValidacionDTO> respuesta = validacionTransaccionClient
                        .validarTransaccion(validacionDTO);
                log.info("Respuesta del sistema externo - Status: {}, Body: {}",
                        respuesta.getStatusCode(), respuesta.getBody());

                // Si el código es 202, esperar a recibir una respuesta con 201 o 400
                if (respuesta.getStatusCode().value() == 202) {
                    log.info("Transacción en proceso de validación, esperando confirmación...");

                    while (respuesta.getStatusCode().value() == 202) {
                        // Espera de 5 segundos entre verificaciones (ajustar según sea necesario)
                        Thread.sleep(5000);
                        respuesta = validacionTransaccionClient.validarTransaccion(validacionDTO);
                    }

                    // Después de recibir una respuesta con código 201 o 400, actualizamos el estado
                    if (respuesta.getStatusCode().value() == 201) {
                        transaccion.setEstado(ESTADO_AUTORIZADO);
                        String mensaje = respuesta.getBody() != null ? respuesta.getBody().getMensaje()
                                : "Transacción autorizada";
                        log.info("Transacción autorizada por sistema externo");
                    } else if (respuesta.getStatusCode().value() == 400) {
                        transaccion.setEstado(ESTADO_RECHAZADO);
                        String mensaje = respuesta.getBody() != null ? respuesta.getBody().getMensaje()
                                : "Transacción rechazada";
                        log.info("Transacción rechazada por sistema externo");
                    }
                } else if (respuesta.getStatusCode().value() == 201) {
                    transaccion.setEstado(ESTADO_AUTORIZADO);
                    String mensaje = respuesta.getBody() != null ? respuesta.getBody().getMensaje()
                            : "Transacción autorizada";
                    log.info("Transacción autorizada por sistema externo");
                } else {
                    transaccion.setEstado(ESTADO_RECHAZADO);
                    String mensaje = respuesta.getBody() != null ? respuesta.getBody().getMensaje()
                            : "Transacción rechazada";
                    log.info("Transacción rechazada por sistema externo");
                }

                // Guardar la actualización del estado
                transaccion = transaccionRepository.save(transaccion);
                log.info("Estado de transacción actualizado en gateway a: {}",
                        transaccion.getEstado());

                // Notificar al POS
                notificarActualizacionAlPOS(transaccion, "Estado actualizado a: " + transaccion.getEstado());

            } catch (feign.FeignException.BadRequest e) {
                // Manejar específicamente el error 400
                log.info("Transacción rechazada por el sistema externo con código 400");
                transaccion.setEstado(ESTADO_RECHAZADO);
                transaccion = transaccionRepository.save(transaccion);
                notificarActualizacionAlPOS(transaccion, "Transacción rechazada");
            } catch (Exception e) {
                log.error("Error en validación externa: {}. Marcando transacción como rechazada", e.getMessage());
                transaccion.setEstado(ESTADO_RECHAZADO);
                transaccion = transaccionRepository.save(transaccion);
                notificarActualizacionAlPOS(transaccion, "Error en validación: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("Error al preparar validación: {}", e.getMessage());
            transaccion.setEstado(ESTADO_RECHAZADO);
            transaccion = transaccionRepository.save(transaccion);
            notificarActualizacionAlPOS(transaccion, "Error al preparar validación: " + e.getMessage());
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
        } catch (Exception e) {
            log.error("Error al notificar al POS: {}", e.getMessage());
        }
    }

    private ValidacionTransaccionDTO prepararValidacionDTO(Transaccion transaccion) {
        ValidacionTransaccionDTO dto = new ValidacionTransaccionDTO();
        DatosTarjetaDTO datosTarjeta = extraerDatosTarjeta(transaccion.getTarjeta());

        dto.setCodigoBanco(1);

        // Configurar datos de la transacción
        dto.setMonto(transaccion.getMonto().doubleValue());
        dto.setModalidad(transaccion.getTipo());
        dto.setCodigoMoneda("USD");
        dto.setMarca(transaccion.getMarca());
        dto.setFechaExpiracionTarjeta(datosTarjeta.getExpiryDate());
        dto.setNombreTarjeta(datosTarjeta.getNombreTarjeta());
        dto.setNumeroTarjeta(datosTarjeta.getCardNumber());
        dto.setDireccionTarjeta(datosTarjeta.getDireccionTarjeta());
        dto.setCvv(datosTarjeta.getCvv());
        dto.setPais("EC"); // Valor quemado
        dto.setNumeroCuenta("00000003"); // Valor quemado
        dto.setCodigoUnicoTransaccion(transaccion.getCodigoUnicoTransaccion());
        dto.setGtwComision("0"); // Valor quemado
        dto.setGtwCuenta("00000002"); // Valor quemado

        // Configurar datos de diferido
        dto.setInteresDiferido(transaccion.getInteresDiferido());
        dto.setCuotas(transaccion.getCuotas());

        return dto;
    }

    private DatosTarjetaDTO extraerDatosTarjeta(String jsonTarjeta) {
        try {
            return objectMapper.readValue(jsonTarjeta, DatosTarjetaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar datos de tarjeta", e);
        }
    }

}