package ec.edu.espe.gateway.comision.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.services.ComisionService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/comisiones")
public class ComisionController {

    private final ComisionService comisionService;

    public ComisionController(ComisionService comisionService) {
        this.comisionService = comisionService;
    }

    @GetMapping
    public ResponseEntity<List<Comision>> getAll() {
        List<Comision> comisiones = comisionService.findAll();
        return ResponseEntity.ok(comisiones);
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
        }
    }

    @PostMapping
    public ResponseEntity<Comision> create(@RequestBody Comision comision) {
        try {
            Comision savedComision = comisionService.save(comision);
            return ResponseEntity.ok(savedComision);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Comision> update(
            @PathVariable Integer codigo,
            @RequestParam Double montoBase,
            @RequestParam Integer transaccionesBase,
            @RequestParam Boolean manejaSegmentos) {
        try {
            // Crear objeto con los valores específicos que deben actualizarse
            Comision partialUpdate = new Comision();
            partialUpdate.setMontoBase(null);;
            partialUpdate.setTransaccionesBase(null);;
            partialUpdate.setManejaSegmentos(manejaSegmentos);; // Conversión a "S" o "N"

            // Llamar al servicio para la actualización
            Comision updatedComision = comisionService.update(codigo, partialUpdate);
            return ResponseEntity.ok(updatedComision);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}