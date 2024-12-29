package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.Transaccion;
import ec.edu.espe.pos.service.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public ResponseEntity<List<Transaccion>> listarTodas() {
        return ResponseEntity.ok(this.transaccionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaccion> obtenerPorId(@PathVariable Integer id) {
        return this.transaccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transaccion> crear(@RequestBody Transaccion transaccion) {
        return ResponseEntity.ok(this.transaccionService.crear(transaccion));
    }

    @PatchMapping("/{id}/estados")
    public ResponseEntity<Transaccion> actualizarEstados(
            @PathVariable Integer id,
            @RequestParam String estado,
            @RequestParam String estadoRecibo) {
        try {
            Transaccion actualizada = this.transaccionService.actualizarEstados(id, estado, estadoRecibo);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 