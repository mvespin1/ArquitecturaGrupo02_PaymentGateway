package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.PosTransaccion;
import ec.edu.espe.pos.service.PosTransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class PosTransaccionController {

    private final PosTransaccionService posTransaccionService;

    public PosTransaccionController(PosTransaccionService posTransaccionService) {
        this.posTransaccionService = posTransaccionService;
    }

    @GetMapping
    public ResponseEntity<List<PosTransaccion>> listarTodas() {
        return ResponseEntity.ok(this.posTransaccionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PosTransaccion> obtenerPorId(@PathVariable Integer id) {
        return this.posTransaccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PosTransaccion> crear(@RequestBody PosTransaccion posTransaccion) {
        return ResponseEntity.ok(this.posTransaccionService.crear(posTransaccion));
    }

    @PatchMapping("/{id}/estados")
    public ResponseEntity<PosTransaccion> actualizarEstados(
            @PathVariable Integer id,
            @RequestParam String estado,
            @RequestParam String estadoRecibo) {
        try {
            PosTransaccion actualizada = this.posTransaccionService.actualizarEstados(id, estado, estadoRecibo);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 