package ec.edu.espe.gateway.facturacion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ec.edu.espe.gateway.facturacion.services.FacturacionComercioService;
import ec.edu.espe.gateway.facturacion.services.FacturaService;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.exception.NotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/v1/facturacion")
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
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // Obtener facturaciones por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<FacturacionComercio>> obtenerPorEstado(@PathVariable String estado) {
        try {
            List<FacturacionComercio> facturaciones = facturacionComercioService.obtenerPorEstado(estado);
            return ResponseEntity.ok(facturaciones);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // Crear una nueva facturación
    @PostMapping
    public ResponseEntity<String> crear(@Valid @RequestBody FacturacionComercio facturacionComercio) {
        try {
            facturacionComercioService.crear(facturacionComercio);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Facturación creada exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la facturación: " + e.getMessage());
        }
    }

    // Actualizar una facturación existente
    @PutMapping("/{codigo}")
    public ResponseEntity<String> actualizar(
            @PathVariable Integer codigo,
            @Valid @RequestBody FacturacionComercio facturacionComercio) {
        try {
            facturacionComercio.setCodigo(codigo);
            facturacionComercioService.actualizar(facturacionComercio);
            return ResponseEntity.ok("Facturación actualizada exitosamente");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la facturación: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la facturación: " + e.getMessage());
        }
    }

    // Obtener facturaciones pendientes de pago
    @GetMapping("/pendientes-pago")
    public ResponseEntity<List<FacturacionComercio>> obtenerFacturacionesPendientesPago() {
        try {
            List<FacturacionComercio> facturaciones = facturacionComercioService.obtenerFacturacionesPendientesPago();
            return ResponseEntity.ok(facturaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // Marcar una facturación como pagada
    @PostMapping("/marcar-pagado/{codigo}")
    public ResponseEntity<String> marcarComoPagado(@PathVariable Integer codigo) {
        try {
            facturacionComercioService.marcarComoPagado(codigo);
            return ResponseEntity.ok("Facturación marcada como pagada exitosamente");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la facturación: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de estado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al marcar la facturación como pagada: " + e.getMessage());
        }
    }

    // Procesar la facturación automática para todos los comercios
    @PostMapping("/procesar-facturacion-automatica")
    public ResponseEntity<String> procesarFacturacionAutomatica() {
        try {
            facturaService.procesarFacturacionAutomatica();
            return ResponseEntity.ok("Facturación automática procesada exitosamente");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron facturas para procesar: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la facturación automática: " + e.getMessage());
        }
    }
}
