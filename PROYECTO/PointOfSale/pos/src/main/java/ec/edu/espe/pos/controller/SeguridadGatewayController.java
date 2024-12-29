package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.SeguridadGateway;
import ec.edu.espe.pos.service.SeguridadGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguridad-gateway")
public class SeguridadGatewayController {

    private final SeguridadGatewayService seguridadGatewayService;

    public SeguridadGatewayController(SeguridadGatewayService seguridadGatewayService) {
        this.seguridadGatewayService = seguridadGatewayService;
    }

    @GetMapping
    public ResponseEntity<List<SeguridadGateway>> listarTodos() {
        return ResponseEntity.ok(this.seguridadGatewayService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguridadGateway> obtenerPorId(@PathVariable Integer id) {
        return this.seguridadGatewayService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SeguridadGateway> crear(@RequestBody SeguridadGateway seguridadGateway) {
        return ResponseEntity.ok(this.seguridadGatewayService.crear(seguridadGateway));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SeguridadGateway> actualizar(
            @PathVariable Integer id,
            @RequestParam String clave,
            @RequestParam String estado) {
        try {
            SeguridadGateway actualizado = this.seguridadGatewayService.actualizar(id, clave, estado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 