package ec.edu.espe.gateway.transaccion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.services.TransaccionService;
import ec.edu.espe.gateway.transaccion.services.RecurrenceService;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import jakarta.persistence.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/v1/transacciones")
@Tag(name = "Transacciones", description = "API para gestionar las transacciones del gateway de pagos")
public class TransaccionController {

    private static final Logger log = LoggerFactory.getLogger(TransaccionController.class);
    private final TransaccionService transaccionService;
    private final TransaccionRepository transaccionRepository;

    public static final String ESTADO_AUTORIZADO = "AUT";
    public static final String ESTADO_RECHAZADO = "REC";

    public TransaccionController(TransaccionService transaccionService,
            TransaccionRepository transaccionRepository,
            RecurrenceService recurrenceService) {
        this.transaccionService = transaccionService;
        this.transaccionRepository = transaccionRepository;
    }

    @Operation(summary = "Sincronizar transacción desde POS", 
               description = "Procesa y sincroniza una transacción recibida desde un punto de venta (POS)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transacción aceptada o en proceso de validación",
                    content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Transacción aceptada"))),
        @ApiResponse(responseCode = "400", description = "Transacción rechazada o datos inválidos",
                    content = @Content(mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Transacción rechazada"))),
        @ApiResponse(responseCode = "404", description = "Entidad no encontrada",
                    content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping("/sincronizar")
    public ResponseEntity<String> sincronizarTransaccion(
            @Parameter(description = "Datos de la transacción a sincronizar", required = true)
            @RequestBody Transaccion transaccion) {
        log.info("Recibiendo petición de sincronización desde POS");
        log.info("Datos de transacción recibidos: {}", transaccion);
        log.info("Comercio ID: {}", transaccion.getComercio().getCodigo());
        log.info("Facturación ID: {}", transaccion.getFacturacionComercio().getCodigo());
        
        try {
            transaccionService.procesarTransaccionPOS(transaccion);
            log.info("Sincronización completada exitosamente");
            
            
            Transaccion transaccionActualizada = transaccionRepository.findById(transaccion.getCodigo())
                    .orElseThrow(() -> new EntityNotFoundException("Transacción no encontrada"));
            
            
            if (ESTADO_AUTORIZADO.equals(transaccionActualizada.getEstado())) {
                return ResponseEntity.status(201)
                        .body("Transacción aceptada");
            } else if (ESTADO_RECHAZADO.equals(transaccionActualizada.getEstado())) {
                return ResponseEntity.status(400)
                        .body("Transacción rechazada");
            } else {
                return ResponseEntity.status(202)
                        .body("Transacción en proceso de validación");
            }
            
        } catch (EntityNotFoundException e) {
            log.error("Entidad no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
            
        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
            
        } catch (Exception e) {
            log.error("Error inesperado al sincronizar: {}", e.getMessage());
            return ResponseEntity.status(400)
                    .body("Transacción rechazada");
        }
    }
}
