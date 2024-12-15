package ec.edu.espe.gateway.facturacion.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.facturacion.model.GtwFacturacionComercio;
import ec.edu.espe.gateway.facturacion.services.GtwFacturacionComercioService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/facturacion-comercios")
public class GtwFacturacionComercioController {

    private final GtwFacturacionComercioService facturacionComercioService;

    public GtwFacturacionComercioController(GtwFacturacionComercioService facturacionComercioService) {
        this.facturacionComercioService = facturacionComercioService;
    }

    @GetMapping
    public ResponseEntity<List<GtwFacturacionComercio>> getAll() {
        List<GtwFacturacionComercio> facturaciones = facturacionComercioService.findAll();
        return ResponseEntity.ok(facturaciones);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<GtwFacturacionComercio> getById(@PathVariable Integer codigo) {
        try {
            GtwFacturacionComercio facturacion = facturacionComercioService.findById(codigo)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Facturación de Comercio con código " + codigo + " no encontrada."));
            return ResponseEntity.ok(facturacion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GtwFacturacionComercio> create(@RequestBody GtwFacturacionComercio facturacionComercio) {
        GtwFacturacionComercio savedFacturacion = facturacionComercioService.save(facturacionComercio);
        return ResponseEntity.ok(savedFacturacion);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<GtwFacturacionComercio> update(
            @PathVariable Integer codigo,
            @RequestBody GtwFacturacionComercio facturacionComercio) {
        try {
            GtwFacturacionComercio updatedFacturacion = facturacionComercioService.update(codigo, facturacionComercio);
            return ResponseEntity.ok(updatedFacturacion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

   /*@DeleteMapping("/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable Integer codigo) {
        try {
            facturacionComercioService.deleteById(codigo);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    } */ 
}