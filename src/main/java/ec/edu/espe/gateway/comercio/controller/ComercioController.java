package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.services.ComercioService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ec.edu.espe.gateway.exception.DuplicateException;
import ec.edu.espe.gateway.exception.InvalidDataException;
import ec.edu.espe.gateway.exception.StateException;
import ec.edu.espe.gateway.exception.ValidationException;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
@RestController
@RequestMapping("/v1/comercios")
@Tag(name = "Comercio", description = "API para la gestión de comercios")
public class ComercioController {

    private final ComercioService comercioService;
    private static final Logger logger = LoggerFactory.getLogger(ComercioController.class);

    public ComercioController(ComercioService comercioService) {
        this.comercioService = comercioService;
    }

    @Operation(summary = "Obtener todos los comercios", 
              description = "Retorna una lista de todos los comercios registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de comercios obtenida exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Comercio.class, type = "array"))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Comercio>> obtenerTodos() {
        try {
            logger.info("Obteniendo todos los comercios");
            List<Comercio> comercios = comercioService.obtenerTodos();
            return ResponseEntity.ok(comercios);
        } catch (Exception e) {
            logger.error("Error al obtener todos los comercios", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Registrar un nuevo comercio",
              description = "Crea un nuevo registro de comercio en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comercio registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o comercio duplicado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<String> registrarComercio(
            @Parameter(description = "Datos del comercio a registrar", required = true)
            @RequestBody Comercio comercio) {
        try {
            logger.info("Registrando un nuevo comercio: {}", comercio);
            comercioService.registrarComercio(comercio);
            return ResponseEntity.ok().build();
        } catch (DuplicateException | InvalidDataException e) {
            logger.warn("Error al registrar comercio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Actualizar estado de un comercio",
              description = "Actualiza el estado de un comercio específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido o error de validación",
            content = @Content(mediaType = "application/json",
                schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(mediaType = "application/json",
                schema = @Schema(type = "string")))
    })
    @PutMapping("/{codigo}/estado")
    public ResponseEntity<String> actualizarEstado(
            @Parameter(description = "Código del comercio", required = true)
            @PathVariable Integer codigo,
            @Parameter(description = "Nuevo estado del comercio", required = true)
            @RequestParam String nuevoEstado) {
        try {
            logger.info("Actualizando estado del comercio con código {}: nuevo estado {}", codigo, nuevoEstado);
            comercioService.actualizarEstado(codigo, nuevoEstado);
            return ResponseEntity.ok().build();
        } catch (StateException | ValidationException e) {
            logger.warn("Error al actualizar estado del comercio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar estado del comercio: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado");
        }
    }
}