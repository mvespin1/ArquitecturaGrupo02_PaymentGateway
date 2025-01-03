package ec.edu.espe.gateway.comision.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.services.ComisionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/comisiones")
public class ComisionController {

    private final ComisionService comisionService;

    public ComisionController(ComisionService comisionService) {
        this.comisionService = comisionService;
    }

    @GetMapping
    public ResponseEntity<List<Comision>> getAll() {
        try {
            List<Comision> comisiones = comisionService.findAll();
            return ResponseEntity.ok(comisiones);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Comision> getById(@PathVariable Integer codigo) {
        try {
            Comision comision = comisionService.findById(codigo)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Comisión con código " + codigo + " no encontrada."));
            return ResponseEntity.ok(comision);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Comision comision) {
        try {
            Comision savedComision = comisionService.save(comision);
            return ResponseEntity.ok(savedComision);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la comisión: " + e.getMessage());
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> update(@PathVariable Integer codigo, @RequestBody Comision comision) {
        try {
            Comision updatedComision = comisionService.update(codigo, comision);
            return ResponseEntity.ok(updatedComision);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la comisión: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en los datos: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Para ver el error en los logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al actualizar la comisión: " + e.getMessage());
        }
    }
}