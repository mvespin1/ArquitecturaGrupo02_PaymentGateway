package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadProcesador;
import ec.edu.espe.gateway.seguridad.services.SeguridadProcesadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/seguridad-procesadores")
public class SeguridadProcesadorController {

    private final SeguridadProcesadorService procesadorService;

    public SeguridadProcesadorController(SeguridadProcesadorService procesadorService) {
        this.procesadorService = procesadorService;
    }

    @GetMapping
    public ResponseEntity<List<SeguridadProcesador>> getAll() {
        List<SeguridadProcesador> procesadores = procesadorService.getAllProcesadores();
        return ResponseEntity.ok(procesadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguridadProcesador> getById(@PathVariable Integer id) {
        try {
            SeguridadProcesador procesador = procesadorService.getProcesadorById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Procesador con ID " + id + " no encontrado."));
            return ResponseEntity.ok(procesador);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SeguridadProcesador> create(@RequestBody SeguridadProcesador procesador) {
        SeguridadProcesador savedProcesador = procesadorService.createProcesador(procesador);
        return ResponseEntity.ok(savedProcesador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeguridadProcesador> update(
            @PathVariable Integer id,
            @RequestBody SeguridadProcesador updatedProcesadorDetails) {
        try {
            SeguridadProcesador updatedProcesador = procesadorService.updateProcesador(id, updatedProcesadorDetails);
            return ResponseEntity.ok(updatedProcesador);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SeguridadProcesador> deactivate(@PathVariable Integer id) {
        try {
            SeguridadProcesador deactivatedProcesador = procesadorService.deactivateProcesador(id);
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