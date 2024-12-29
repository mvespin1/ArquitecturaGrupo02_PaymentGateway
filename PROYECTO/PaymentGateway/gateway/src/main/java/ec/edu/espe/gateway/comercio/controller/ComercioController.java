package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.services.ComercioService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    private final ComercioService comercioService;

    public ComercioController(ComercioService comercioService) {
        this.comercioService = comercioService;
    }

    @GetMapping
    public ResponseEntity<List<Comercio>> getAll() {
        List<Comercio> comercios = comercioService.findAll();
        return ResponseEntity.ok(comercios);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Comercio> getById(@PathVariable int codigo) {
        return comercioService.findById(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comercio> create(@RequestBody Comercio comercio) {
        Comercio savedComercio = comercioService.save(comercio);
        return ResponseEntity.ok(savedComercio);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Comercio> update(@PathVariable int codigo, @RequestBody Comercio comercio) {
        try {
            Comercio updatedComercio = comercioService.update(codigo, comercio);
            return ResponseEntity.ok(updatedComercio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable int codigo) {
        try {
            comercioService.deleteById(codigo);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
