package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadGateway;
import ec.edu.espe.gateway.seguridad.services.SeguridadGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/seguridad-gateways")
public class SeguridadGatewayController {

    private final SeguridadGatewayService gatewayService;

    public SeguridadGatewayController(SeguridadGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguridadGateway> getById(@PathVariable Integer id) {
        try {
            SeguridadGateway gateway = gatewayService.getGatewayById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Gateway con ID " + id + " no encontrado."));
            return ResponseEntity.ok(gateway);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SeguridadGateway> create(@RequestBody SeguridadGateway gateway) {
        SeguridadGateway savedGateway = gatewayService.createGateway(gateway);
        return ResponseEntity.ok(savedGateway);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeguridadGateway> update(
            @PathVariable Integer id,
            @RequestBody SeguridadGateway gatewayDetails) {
        try {
            SeguridadGateway updatedGateway = gatewayService.updateGateway(id, gatewayDetails);
            return ResponseEntity.ok(updatedGateway);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            gatewayService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<SeguridadGateway>> getActiveGateways() {
        List<SeguridadGateway> activeGateways = gatewayService.getGatewaysByEstadoActivo();
        if (activeGateways.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activeGateways);
    }
}
