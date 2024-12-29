package ec.edu.espe.gateway.transaccion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.services.TransaccionService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;


@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaccion>> getAll() {
        List<Transaccion> transacciones = transaccionService.findAll();
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaccion> getById(@PathVariable Integer id) {
        return transaccionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Transaccion transaccion) {
        try {
            Transaccion savedTransaccion = transaccionService.create(transaccion);
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
            Transaccion updatedTransaccion = transaccionService.updateEstado(id, nuevoEstado);
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
    public ResponseEntity<List<Transaccion>> findByEstado(@PathVariable String estado) {
        try {
            List<Transaccion> transacciones = transaccionService.findByEstado(estado);
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
