package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadGateway;
import ec.edu.espe.gateway.seguridad.services.SeguridadGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/seguridad")
public class SeguridadGatewayController {

    private final SeguridadGatewayService gatewayService;

    public SeguridadGatewayController(SeguridadGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    // Endpoint para que el POS obtenga la clave activa
    @GetMapping("/clave-activa")
    public ResponseEntity<SeguridadGateway> obtenerClaveActiva() {
        try {
            SeguridadGateway claveActiva = gatewayService.obtenerClaveActiva();
            return ResponseEntity.ok(claveActiva);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
