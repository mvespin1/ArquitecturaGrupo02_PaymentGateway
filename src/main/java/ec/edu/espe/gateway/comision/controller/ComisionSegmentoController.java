package ec.edu.espe.gateway.comision.controller;

import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.services.ComisionSegmentoService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
@RestController
@RequestMapping("/v1/comision-segmentos")
public class ComisionSegmentoController {

    private final ComisionSegmentoService segmentoService;
    private static final Logger logger = LoggerFactory.getLogger(ComisionSegmentoController.class);

    public ComisionSegmentoController(ComisionSegmentoService segmentoService) {
        this.segmentoService = segmentoService;
    }

    @GetMapping("/{comision}/{transaccionesDesde}")
    public ResponseEntity<ComisionSegmento> getById(
            @PathVariable Integer comision,
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

    @PutMapping("/{comision}/{transaccionesDesde}")
    public ResponseEntity<?> update(
            @PathVariable Integer comision,
            @PathVariable Integer transaccionesDesde,
            @RequestParam Integer transaccionesHasta,
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