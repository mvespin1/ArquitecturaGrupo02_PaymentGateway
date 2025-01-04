package ec.edu.espe.gateway.transaccion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.services.TransaccionService;
import ec.edu.espe.gateway.transaccion.services.RecurrenceService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;
    private final RecurrenceService recurrenceService;

    public TransaccionController(TransaccionService transaccionService, RecurrenceService recurrenceService) {
        this.transaccionService = transaccionService;
        this.recurrenceService = recurrenceService;
    }

    @PostMapping("/pos/{codigoPos}")
    public ResponseEntity<?> crearTransaccionPOS(
            @PathVariable String codigoPos,
            @RequestBody Transaccion transaccion) {
        try {
            Transaccion nuevaTransaccion = transaccionService.crearTransaccionPOS(transaccion, codigoPos);
            return ResponseEntity.ok(nuevaTransaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{codigo}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable Integer codigo,
            @RequestParam String nuevoEstado) {
        try {
            transaccionService.actualizarEstado(codigo, nuevoEstado);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Transaccion>> obtenerPorEstado(@PathVariable String estado) {
        List<Transaccion> transacciones = transaccionService.obtenerPorEstado(estado);
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/comercio/{codigoComercio}")
    public ResponseEntity<List<Transaccion>> obtenerPorComercioYFecha(
            @PathVariable Integer codigoComercio,
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin) {
        List<Transaccion> transacciones = transaccionService.obtenerPorComercioYFecha(
            codigoComercio, fechaInicio, fechaFin);
        return ResponseEntity.ok(transacciones);
    }

    @PostMapping("/recurrentes/procesar")
    public ResponseEntity<Void> procesarTransaccionesRecurrentes() {
        try {
            recurrenceService.procesarTransaccionesRecurrentes();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/recurrentes/comercio/{codigoComercio}/detener")
    public ResponseEntity<Void> detenerRecurrenciasPorComercio(@PathVariable Integer codigoComercio) {
        try {
            recurrenceService.detenerRecurrenciasPorComercio(codigoComercio);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/sincronizar")
    public void sincronizarTransaccion(@RequestBody Transaccion transaccionPOS) {
        // El servicio se encargará de completar los campos faltantes
        transaccionService.procesarTransaccionPOS(transaccionPOS);
    }
}
