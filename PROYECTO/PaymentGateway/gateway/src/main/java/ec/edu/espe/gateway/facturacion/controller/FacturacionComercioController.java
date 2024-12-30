package ec.edu.espe.gateway.facturacion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ec.edu.espe.gateway.facturacion.services.FacturacionComercioService;
import ec.edu.espe.gateway.facturacion.services.FacturaService;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/facturacion")
public class FacturacionComercioController {

    @Autowired
    private FacturacionComercioService facturacionComercioService;

    @Autowired
    private FacturaService facturaService;

    // Obtener una facturación por código
    @GetMapping("/{codigo}")
    public ResponseEntity<FacturacionComercio> obtenerPorCodigo(@PathVariable Integer codigo) {
        try {
            FacturacionComercio facturacion = facturacionComercioService.obtenerPorCodigo(codigo);
            return ResponseEntity.ok(facturacion);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Obtener facturaciones por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<FacturacionComercio>> obtenerPorEstado(@PathVariable String estado) {
        try {
            List<FacturacionComercio> facturaciones = facturacionComercioService.obtenerPorEstado(estado);
            return ResponseEntity.ok(facturaciones);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Crear una nueva facturación
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody FacturacionComercio facturacionComercio) {
        try {
            facturacionComercioService.crear(facturacionComercio);
            return ResponseEntity.status(201).body("Facturación creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error al crear la facturación: " + e.getMessage());
        }
    }

    // Actualizar una facturación existente
    @PutMapping
    public ResponseEntity<String> actualizar(@Valid @RequestBody FacturacionComercio facturacionComercio) {
        try {
            facturacionComercioService.actualizar(facturacionComercio);
            return ResponseEntity.ok("Facturación actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error al actualizar la facturación: " + e.getMessage());
        }
    }

    // Obtener facturaciones pendientes de pago
    @GetMapping("/pendientes-pago")
    public ResponseEntity<List<FacturacionComercio>> obtenerFacturacionesPendientesPago() {
        try {
            List<FacturacionComercio> facturaciones = facturacionComercioService.obtenerFacturacionesPendientesPago();
            return ResponseEntity.ok(facturaciones);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Marcar una facturación como pagada
    @PostMapping("/marcar-pagado/{codigo}")
    public ResponseEntity<String> marcarComoPagado(@PathVariable Integer codigo) {
        try {
            facturacionComercioService.marcarComoPagado(codigo);
            return ResponseEntity.ok("Facturación marcada como pagada");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error al marcar la facturación como pagada: " + e.getMessage());
        }
    }

    // Procesar la facturación automática para todos los comercios
    @PostMapping("/procesar-facturacion-automatica")
    public ResponseEntity<String> procesarFacturacionAutomatica() {
        try {
            facturaService.procesarFacturacionAutomatica();
            return ResponseEntity.ok("Facturación automática procesada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error al procesar la facturación automática: " + e.getMessage());
        }
    }
}
