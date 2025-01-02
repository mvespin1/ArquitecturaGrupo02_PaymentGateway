package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.comercio.services.PosComercioService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pos-comercios")
public class PosComercioController {

    private final PosComercioService posComercioService;

    public PosComercioController(PosComercioService posComercioService) {
        this.posComercioService = posComercioService;
    }

    @GetMapping
    public ResponseEntity<List<PosComercio>> obtenerTodos() {
        List<PosComercio> posComercios = posComercioService.obtenerTodos();
        return ResponseEntity.ok(posComercios);
    }

    @GetMapping("/{codigoPos}/{tipo}")
    public ResponseEntity<PosComercio> obtenerPorId(@PathVariable String codigoPos, @PathVariable String tipo) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            PosComercio posComercio = posComercioService.obtenerPorId(id);
            return ResponseEntity.ok(posComercio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PosComercio> crear(@RequestBody PosComercio posComercio) {
        try {
            PosComercio createdPos = posComercioService.crear(posComercio);
            return ResponseEntity.ok(createdPos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{codigoPos}/{tipo}/activar")
    public ResponseEntity<Void> activarPOS(@PathVariable String codigoPos, @PathVariable String tipo) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.activarPOS(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{codigoPos}/{tipo}/inactivar")
    public ResponseEntity<Void> inactivarPOS(@PathVariable String codigoPos, @PathVariable String tipo) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.inactivarPOS(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigoPos}/{tipo}/ultimo-uso")
    public ResponseEntity<Void> actualizarUltimoUso(
            @PathVariable String codigoPos, 
            @PathVariable String tipo,
            @RequestParam LocalDate fechaUltimoUso) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.actualizarUltimoUso(id, fechaUltimoUso);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{codigoPos}/{tipo}/comercio/{nuevoCodigoComercio}")
    public ResponseEntity<Void> cambiarComercioAsociado(
            @PathVariable String codigoPos, 
            @PathVariable String tipo,
            @PathVariable Integer nuevoCodigoComercio) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.cambiarComercioAsociado(id, nuevoCodigoComercio);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}