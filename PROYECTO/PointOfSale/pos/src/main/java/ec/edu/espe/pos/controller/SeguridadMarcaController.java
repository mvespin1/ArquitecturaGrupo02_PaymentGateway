package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.SeguridadMarca;
import ec.edu.espe.pos.service.SeguridadMarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguridad-marca")
public class SeguridadMarcaController {

    private final SeguridadMarcaService seguridadMarcaService;

    public SeguridadMarcaController(SeguridadMarcaService seguridadMarcaService) {
        this.seguridadMarcaService = seguridadMarcaService;
    }

    @GetMapping
    public ResponseEntity<List<SeguridadMarca>> listarTodas() {
        return ResponseEntity.ok(this.seguridadMarcaService.obtenerTodas());
    }

    @GetMapping("/{marca}")
    public ResponseEntity<SeguridadMarca> obtenerPorMarca(@PathVariable String marca) {
        return this.seguridadMarcaService.obtenerPorMarca(marca)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SeguridadMarca> crear(@RequestBody SeguridadMarca seguridadMarca) {
        return ResponseEntity.ok(this.seguridadMarcaService.crear(seguridadMarca));
    }

    @PatchMapping("/{marca}/clave")
    public ResponseEntity<SeguridadMarca> actualizarClave(
            @PathVariable String marca,
            @RequestParam String clave) {
        try {
            SeguridadMarca actualizada = this.seguridadMarcaService.actualizarClave(marca, clave);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 