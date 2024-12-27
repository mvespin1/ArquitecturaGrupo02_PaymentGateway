package ec.edu.espe.gateway.comision.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comision.model.GtwComision;
import ec.edu.espe.gateway.comision.services.GtwComisionService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/comisiones")
public class GtwComisionController {

    private final GtwComisionService comisionService;

    public GtwComisionController(GtwComisionService comisionService) {
        this.comisionService = comisionService;
    }

    @GetMapping
    public ResponseEntity<List<GtwComision>> getAll() {
        List<GtwComision> comisiones = comisionService.findAll();
        return ResponseEntity.ok(comisiones);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<GtwComision> getById(@PathVariable Integer codigo) {
        try {
            GtwComision comision = comisionService.findById(codigo)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Comisión con código " + codigo + " no encontrada."));
            return ResponseEntity.ok(comision);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GtwComision> create(@RequestBody GtwComision comision) {
        try {
            GtwComision savedComision = comisionService.save(comision);
            return ResponseEntity.ok(savedComision);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<GtwComision> update(
            @PathVariable Integer codigo,
            @RequestParam Double montoBase,
            @RequestParam Integer transaccionesBase,
            @RequestParam Boolean manejaSegmentos) {
        try {
            // Crear objeto con los valores específicos que deben actualizarse
            GtwComision partialUpdate = new GtwComision();
            partialUpdate.setMontoBase(null);;
            partialUpdate.setTransaccionesBase(null);;
            partialUpdate.setManejaSegmentos(manejaSegmentos);; // Conversión a "S" o "N"

            // Llamar al servicio para la actualización
            GtwComision updatedComision = comisionService.update(codigo, partialUpdate);
            return ResponseEntity.ok(updatedComision);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}