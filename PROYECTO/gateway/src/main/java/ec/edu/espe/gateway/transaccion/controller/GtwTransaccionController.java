package ec.edu.espe.gateway.transaccion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.transaccion.model.GtwTransaccion;
import ec.edu.espe.gateway.transaccion.services.GtwTransaccionService;
import jakarta.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/api/transacciones")
public class GtwTransaccionController {

    private final GtwTransaccionService transaccionService;

    public GtwTransaccionController(GtwTransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<GtwTransaccion> updateEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        try {
            GtwTransaccion updatedTransaccion = transaccionService.updateEstado(id, nuevoEstado);
            return ResponseEntity.ok(updatedTransaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return ResponseEntity.status(405).build(); // Método no permitido
    }
}
