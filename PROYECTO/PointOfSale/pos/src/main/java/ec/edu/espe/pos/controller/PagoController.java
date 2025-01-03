package ec.edu.espe.pos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.pos.dto.SecureCardDataDTO;
import ec.edu.espe.pos.service.TransaccionService;
import ec.edu.espe.pos.model.Transaccion;
import java.time.LocalDate;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final TransaccionService transaccionService;

    public PagoController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/procesar")
    public ResponseEntity<Object> procesarPago(@RequestBody SecureCardDataDTO datos) {
        try {
            // Crear objeto Transaccion con los datos recibidos
            Transaccion transaccion = new Transaccion();
            transaccion.setMonto(datos.getMonto());
            transaccion.setMarca(datos.getMarca());
            
            Transaccion transaccionProcesada = transaccionService.crear(transaccion);
            
            return ResponseEntity.ok(transaccionProcesada);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar el pago: " + e.getMessage());
        }
    }
}
