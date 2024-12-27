package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.GtwSeguridadProcesador;
import ec.edu.espe.gateway.seguridad.services.GtwSeguridadProcesadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/seguridad-procesadores")
public class GtwSeguridadProcesadorController {

    private final GtwSeguridadProcesadorService procesadorService;

    public GtwSeguridadProcesadorController(GtwSeguridadProcesadorService procesadorService) {
        this.procesadorService = procesadorService;
    }

    @GetMapping
    public ResponseEntity<List<GtwSeguridadProcesador>> getAll() {
        List<GtwSeguridadProcesador> procesadores = procesadorService.getAllProcesadores();
        return ResponseEntity.ok(procesadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GtwSeguridadProcesador> getById(@PathVariable Integer id) {
        try {
            GtwSeguridadProcesador procesador = procesadorService.getProcesadorById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Procesador con ID " + id + " no encontrado."));
            return ResponseEntity.ok(procesador);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GtwSeguridadProcesador> create(@RequestBody GtwSeguridadProcesador procesador) {
        GtwSeguridadProcesador savedProcesador = procesadorService.createProcesador(procesador);
        return ResponseEntity.ok(savedProcesador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GtwSeguridadProcesador> update(
            @PathVariable Integer id,
            @RequestBody GtwSeguridadProcesador updatedProcesadorDetails) {
        try {
            GtwSeguridadProcesador updatedProcesador = procesadorService.updateProcesador(id, updatedProcesadorDetails);
            return ResponseEntity.ok(updatedProcesador);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<GtwSeguridadProcesador> deactivate(@PathVariable Integer id) {
        try {
            GtwSeguridadProcesador deactivatedProcesador = procesadorService.deactivateProcesador(id);
            return ResponseEntity.ok(deactivatedProcesador);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            procesadorService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}