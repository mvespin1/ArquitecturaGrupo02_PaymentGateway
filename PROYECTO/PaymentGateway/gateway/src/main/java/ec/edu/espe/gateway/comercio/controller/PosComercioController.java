package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.comercio.services.PosComercioService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/posComercios")
public class PosComercioController {

    private final PosComercioService posComercioService;

    public PosComercioController(PosComercioService posComercioService) {
        this.posComercioService = posComercioService;
    }

    @GetMapping
    public ResponseEntity<List<PosComercio>> getAll() {
        List<PosComercio> posComercios = posComercioService.findAll();
        return ResponseEntity.ok(posComercios);
    }

    @GetMapping("/{codigo}/{tipo}")
    public ResponseEntity<PosComercio> getById(@PathVariable String codigo, @PathVariable String tipo) {
        PosComercioPK id = new PosComercioPK(codigo, tipo);
        try {
            PosComercio comercio = posComercioService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Comercio con código " + codigo + " y tipo " + tipo + " no encontrado."));
            return ResponseEntity.ok(comercio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PosComercio> create(@RequestBody PosComercio posComercio) {
        PosComercio savedComercio = posComercioService.save(posComercio);
        return ResponseEntity.ok(savedComercio);
    }

    @PutMapping("/{codigo}/{tipo}")
    public ResponseEntity<PosComercio> update(@PathVariable String codigo, @PathVariable String tipo,
                                              @RequestBody PosComercio posComercio) {
        PosComercioPK id = new PosComercioPK(codigo, tipo);
        try {
            PosComercio updatedComercio = posComercioService.update(id, posComercio);
            return ResponseEntity.ok(updatedComercio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codigo}/{tipo}")
    public ResponseEntity<Void> delete(@PathVariable String codigo, @PathVariable String tipo) {
        PosComercioPK id = new PosComercioPK(codigo, tipo);
        posComercioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}