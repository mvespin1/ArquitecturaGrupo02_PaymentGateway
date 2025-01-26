package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadGateway;
import ec.edu.espe.gateway.seguridad.services.SeguridadGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ec.edu.espe.gateway.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/seguridad-gateway")
@Tag(name = "Seguridad Gateway", description = "API para gestionar las claves de seguridad del gateway")
public class SeguridadGatewayController {

    private static final Logger logger = LoggerFactory.getLogger(SeguridadGatewayController.class);

    private final SeguridadGatewayService gatewayService;

    public SeguridadGatewayController(SeguridadGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @Operation(summary = "Obtener clave activa del gateway", 
               description = "Retorna la clave de seguridad activa actual del gateway")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clave activa encontrada",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SeguridadGateway.class))),
        @ApiResponse(responseCode = "404", description = "Clave activa no encontrada",
                    content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
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
