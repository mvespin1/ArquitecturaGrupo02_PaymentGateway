package ec.edu.espe.gateway.transaccion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/process-payment")
public class PagoController {

    @PostMapping
    public ResponseEntity<String> procesarPago(@RequestBody Map<String, String> request) {
        try {
            String encryptedData = request.get("encryptedData");
            
            // Imprimir los datos recibidos
            System.out.println("========== DATOS RECIBIDOS EN PAYMENT GATEWAY ==========");
            System.out.println("Datos encriptados: " + encryptedData);
            System.out.println("====================================================");
            
            return ResponseEntity.ok("Datos recibidos correctamente");
            
        } catch (Exception e) {
            System.err.println("Error al procesar el pago: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error al procesar el pago");
        }
    }
}
