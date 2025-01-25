package ec.edu.espe.gateway.comision.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.services.ComisionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ec.edu.espe.gateway.exception.DuplicateException;
import ec.edu.espe.gateway.exception.InvalidDataException;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
@RestController
@RequestMapping("/v1/comisiones")
public class ComisionController {

    private final ComisionService comisionService;
    private static final Logger logger = LoggerFactory.getLogger(ComisionController.class);

    public ComisionController(ComisionService comisionService) {
        this.comisionService = comisionService;
    }

    @GetMapping
    public ResponseEntity<List<Comision>> getAll() {
        try {
            logger.info("Obteniendo todas las comisiones");
            List<Comision> comisiones = comisionService.findAll();
            return ResponseEntity.ok(comisiones);
        } catch (Exception e) {
            logger.error("Error al obtener todas las comisiones", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Comision> getById(@PathVariable Integer codigo) {
        try {
            logger.info("Obteniendo comisión por código: {}", codigo);
            return comisionService.findById(codigo)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (EntityNotFoundException e) {
            logger.warn("Comisión no encontrada con código: {}", codigo);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error al obtener comisión por código: {}", codigo, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Comision comision) {
        try {
            logger.info("Creando nueva comisión: {}", comision);
            Comision savedComision = comisionService.save(comision);
            return ResponseEntity.ok(savedComision);
        } catch (DuplicateException | InvalidDataException e) {
            logger.warn("Error al crear comisión: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al crear la comisión", e);
            return ResponseEntity.internalServerError()
                    .body("Error al crear la comisión: " + e.getMessage());
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> update(@PathVariable Integer codigo, @RequestBody Comision comision) {
        try {
            logger.info("Actualizando comisión con código: {}", codigo);
            Comision updatedComision = comisionService.update(codigo, comision);
            return ResponseEntity.ok(updatedComision);
        } catch (EntityNotFoundException e) {
            logger.warn("Comisión no encontrada para actualizar: {}", codigo);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la comisión: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos al actualizar comisión: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en los datos: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error interno al actualizar la comisión", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al actualizar la comisión: " + e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<?> delete(@PathVariable Integer codigo) {
        try {
            logger.info("Eliminando comisión con código: {}", codigo);
            comisionService.deleteById(codigo);
            return ResponseEntity.noContent().build();
        } catch (UnsupportedOperationException e) {
            logger.warn("Operación no soportada al eliminar comisión: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar la comisión", e);
            return ResponseEntity.internalServerError()
                    .body("Error al eliminar la comisión: " + e.getMessage());
        }
    }
}