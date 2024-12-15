package ec.edu.espe.pos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.pos.model.POSConfiguracion;
import ec.edu.espe.pos.model.PosConfiguracionPK;
import ec.edu.espe.pos.services.POSConfiguracionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/pos/configuraciones")
@CrossOrigin(origins = "*")
public class POSConfiguracionController {

    private final POSConfiguracionService posConfiguracionService;

    public POSConfiguracionController(POSConfiguracionService posConfiguracionService) {
        this.posConfiguracionService = posConfiguracionService;
    }

    @GetMapping
    public ResponseEntity<List<POSConfiguracion>> findAll() {
        return ResponseEntity.ok(posConfiguracionService.findAll());
    }

    @GetMapping("/{codigo}/{modelo}")
    public ResponseEntity<POSConfiguracion> findById(@PathVariable String codigo, @PathVariable String modelo) {
        PosConfiguracionPK id = new PosConfiguracionPK(codigo, modelo);
        return posConfiguracionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<POSConfiguracion> create(@Valid @RequestBody POSConfiguracion posConfiguracion) {
        try {
            POSConfiguracion savedConfig = posConfiguracionService.save(posConfiguracion);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedConfig);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{codigo}/{modelo}")
    public ResponseEntity<POSConfiguracion> update(
            @PathVariable String codigo,
            @PathVariable String modelo,
            @Valid @RequestBody POSConfiguracion posConfiguracion) {
        try {
            PosConfiguracionPK id = new PosConfiguracionPK(codigo, modelo);
            if (!posConfiguracionService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            posConfiguracion.setPk(id);
            return ResponseEntity.ok(posConfiguracionService.save(posConfiguracion));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{codigo}/{modelo}")
    public ResponseEntity<Void> delete(@PathVariable String codigo, @PathVariable String modelo) {
        try {
            PosConfiguracionPK id = new PosConfiguracionPK(codigo, modelo);
            if (!posConfiguracionService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            posConfiguracionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
