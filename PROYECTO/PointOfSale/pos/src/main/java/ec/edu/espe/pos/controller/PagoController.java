package ec.edu.espe.pos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.pos.service.TransaccionService;
import ec.edu.espe.pos.model.Transaccion;
import java.math.BigDecimal;
import java.util.Map;

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
            
            // Manejar el campo interesDiferido
            Boolean interesDiferido = Boolean.valueOf(String.valueOf(payload.get("interesDiferido")));
            
            // Manejar el campo cuotas de manera segura
            Integer cuotas = null;
            if (interesDiferido && payload.get("cuotas") != null) {
                try {
                    cuotas = Integer.valueOf(String.valueOf(payload.get("cuotas")));
                } catch (NumberFormatException e) {
                    log.warn("Error al convertir cuotas a número: {}", e.getMessage());
                }
            }
            
            log.info("Datos sensibles recibidos (encriptados)");
            log.info("Interés diferido: {}", interesDiferido);
            log.info("Cuotas: {}", cuotas);
            
            // El resto de valores se establecen en el servicio
            Transaccion transaccionProcesada = transaccionService.crear(transaccion, datosSensibles);
            log.info("Transacción procesada exitosamente: {}", transaccionProcesada);
            
            return ResponseEntity.ok("Transacción procesada exitosamente con ID: " + 
                                   transaccionProcesada.getCodigoUnicoTransaccion());
        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al procesar pago: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Error al procesar el pago: " + e.getMessage());
        }
    }
}
