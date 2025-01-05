package ec.edu.espe.pos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.pos.service.TransaccionService;
import ec.edu.espe.pos.model.Transaccion;
import ec.edu.espe.pos.dto.Comercio;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final TransaccionService transaccionService;
    public PagoController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/procesar")
    public ResponseEntity<Object> procesarPago(@RequestBody Transaccion datos) {
        try {
            if (datos.getComercio() == null) {
                datos.setComercio(new Comercio(1));
            }
            
            Transaccion transaccionProcesada = transaccionService.crear(datos);
            return ResponseEntity.ok(transaccionProcesada);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar el pago: " + e.getMessage());
        }
    }
}
