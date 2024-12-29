package ec.edu.espe.gateway.facturacion.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.services.FacturacionComercioService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/facturacion-comercios")
public class FacturacionComercioController {

    private final FacturacionComercioService facturacionComercioService;

    public FacturacionComercioController(FacturacionComercioService facturacionComercioService) {
        this.facturacionComercioService = facturacionComercioService;
    }

    @GetMapping
    public ResponseEntity<List<FacturacionComercio>> getAll() {
        List<FacturacionComercio> facturaciones = facturacionComercioService.findAll();
        return ResponseEntity.ok(facturaciones);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<FacturacionComercio> getById(@PathVariable Integer codigo) {
        try {
            FacturacionComercio facturacion = facturacionComercioService.findById(codigo)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Facturación de Comercio con código " + codigo + " no encontrada."));
            return ResponseEntity.ok(facturacion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FacturacionComercio> create(@RequestBody FacturacionComercio facturacionComercio) {
        FacturacionComercio savedFacturacion = facturacionComercioService.save(facturacionComercio);
        return ResponseEntity.ok(savedFacturacion);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<FacturacionComercio> update(
            @PathVariable Integer codigo,
            @RequestBody FacturacionComercio facturacionComercio) {
        try {
            FacturacionComercio updatedFacturacion = facturacionComercioService.update(codigo, facturacionComercio);
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