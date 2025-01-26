package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadMarca;
import ec.edu.espe.gateway.seguridad.services.SeguridadMarcaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ec.edu.espe.gateway.exception.InvalidDataException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/v1/seguridad-marca")
@Tag(name = "Seguridad Marca", description = "API para gestionar las claves de seguridad por marca")
public class SeguridadMarcaController {

    private static final Logger logger = LoggerFactory.getLogger(SeguridadMarcaController.class);

    private final SeguridadMarcaService marcaService;

    public SeguridadMarcaController(SeguridadMarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @Operation(summary = "Recibir nueva clave de marca", 
               description = "Guarda una nueva clave de seguridad para una marca específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clave guardada exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "400", description = "Datos de clave inválidos",
                    content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping("/recibir-clave")
    public ResponseEntity<String> recibirClave(
            @Parameter(description = "Datos de la nueva clave de marca", required = true)
            @RequestBody SeguridadMarca nuevaClave) {
        try {
            logger.info("Recibiendo nueva clave para marca: {}", nuevaClave.getMarca());
            marcaService.guardarClave(nuevaClave);
            return ResponseEntity.ok("Clave de marca recibida y guardada exitosamente");
        } catch (InvalidDataException e) {
            logger.error("Datos inválidos al recibir clave de marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al recibir clave de marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recibir clave de marca");
        }
    }
} 