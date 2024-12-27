package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.GtwSeguridadGateway;
import ec.edu.espe.gateway.seguridad.services.GtwSeguridadGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/seguridad-gateways")
public class GtwSeguridadGatewayController {

    private final GtwSeguridadGatewayService gatewayService;

    public GtwSeguridadGatewayController(GtwSeguridadGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping
    public ResponseEntity<List<GtwSeguridadGateway>> getAll() {
        List<GtwSeguridadGateway> gateways = gatewayService.getAllGateways();
        return ResponseEntity.ok(gateways);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GtwSeguridadGateway> getById(@PathVariable Integer id) {
        try {
            GtwSeguridadGateway gateway = gatewayService.getGatewayById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Gateway con ID " + id + " no encontrado."));
            return ResponseEntity.ok(gateway);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GtwSeguridadGateway> create(@RequestBody GtwSeguridadGateway gateway) {
        GtwSeguridadGateway savedGateway = gatewayService.createGateway(gateway);
        return ResponseEntity.ok(savedGateway);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GtwSeguridadGateway> update(
            @PathVariable Integer id,
            @RequestBody GtwSeguridadGateway gatewayDetails) {
        try {
            GtwSeguridadGateway updatedGateway = gatewayService.updateGateway(id, gatewayDetails);
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
}