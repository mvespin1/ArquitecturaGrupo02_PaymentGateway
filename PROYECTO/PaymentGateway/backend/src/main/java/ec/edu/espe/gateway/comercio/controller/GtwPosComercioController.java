package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.gateway.comercio.model.GtwPosComercio;
import ec.edu.espe.gateway.comercio.model.GtwPosComercioPK;
import ec.edu.espe.gateway.comercio.services.GtwPosComercioService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/posComercios")
public class GtwPosComercioController {

    private final GtwPosComercioService gtwPosComercioService;

    public GtwPosComercioController(GtwPosComercioService gtwPosComercioService) {
        this.gtwPosComercioService = gtwPosComercioService;
    }

    @GetMapping
    public ResponseEntity<List<GtwPosComercio>> getAll() {
        List<GtwPosComercio> posComercios = gtwPosComercioService.findAll();
        return ResponseEntity.ok(posComercios);
    }

    @GetMapping("/{codigo}/{tipo}")
    public ResponseEntity<GtwPosComercio> getById(@PathVariable String codigo, @PathVariable String tipo) {
        GtwPosComercioPK id = new GtwPosComercioPK(codigo, tipo);
        try {
            GtwPosComercio comercio = gtwPosComercioService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Comercio con código " + codigo + " y tipo " + tipo + " no encontrado."));
            return ResponseEntity.ok(comercio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GtwPosComercio> create(@RequestBody GtwPosComercio gtwPosComercio) {
        GtwPosComercio savedComercio = gtwPosComercioService.save(gtwPosComercio);
        return ResponseEntity.ok(savedComercio);
    }

    @PutMapping("/{codigo}/{tipo}")
    public ResponseEntity<GtwPosComercio> update(@PathVariable String codigo, @PathVariable String tipo,
            @RequestBody GtwPosComercio gtwPosComercio) {
        GtwPosComercioPK id = new GtwPosComercioPK(codigo, tipo);
        try {
            GtwPosComercio updatedComercio = gtwPosComercioService.update(id, gtwPosComercio);
            return ResponseEntity.ok(updatedComercio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codigo}/{tipo}")
    public ResponseEntity<Void> delete(@PathVariable String codigo, @PathVariable String tipo) {
        GtwPosComercioPK id = new GtwPosComercioPK(codigo, tipo);
        gtwPosComercioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}