package ec.edu.espe.gateway.comision.controller;

import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.services.ComisionSegmentoService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/v1/comision-segmentos")
@Tag(name = "Comisión Segmentos", description = "API para la gestión de segmentos de comisiones")
public class ComisionSegmentoController {

    private final ComisionSegmentoService segmentoService;
    private static final Logger logger = LoggerFactory.getLogger(ComisionSegmentoController.class);

    public ComisionSegmentoController(ComisionSegmentoService segmentoService) {
        this.segmentoService = segmentoService;
    }

    @Operation(summary = "Obtener segmento de comisión por ID compuesto",
               description = "Retorna un segmento de comisión específico basado en su comisión y transacciones desde")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Segmento de comisión encontrado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ComisionSegmento.class))),
        @ApiResponse(responseCode = "404", description = "Segmento de comisión no encontrado",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @GetMapping("/{comision}/{transaccionesDesde}")
    public ResponseEntity<ComisionSegmento> getById(
            @Parameter(description = "ID de la comisión", required = true)
            @PathVariable Integer comision,
            @Parameter(description = "Número de transacciones desde", required = true)
            @PathVariable Integer transaccionesDesde) {
        try {
            logger.info("Obteniendo segmento de comisión por comision: {} y transaccionesDesde: {}", comision,
                    transaccionesDesde);
            return segmentoService.findById(comision, transaccionesDesde)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (EntityNotFoundException e) {
            logger.warn("Segmento de comisión no encontrado para comision: {} y transaccionesDesde: {}", comision,
                    transaccionesDesde);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error al obtener segmento de comisión", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Actualizar segmento de comisión",
               description = "Actualiza un segmento de comisión existente con nuevos valores de rango y monto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Segmento de comisión actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ComisionSegmento.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", description = "Segmento de comisión no encontrado",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @PutMapping("/{comision}/{transaccionesDesde}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID de la comisión", required = true)
            @PathVariable Integer comision,
            @Parameter(description = "Número de transacciones desde", required = true)
            @PathVariable Integer transaccionesDesde,
            @Parameter(description = "Número de transacciones hasta", required = true)
            @RequestParam Integer transaccionesHasta,
            @Parameter(description = "Monto de la comisión", required = true)
            @RequestParam BigDecimal monto) {
        try {
            logger.info(
                    "Actualizando segmento de comisión para comision: {}, transaccionesDesde: {}, transaccionesHasta: {}, monto: {}",
                    comision, transaccionesDesde, transaccionesHasta, monto);
            ComisionSegmento updatedSegmento = segmentoService.update(
                    comision, transaccionesDesde, transaccionesHasta, monto);
            return ResponseEntity.ok(updatedSegmento);
        } catch (EntityNotFoundException e) {
            logger.warn("Segmento de comisión no encontrado para actualizar: comision: {}, transaccionesDesde: {}",
                    comision, transaccionesDesde);
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos al actualizar segmento de comisión: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar el segmento de comisión", e);
            return ResponseEntity.internalServerError()
                    .body("Error al actualizar el segmento: " + e.getMessage());
        }
    }
}