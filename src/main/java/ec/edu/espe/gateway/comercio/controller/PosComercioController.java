package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.comercio.services.PosComercioService;
import java.util.List;
import ec.edu.espe.gateway.comercio.controller.mapper.ConfiguracionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ec.edu.espe.gateway.exception.InvalidDataException;
import ec.edu.espe.gateway.exception.NotFoundException;
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
@RequestMapping("/v1/pos-comercio")
@Tag(name = "POS Comercio", description = "API para gestionar los POS de comercios")
public class PosComercioController {

    private final PosComercioService posComercioService;
    private static final Logger logger = LoggerFactory.getLogger(PosComercioController.class);

    public PosComercioController(
            PosComercioService posComercioService,
            ConfiguracionMapper configuracionMapper) {
        this.posComercioService = posComercioService;
    }

    @Operation(summary = "Obtener todos los POS de comercio", 
               description = "Retorna una lista de todos los POS de comercio registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de POS encontrada exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PosComercio.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PosComercio>> obtenerTodos() {
        logger.info("Obteniendo todos los POS de comercio");
        List<PosComercio> posComercios = posComercioService.obtenerTodos();
        return ResponseEntity.ok(posComercios);
    }

    @Operation(summary = "Crear un nuevo POS de comercio",
               description = "Crea un nuevo registro de POS de comercio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "POS creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> crear(
            @Parameter(description = "Datos del POS a crear", required = true)
            @RequestBody PosComercio posComercio) {
        try {
            logger.info("Creando un nuevo POS de comercio: {}", posComercio);
            posComercioService.crear(posComercio);
            return ResponseEntity.ok().build();
        } catch (InvalidDataException e) {
            logger.warn("Error al crear POS de comercio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Activar un POS de comercio",
               description = "Activa un POS de comercio específico por su código y tipo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "POS activado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en la activación del POS",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "POS no encontrado",
            content = @Content)
    })
    @PutMapping("/{codigoPos}/{tipo}/activar")
    public ResponseEntity<String> activarPOS(
            @Parameter(description = "Código del POS", required = true) @PathVariable String codigoPos,
            @Parameter(description = "Tipo del POS", required = true) @PathVariable String tipo) {
        try {
            logger.info("Activando POS de comercio con código {} y tipo {}", codigoPos, tipo);
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.activarPOS(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException | IllegalStateException e) {
            logger.warn("Error al activar POS de comercio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Inactivar un POS de comercio",
               description = "Inactiva un POS de comercio específico por su código y tipo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "POS inactivado exitosamente"),
        @ApiResponse(responseCode = "404", description = "POS no encontrado",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @PutMapping("/{codigoPos}/{tipo}/inactivar")
    public ResponseEntity<String> inactivarPOS(
            @Parameter(description = "Código del POS", required = true) @PathVariable String codigoPos,
            @Parameter(description = "Tipo del POS", required = true) @PathVariable String tipo) {
        try {
            logger.info("Inactivando POS de comercio con código {} y tipo {}", codigoPos, tipo);
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.inactivarPOS(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            logger.error("POS de comercio no encontrado para inactivar: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}