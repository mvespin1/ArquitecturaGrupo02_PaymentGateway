package ec.edu.espe.gateway.facturacion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.facturacion.services.FacturacionService;
import ec.edu.espe.gateway.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/facturacion")
@Tag(name = "Facturación", description = "API para la gestión de facturación de comercios")
public class FacturacionComercioController {

    private static final Logger logger = LoggerFactory.getLogger(FacturacionComercioController.class);
    private final FacturacionService facturacionService;

    public FacturacionComercioController(FacturacionService facturacionService) {
        this.facturacionService = facturacionService;
    }

    @Operation(summary = "Marcar facturación como pagada",
               description = "Actualiza el estado de una factura a pagado cuando se confirma el pago del comercio al gateway")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
            description = "Facturación marcada como pagada exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", 
            description = "Error de estado al intentar marcar como pagada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "404", 
            description = "Facturación no encontrada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/marcar-pagado/{codigo}")
    public ResponseEntity<String> marcarComoPagado(
            @Parameter(description = "Código de la facturación a marcar como pagada", required = true)
            @PathVariable Integer codigo) {
        try {
            logger.info("Intentando marcar como pagada la facturación con código: {}", codigo);
            facturacionService.marcarComoPagado(codigo);
            return ResponseEntity.ok("Facturación marcada como pagada exitosamente");
        } catch (NotFoundException e) {
            logger.error("No se encontró la facturación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la facturación: " + e.getMessage());
        } catch (IllegalStateException e) {
            logger.warn("Error de estado al marcar como pagada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de estado: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error al marcar la facturación como pagada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al marcar la facturación como pagada: " + e.getMessage());
        }
    }
}
