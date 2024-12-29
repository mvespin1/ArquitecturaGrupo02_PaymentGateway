package ec.edu.espe.gateway.comision.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.services.ComisionSegmentoService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/comision-segmentos")
public class ComisionSegmentoController {

    private final ComisionSegmentoService segmentoService;

    public ComisionSegmentoController(ComisionSegmentoService segmentoService) {
        this.segmentoService = segmentoService;
    }

    /**
     * Obtener todos los segmentos de comisión.
     */
    @GetMapping
    public ResponseEntity<List<ComisionSegmento>> getAll() {
        List<ComisionSegmento> segmentos = segmentoService.findAll();
        return ResponseEntity.ok(segmentos);
    }

    /**
     * Obtener un segmento de comisión por su clave primaria.
     */
    @GetMapping("/{comision}/{transaccionesDesde}")
    public ResponseEntity<ComisionSegmento> getById(
            @PathVariable Integer comision,
            @PathVariable Integer transaccionesDesde) {
        return segmentoService.findById(comision, transaccionesDesde)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crear un nuevo segmento de comisión.
     */
    @PostMapping
    public ResponseEntity<ComisionSegmento> create(@RequestBody ComisionSegmento segmento) {
        try {
            ComisionSegmento savedSegmento = segmentoService.save(segmento);
            return ResponseEntity.ok(savedSegmento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualizar los campos TRANSACCIONES_HASTA y MONTO de un segmento.
     */
    @PutMapping("/{comision}/{transaccionesDesde}")
    public ResponseEntity<ComisionSegmento> update(
            @PathVariable Integer comision,
            @PathVariable Integer transaccionesDesde,
            @RequestParam Integer transaccionesHasta,
            @RequestParam BigDecimal monto) {
        try {
            ComisionSegmento updatedSegmento = segmentoService.update(
                comision, transaccionesDesde, transaccionesHasta, monto);
            return ResponseEntity.ok(updatedSegmento);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar un segmento de comisión.
     * Solo permitido si MONTO == 0.
     */
    @DeleteMapping("/{comision}/{transaccionesDesde}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer comision,
            @PathVariable Integer transaccionesDesde) {
        try {
            segmentoService.deleteById(comision, transaccionesDesde);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}