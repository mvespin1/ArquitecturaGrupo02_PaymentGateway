package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadProcesador;
import ec.edu.espe.gateway.seguridad.services.SeguridadProcesadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ec.edu.espe.gateway.exception.NotFoundException;
import ec.edu.espe.gateway.exception.InvalidDataException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/v1/seguridad-procesador")
@Tag(name = "Seguridad Procesador", description = "API para gestionar las claves de seguridad de procesadores")
public class SeguridadProcesadorController {

    private static final Logger logger = LoggerFactory.getLogger(SeguridadProcesadorController.class);

    private final SeguridadProcesadorService procesadorService;

    public SeguridadProcesadorController(SeguridadProcesadorService procesadorService) {
        this.procesadorService = procesadorService;
    }

    @Operation(summary = "Recibir nueva clave de procesador", 
               description = "Guarda una nueva clave de seguridad para un procesador específico")
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
            @Parameter(description = "Datos de la nueva clave de procesador", required = true)
            @RequestBody SeguridadProcesador nuevaClave) {
        try {
            logger.info("Recibiendo nueva clave para procesador con código: {}", nuevaClave.getCodigo());
            procesadorService.guardarClave(nuevaClave);
            return ResponseEntity.ok("Clave de procesador recibida y guardada exitosamente");
        } catch (InvalidDataException e) {
            logger.error("Datos inválidos al recibir clave de procesador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al recibir clave de procesador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recibir clave de procesador");
        }
    }

    @Operation(summary = "Actualizar estado de clave de procesador", 
               description = "Actualiza el estado de una clave de procesador existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de clave actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "400", description = "Datos de estado inválidos",
                    content = @Content),
        @ApiResponse(responseCode = "404", description = "Clave de procesador no encontrada",
                    content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PutMapping("/actualizar-estado/{codigo}")
    public ResponseEntity<String> actualizarEstadoClave(
            @Parameter(description = "Código del procesador", required = true)
            @PathVariable Integer codigo,
            @Parameter(description = "Nuevo estado de la clave", required = true)
            @RequestParam String nuevoEstado) {
        try {
            logger.info("Actualizando estado de la clave para procesador con código {}: nuevo estado {}", codigo, nuevoEstado);
            procesadorService.actualizarEstadoClave(codigo, nuevoEstado);
            return ResponseEntity.ok("Estado de la clave de procesador actualizado exitosamente");
        } catch (NotFoundException e) {
            logger.error("Clave de procesador no encontrada para actualizar estado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidDataException e) {
            logger.error("Datos inválidos al actualizar estado de clave de procesador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar estado de clave de procesador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar estado de clave de procesador");
        }
    }
}