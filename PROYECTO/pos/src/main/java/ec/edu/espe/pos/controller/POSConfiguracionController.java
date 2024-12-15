package ec.edu.espe.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.pos.model.POSConfiguracion;
import ec.edu.espe.pos.model.PosConfiguracionPK;
import ec.edu.espe.pos.services.POSConfiguracionService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/pos/configuraciones")
public class POSConfiguracionController {

    @Autowired
    private POSConfiguracionService posConfiguracionService;

    @GetMapping
    public ResponseEntity<List<POSConfiguracion>> findAll() {
        return ResponseEntity.ok(posConfiguracionService.findAll());
    }

    @GetMapping("/{codigo}/{tipo}")
    public ResponseEntity<POSConfiguracion> findById(@PathVariable String codigo, @PathVariable String tipo) {
        PosConfiguracionPK id = new PosConfiguracionPK(codigo, tipo);
        try {
            POSConfiguracion posConfiguracion = posConfiguracionService.findById(id)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Configuración POS con ID " + id + " no encontrada."));
            return ResponseEntity.ok(posConfiguracion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<POSConfiguracion> create(@RequestBody POSConfiguracion posConfiguracion) {
        return ResponseEntity.ok(posConfiguracionService.save(posConfiguracion));
    }

}
