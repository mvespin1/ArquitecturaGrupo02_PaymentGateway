package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadMarca;
import ec.edu.espe.gateway.seguridad.services.SeguridadMarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ec.edu.espe.gateway.seguridad.exception.NotFoundException;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/v1/seguridad-marcas")
public class SeguridadMarcaController {

    private final SeguridadMarcaService marcaService;

    public SeguridadMarcaController(SeguridadMarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @GetMapping
    public ResponseEntity<List<SeguridadMarca>> getAll() {
        try {
            List<SeguridadMarca> marcas = marcaService.getAllMarcas();
            return ResponseEntity.ok(marcas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{marca}")
    public ResponseEntity<SeguridadMarca> getById(@PathVariable String marca) {
        try {
            return marcaService.getMarcaById(marca)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException(marca, "Marca"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<SeguridadMarca> create(@RequestBody SeguridadMarca marca) {
        try {
            SeguridadMarca savedMarca = marcaService.createMarca(marca);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMarca);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{marca}")
    public ResponseEntity<Void> delete(@PathVariable String marca) {
        try {
            marcaService.deleteById(marca);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }
}
