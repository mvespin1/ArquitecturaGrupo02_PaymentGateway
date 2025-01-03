package ec.edu.espe.pos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import ec.edu.espe.pos.dto.SecureCardDataDTO;
import ec.edu.espe.pos.service.TransaccionService;
import ec.edu.espe.pos.model.Transaccion;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final TransaccionService transaccionService;
    private final RestTemplate restTemplate;
    private static final String PAYMENT_GATEWAY_URL = "http://localhost:8083"; // Verifica que este puerto coincida con
                                                                               // tu PaymentGateway

    public PagoController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/procesar")
    public ResponseEntity<Object> procesarPago(@RequestBody SecureCardDataDTO datos) {
        try {
            // Crear la transacción local inmediatamente
            Transaccion transaccion = new Transaccion();
            transaccion.setMonto(datos.getMonto());

            // Convertir el nombre de la marca a código
            String marcaCodigo = convertirMarcaACodigo(datos.getMarca());
            transaccion.setMarca(marcaCodigo);

            Transaccion transaccionProcesada = transaccionService.crear(transaccion);

            // Enviar datos al PaymentGateway de manera asíncrona
            CompletableFuture.runAsync(() -> {
                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    Map<String, String> paymentRequest = new HashMap<>();
                    paymentRequest.put("encryptedData", datos.getEncryptedData());

                    HttpEntity<Map<String, String>> request = new HttpEntity<>(paymentRequest, headers);

                    System.out.println("Intentando conectar con PaymentGateway en: " + PAYMENT_GATEWAY_URL);
                    ResponseEntity<String> response = restTemplate.postForEntity(
                            PAYMENT_GATEWAY_URL + "/api/process-payment",
                            request,
                            String.class);
                    System.out.println("Respuesta del PaymentGateway: " + response.getStatusCode());

                } catch (Exception e) {
                    System.err.println("Error al comunicarse con PaymentGateway: " + e.getMessage());
                }
            });

            return ResponseEntity.ok(transaccionProcesada);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar el pago: " + e.getMessage());
        }
    }

    private String convertirMarcaACodigo(String marca) {
        if (marca == null)
            return "OTHR";

        switch (marca.toUpperCase()) {
            case "MASTERCARD":
                return "MSCD";
            case "VISA":
                return "VISA";
            case "AMERICAN EXPRESS":
                return "AMEX";
            case "DINERS CLUB":
                return "DINE";
            default:
                return "OTHR";
        }
    }
}
