package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.PosSeguridadMarca;
import ec.edu.espe.pos.service.PosSeguridadMarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguridad-marca")
public class PosSeguridadMarcaController {

    private final PosSeguridadMarcaService posSeguridadMarcaService;

    public PosSeguridadMarcaController(PosSeguridadMarcaService posSeguridadMarcaService) {
        this.posSeguridadMarcaService = posSeguridadMarcaService;
    }

    @GetMapping
    public ResponseEntity<List<PosSeguridadMarca>> listarTodas() {
        return ResponseEntity.ok(this.posSeguridadMarcaService.obtenerTodas());
    }

    @GetMapping("/{marca}")
    public ResponseEntity<PosSeguridadMarca> obtenerPorMarca(@PathVariable String marca) {
        return this.posSeguridadMarcaService.obtenerPorMarca(marca)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PosSeguridadMarca> crear(@RequestBody PosSeguridadMarca posSeguridadMarca) {
        return ResponseEntity.ok(this.posSeguridadMarcaService.crear(posSeguridadMarca));
    }

    @PatchMapping("/{marca}/clave")
    public ResponseEntity<PosSeguridadMarca> actualizarClave(
            @PathVariable String marca,
            @RequestParam String clave) {
        try {
            PosSeguridadMarca actualizada = this.posSeguridadMarcaService.actualizarClave(marca, clave);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 