package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.GtwSeguridadMarca;
import ec.edu.espe.gateway.seguridad.services.GtwSeguridadMarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/seguridad-marcas")
public class GtwSeguridadMarcaController {

    private final GtwSeguridadMarcaService marcaService;

    public GtwSeguridadMarcaController(GtwSeguridadMarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public ResponseEntity<List<GtwSeguridadMarca>> getAll() {
        List<GtwSeguridadMarca> marcas = marcaService.getAllMarcas();
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/{marca}")
    public ResponseEntity<GtwSeguridadMarca> getById(@PathVariable String marca) {
        try {
            GtwSeguridadMarca foundMarca = marcaService.getMarcaById(marca)
                    .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + marca + " no encontrada."));
            return ResponseEntity.ok(foundMarca);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GtwSeguridadMarca> create(@RequestBody GtwSeguridadMarca marca) {
        GtwSeguridadMarca savedMarca = marcaService.createMarca(marca);
        return ResponseEntity.ok(savedMarca);
    }

    @PutMapping("/{marca}")
    public ResponseEntity<GtwSeguridadMarca> update(
            @PathVariable String marca,
            @RequestBody GtwSeguridadMarca updatedMarcaDetails) {
        try {
            GtwSeguridadMarca updatedMarca = marcaService.updateMarca(marca, updatedMarcaDetails);
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