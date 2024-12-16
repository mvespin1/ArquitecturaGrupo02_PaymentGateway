package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.PosSeguridadGateway;
import ec.edu.espe.pos.service.PosSeguridadGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguridad-gateway")
public class PosSeguridadGatewayController {

    private final PosSeguridadGatewayService posSeguridadGatewayService;

    public PosSeguridadGatewayController(PosSeguridadGatewayService posSeguridadGatewayService) {
        this.posSeguridadGatewayService = posSeguridadGatewayService;
    }

    @GetMapping
    public ResponseEntity<List<PosSeguridadGateway>> listarTodos() {
        return ResponseEntity.ok(this.posSeguridadGatewayService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PosSeguridadGateway> obtenerPorId(@PathVariable Integer id) {
        return this.posSeguridadGatewayService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PosSeguridadGateway> crear(@RequestBody PosSeguridadGateway posSeguridadGateway) {
        return ResponseEntity.ok(this.posSeguridadGatewayService.crear(posSeguridadGateway));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PosSeguridadGateway> actualizar(
            @PathVariable Integer id,
            @RequestParam String clave,
            @RequestParam String estado) {
        try {
            PosSeguridadGateway actualizado = this.posSeguridadGatewayService.actualizar(id, clave, estado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 