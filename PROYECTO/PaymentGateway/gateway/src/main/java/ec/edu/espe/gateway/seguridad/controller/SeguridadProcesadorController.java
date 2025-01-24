package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadProcesador;
import ec.edu.espe.gateway.seguridad.services.SeguridadProcesadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ec.edu.espe.gateway.seguridad.exception.NotFoundException;

import java.util.List;

@RestController
@RequestMapping("/v1/seguridad-procesadores")
public class SeguridadProcesadorController {

    private final SeguridadProcesadorService procesadorService;

    public SeguridadProcesadorController(SeguridadProcesadorService procesadorService) {
        this.procesadorService = procesadorService;
    }

    @GetMapping
    public ResponseEntity<List<SeguridadProcesador>> getAll() {
        try {
            List<SeguridadProcesador> procesadores = procesadorService.getAllProcesadores();
            return ResponseEntity.ok(procesadores);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguridadProcesador> getById(@PathVariable Integer id) {
        try {
            return procesadorService.getProcesadorById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException(id.toString(), "Procesador"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<SeguridadProcesador> create(@RequestBody SeguridadProcesador procesador) {
        try {
            SeguridadProcesador savedProcesador = procesadorService.createProcesador(procesador);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProcesador);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeguridadProcesador> update(
            @PathVariable Integer id,
            @RequestBody SeguridadProcesador updatedProcesadorDetails) {
        try {
            SeguridadProcesador updatedProcesador = procesadorService.updateProcesador(id, updatedProcesadorDetails);
            return ResponseEntity.ok(updatedProcesador);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SeguridadProcesador> deactivate(@PathVariable Integer id) {
        try {
            SeguridadProcesador deactivatedProcesador = procesadorService.deactivateProcesador(id);
            return ResponseEntity.ok(deactivatedProcesador);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            procesadorService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}