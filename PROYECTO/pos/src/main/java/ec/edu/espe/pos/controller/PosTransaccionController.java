package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.PosTransaccion;
import ec.edu.espe.pos.services.PosTransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/pos/transacciones")
public class PosTransaccionController {

    @Autowired
    private final PosTransaccionService posTransaccionService;

    public PosTransaccionController(PosTransaccionService posTransaccionService) {
        this.posTransaccionService = posTransaccionService;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<PosTransaccion> obtenerTransaccion(@PathVariable Integer codigo) {
        Optional<PosTransaccion> transaccion = posTransaccionService.obtenerTransaccionPorCodigo(codigo);
        if (transaccion.isPresent()) {
            return ResponseEntity.ok(transaccion.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PosTransaccion> crearOActualizarTransaccion(@RequestBody PosTransaccion posTransaccion) {
        PosTransaccion transaccionGuardada = posTransaccionService.crearOActualizarTransaccion(posTransaccion);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaccionGuardada);
    }

    @PatchMapping("/{codigo}")
    public ResponseEntity<PosTransaccion> actualizarEstado(@PathVariable Integer codigo,
            @RequestBody PosTransaccion posTransaccion) {
        Optional<PosTransaccion> transaccionExistente = posTransaccionService.obtenerTransaccionPorCodigo(codigo);

        if (transaccionExistente.isPresent()) {
            posTransaccion.setCodigo(codigo);
            PosTransaccion transaccionActualizada = posTransaccionService.crearOActualizarTransaccion(posTransaccion);
            return ResponseEntity.ok(transaccionActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
