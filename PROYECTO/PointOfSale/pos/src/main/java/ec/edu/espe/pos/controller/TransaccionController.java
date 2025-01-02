package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.Transaccion;
import ec.edu.espe.pos.service.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public ResponseEntity<Object> listarTodas() {
        try {
            return ResponseEntity.ok(this.transaccionService.obtenerTodas());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener las transacciones: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.transaccionService.obtenerPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<Object> obtenerPorEstado(@PathVariable String estado) {
        try {
            return ResponseEntity.ok(this.transaccionService.obtenerPorEstado(estado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener transacciones por estado: " + e.getMessage());
        }
    }

    @GetMapping("/tipo-estado")
    public ResponseEntity<Object> obtenerPorTipoYEstado(
            @RequestParam String tipo,
            @RequestParam String estado) {
        try {
            return ResponseEntity.ok(this.transaccionService.obtenerPorTipoYEstado(tipo, estado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al obtener transacciones por tipo y estado: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody Transaccion transaccion) {
        try {
            return ResponseEntity.ok(this.transaccionService.crear(transaccion));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear la transacción: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Object> actualizarEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        try {
            return ResponseEntity.ok(this.transaccionService.actualizarEstado(id, nuevoEstado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado-recibo")
    public ResponseEntity<Object> actualizarEstadoRecibo(
            @PathVariable Integer id,
            @RequestParam String nuevoEstadoRecibo) {
        try {
            return ResponseEntity.ok(this.transaccionService.actualizarEstadoRecibo(id, nuevoEstadoRecibo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar el estado del recibo: " + e.getMessage());
        }
    }
} 