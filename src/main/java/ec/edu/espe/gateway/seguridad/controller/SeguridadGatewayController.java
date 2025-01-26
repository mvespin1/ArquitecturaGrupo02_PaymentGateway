package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadGateway;
import ec.edu.espe.gateway.seguridad.services.SeguridadGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ec.edu.espe.gateway.exception.NotFoundException;

@RestController
@RequestMapping("/v1/seguridad-gateway")
public class SeguridadGatewayController {

    private static final Logger logger = LoggerFactory.getLogger(SeguridadGatewayController.class);

    private final SeguridadGatewayService gatewayService;

    public SeguridadGatewayController(SeguridadGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/clave-activa")
    public ResponseEntity<SeguridadGateway> obtenerClaveActiva() {
        try {
            SeguridadGateway claveActiva = gatewayService.obtenerClaveActiva();
            return ResponseEntity.ok(claveActiva);
        } catch (NotFoundException e) {
            logger.error("Clave activa no encontrada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error al obtener clave activa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
