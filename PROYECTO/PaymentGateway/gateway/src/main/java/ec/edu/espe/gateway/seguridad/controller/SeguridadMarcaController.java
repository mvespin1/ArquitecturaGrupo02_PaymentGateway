package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadMarca;
import ec.edu.espe.gateway.seguridad.services.SeguridadMarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/seguridad-marcas")
public class SeguridadMarcaController {

    private final SeguridadMarcaService marcaService;

    public SeguridadMarcaController(SeguridadMarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public ResponseEntity<List<SeguridadMarca>> getAll() {
        List<SeguridadMarca> marcas = marcaService.getAllMarcas();
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/{marca}")
    public ResponseEntity<SeguridadMarca> getById(@PathVariable String marca) {
        try {
            SeguridadMarca foundMarca = marcaService.getMarcaById(marca)
                    .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + marca + " no encontrada."));
            return ResponseEntity.ok(foundMarca);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SeguridadMarca> create(@RequestBody SeguridadMarca marca) {
        SeguridadMarca savedMarca = marcaService.createMarca(marca);
        return ResponseEntity.ok(savedMarca);
    }

    @PutMapping("/{marca}")
    public ResponseEntity<SeguridadMarca> update(
            @PathVariable String marca,
            @RequestBody SeguridadMarca updatedMarcaDetails) {
        try {
            SeguridadMarca updatedMarca = marcaService.updateMarca(marca, updatedMarcaDetails);
            return ResponseEntity.ok(updatedMarca);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{marca}")
    public ResponseEntity<Void> delete(@PathVariable String marca) {
        try {
            marcaService.deleteById(marca);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}