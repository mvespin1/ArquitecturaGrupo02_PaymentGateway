package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadGateway;
import ec.edu.espe.gateway.seguridad.services.SeguridadGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ec.edu.espe.gateway.seguridad.exception.NotFoundException;

@RestController
@RequestMapping("/v1/seguridad-gateway")
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
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguridadGateway> getById(@PathVariable Integer id) {
        try {
            return gatewayService.getGatewayById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new NotFoundException(id.toString(), "Gateway"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ... otros endpoints con manejo similar de respuestas ...
}
