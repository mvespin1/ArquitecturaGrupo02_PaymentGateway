package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.PosTransaccion;
import ec.edu.espe.pos.services.PosTransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pos/transacciones")
@CrossOrigin(origins = "*")
public class PosTransaccionController {

    private final PosTransaccionService posTransaccionService;

    public PosTransaccionController(PosTransaccionService posTransaccionService) {
        this.posTransaccionService = posTransaccionService;
    }

    @GetMapping
    public ResponseEntity<List<PosTransaccion>> obtenerTodasLasTransacciones() {
        return ResponseEntity.ok(posTransaccionService.obtenerTodasLasTransacciones());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<PosTransaccion> obtenerTransaccion(@PathVariable Integer codigo) {
        return posTransaccionService.obtenerTransaccionPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PosTransaccion> crearTransaccion(@Valid @RequestBody PosTransaccion posTransaccion) {
        try {
            PosTransaccion nuevaTransaccion = posTransaccionService.crearTransaccion(posTransaccion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTransaccion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<PosTransaccion> actualizarTransaccion(
            @PathVariable Integer codigo,
            @Valid @RequestBody PosTransaccion posTransaccion) {
        try {
            PosTransaccion transaccionActualizada = posTransaccionService.actualizarTransaccion(codigo, posTransaccion);
            return ResponseEntity.ok(transaccionActualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminarTransaccion(@PathVariable Integer codigo) {
        try {
            posTransaccionService.eliminarTransaccion(codigo);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
