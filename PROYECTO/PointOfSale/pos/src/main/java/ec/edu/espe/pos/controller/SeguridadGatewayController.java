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

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.seguridadGatewayService.obtenerPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<Object> obtenerPorEstado(@PathVariable String estado) {
        try {
            List<SeguridadGateway> gateways = this.seguridadGatewayService.obtenerPorEstado(estado);
            return ResponseEntity.ok(gateways);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener gateways por estado: " + e.getMessage());
        }
    }

    @PostMapping("/actualizar-automatico")
    public ResponseEntity<Object> procesarActualizacionAutomatica(@RequestBody SeguridadGateway seguridadGateway) {
        try {
            return ResponseEntity.ok(this.seguridadGatewayService.procesarActualizacionAutomatica(seguridadGateway));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar la actualización automática: " + e.getMessage());
        }
    }
} 