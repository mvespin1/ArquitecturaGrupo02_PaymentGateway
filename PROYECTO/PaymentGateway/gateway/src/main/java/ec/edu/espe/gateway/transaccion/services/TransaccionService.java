package ec.edu.espe.gateway.transaccion.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.model.ValidacionTransaccionDTO;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.comercio.repository.PosComercioRepository;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.transaccion.client.ValidacionTransaccionClient;
import ec.edu.espe.gateway.transaccion.model.RespuestaValidacionDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

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
    private final ObjectMapper objectMapper;

    public TransaccionService(TransaccionRepository transaccionRepository,
            ComercioRepository comercioRepository,
            PosComercioRepository posComercioRepository,
            FacturacionComercioRepository facturacionComercioRepository,
            ValidacionTransaccionClient validacionTransaccionClient,
            ObjectMapper objectMapper) {
        this.transaccionRepository = transaccionRepository;
        this.comercioRepository = comercioRepository;
        this.posComercioRepository = posComercioRepository;
        this.facturacionComercioRepository = facturacionComercioRepository;
        this.validacionTransaccionClient = validacionTransaccionClient;
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

            // Guardar transacción inmediatamente con estado ENVIADO
            Transaccion transaccionGuardada = transaccionRepository.save(transaccion);
            log.info("Transacción guardada inicialmente en el gateway con ID: {} y estado: {}", 
                    transaccionGuardada.getCodigo(), transaccionGuardada.getEstado());

            // Intentar validación con sistema externo
            try {
                ValidacionTransaccionDTO validacionDTO = prepararValidacionDTO(transaccionGuardada);
                
                // Loguear el JSON que se enviará
                try {
                    String jsonRequest = objectMapper.writeValueAsString(validacionDTO);
                    log.info("JSON a enviar al sistema externo: {}", jsonRequest);
                } catch (Exception e) {
                    log.error("Error al serializar DTO a JSON: {}", e.getMessage());
                }
                
                try {
                    RespuestaValidacionDTO respuesta = validacionTransaccionClient.validarTransaccion(validacionDTO);
                    log.info("Respuesta del sistema externo: {}", respuesta);
                    
                    // Actualizar estado basado en el código HTTP
                    if (respuesta != null) {
                        if (respuesta.getCodigoRespuesta() == 200) {
                            transaccionGuardada.setEstado(ESTADO_AUTORIZADO);
                            log.info("Transacción autorizada por sistema externo");
                        } else if (respuesta.getCodigoRespuesta() == 405) {
                            transaccionGuardada.setEstado(ESTADO_RECHAZADO);
                            log.info("Transacción rechazada por sistema externo");
                        }
                        // Guardar la actualización del estado
                        transaccionGuardada = transaccionRepository.save(transaccionGuardada);
                        log.info("Estado de transacción actualizado en gateway a: {}", 
                                transaccionGuardada.getEstado());
                    }
                } catch (feign.FeignException.MethodNotAllowed e) {
                    // Manejar específicamente el error 405
                    log.info("Transacción rechazada por el sistema externo con código 405");
                    transaccionGuardada.setEstado(ESTADO_RECHAZADO);
                    transaccionGuardada = transaccionRepository.save(transaccionGuardada);
                } catch (Exception e) {
                    log.error("Error en validación externa: {}. Marcando transacción como rechazada", e.getMessage());
                    transaccionGuardada.setEstado(ESTADO_RECHAZADO);
                    transaccionGuardada = transaccionRepository.save(transaccionGuardada);
                }
            } catch (Exception e) {
                log.error("Error al preparar validación: {}", e.getMessage());
                transaccionGuardada.setEstado(ESTADO_RECHAZADO);
                transaccionGuardada = transaccionRepository.save(transaccionGuardada);
            }

        } catch (EntityNotFoundException e) {
            log.error("Error al procesar transacción POS: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al procesar transacción POS: {}", e.getMessage());
            throw new RuntimeException("Error al procesar transacción", e);
        }
    }

    private ValidacionTransaccionDTO prepararValidacionDTO(Transaccion transaccion) {
        ValidacionTransaccionDTO dto = new ValidacionTransaccionDTO();
        DatosTarjeta datosTarjeta = extraerDatosTarjeta(transaccion.getTarjeta());

        // Configurar datos del banco
        ValidacionTransaccionDTO.Banco banco = new ValidacionTransaccionDTO.Banco();
        banco.setCodigo(1); // Valor quemado
        dto.setBanco(banco);

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
        dto.setGtwComision("10.50"); // Valor quemado
        dto.setGtwCuenta("00000002"); // Valor quemado

        // Configurar datos de diferido
        dto.setInteresDiferido(transaccion.getInteresDiferido());
        dto.setCuotas(transaccion.getCuotas());

        return dto;
    }

    private DatosTarjeta extraerDatosTarjeta(String jsonTarjeta) {
        try {
            return objectMapper.readValue(jsonTarjeta, DatosTarjeta.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar datos de tarjeta", e);
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
