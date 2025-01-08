package ec.edu.espe.pos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.pos.service.TransaccionService;
import ec.edu.espe.pos.model.Transaccion;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    private static final Logger log = LoggerFactory.getLogger(PagoController.class);
    private final TransaccionService transaccionService;
    
    public PagoController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/procesar")
    public ResponseEntity<Object> procesarPago(@RequestBody Map<String, Object> payload) {
        log.info("Recibiendo petición de pago desde frontend");
        try {
            // Crear objeto transacción con datos básicos
            Transaccion transaccion = new Transaccion();
            transaccion.setMonto(new BigDecimal(payload.get("monto").toString()));
            transaccion.setMarca(payload.get("marca").toString());
            
            // Obtener datos sensibles encriptados
            String datosSensibles = payload.get("datosTarjeta").toString();
            
            // Obtener datos de diferido
            Boolean interesDiferido = payload.get("interesDiferido") != null ? 
                                    (Boolean) payload.get("interesDiferido") : false;
            Integer cuotas = interesDiferido && payload.get("cuotas") != null ? 
                           Integer.valueOf(payload.get("cuotas").toString()) : null;
            
            log.info("Datos de diferido - Interés: {}, Cuotas: {}", interesDiferido, cuotas);
            
            // El resto de valores se establecen en el servicio
            Transaccion transaccionProcesada = transaccionService.crear(transaccion, datosSensibles, 
                                                                       interesDiferido, cuotas);
            log.info("Transacción procesada: {}", transaccionProcesada);
            
            Map<String, Object> response = new HashMap<>();
            // Determinar el mensaje basado en el estado
            if (TransaccionService.ESTADO_AUTORIZADO.equals(transaccionProcesada.getEstado())) {
                response.put("mensaje", "Transacción autorizada");
                response.put("estado", "success");
            } else if (TransaccionService.ESTADO_RECHAZADO.equals(transaccionProcesada.getEstado())) {
                response.put("mensaje", "Transacción rechazada");
                response.put("estado", "error");
            } else {
                response.put("mensaje", "Transacción en proceso");
                response.put("estado", "pending");
            }
            response.put("transaccion", transaccionProcesada);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage());
            errorResponse.put("estado", "error");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            log.error("Error inesperado al procesar pago: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al procesar el pago: " + e.getMessage());
            errorResponse.put("estado", "error");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
