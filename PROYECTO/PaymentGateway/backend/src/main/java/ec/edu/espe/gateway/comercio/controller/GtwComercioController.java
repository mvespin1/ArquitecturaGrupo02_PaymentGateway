package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.gateway.comercio.model.GtwComercio;
import ec.edu.espe.gateway.comercio.services.GtwComercioService;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/comercios")
public class GtwComercioController {

    private final GtwComercioService gtwComercioService;

    public GtwComercioController(GtwComercioService gtwComercioService) {
        this.gtwComercioService = gtwComercioService;
    }

    @GetMapping
    public ResponseEntity<List<GtwComercio>> getAll() {
        List<GtwComercio> comercios = gtwComercioService.findAll();
        return ResponseEntity.ok(comercios);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<GtwComercio> getById(@PathVariable int codigo) {
        return gtwComercioService.findById(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GtwComercio> create(@RequestBody GtwComercio gtwComercio) {
        GtwComercio savedComercio = gtwComercioService.save(gtwComercio);
        return ResponseEntity.ok(savedComercio);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<GtwComercio> update(@PathVariable int codigo, @RequestBody GtwComercio gtwComercio) {
        try {
            GtwComercio updatedComercio = gtwComercioService.update(codigo, gtwComercio);
            return ResponseEntity.ok(updatedComercio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable int codigo) {
        try {
            gtwComercioService.deleteById(codigo);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
