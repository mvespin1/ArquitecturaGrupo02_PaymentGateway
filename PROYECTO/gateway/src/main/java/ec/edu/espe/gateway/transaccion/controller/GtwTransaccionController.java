package ec.edu.espe.gateway.transaccion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.transaccion.model.GtwTransaccion;
import ec.edu.espe.gateway.transaccion.services.GtwTransaccionService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;


@RestController
@RequestMapping("/api/transacciones")
public class GtwTransaccionController {

    private final GtwTransaccionService transaccionService;

    public GtwTransaccionController(GtwTransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public ResponseEntity<List<GtwTransaccion>> getAll() {
        List<GtwTransaccion> transacciones = transaccionService.findAll();
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GtwTransaccion> getById(@PathVariable Integer id) {
        return transaccionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GtwTransaccion transaccion) {
        try {
            GtwTransaccion savedTransaccion = transaccionService.create(transaccion);
            return ResponseEntity.ok(savedTransaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        try {
            GtwTransaccion updatedTransaccion = transaccionService.updateEstado(id, nuevoEstado);
            return ResponseEntity.ok(updatedTransaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<GtwTransaccion>> findByEstado(@PathVariable String estado) {
        try {
            List<GtwTransaccion> transacciones = transaccionService.findByEstado(estado);
            return ResponseEntity.ok(transacciones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return ResponseEntity.status(405).build(); // Método no permitido
    }
}
