package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.POSConfiguracion;
import ec.edu.espe.pos.model.PosConfiguracionPK;
import ec.edu.espe.pos.service.POSConfiguracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pos-configuracion")
public class POSConfiguracionController {

    private final POSConfiguracionService posConfiguracionService;

    public POSConfiguracionController(POSConfiguracionService posConfiguracionService) {
        this.posConfiguracionService = posConfiguracionService;
    }

    @GetMapping
    public ResponseEntity<List<POSConfiguracion>> listarTodos() {
        return ResponseEntity.ok(this.posConfiguracionService.obtenerTodos());
    }

    @GetMapping("/{codigo}/{modelo}")
    public ResponseEntity<POSConfiguracion> obtenerPorId(
            @PathVariable String codigo,
            @PathVariable String modelo) {
        return this.posConfiguracionService.obtenerPorId(new PosConfiguracionPK(codigo, modelo))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<POSConfiguracion> crear(@RequestBody POSConfiguracion posConfiguracion) {
        return ResponseEntity.ok(this.posConfiguracionService.crear(posConfiguracion));
    }

    @PatchMapping("/{codigo}/{modelo}")
    public ResponseEntity<POSConfiguracion> actualizarCampos(
            @PathVariable String codigo,
            @PathVariable String modelo,
            @RequestParam String codigoComercio,
            @RequestParam LocalDate fechaActivacion) {
        try {
            POSConfiguracion actualizado = this.posConfiguracionService.actualizar(
                    new PosConfiguracionPK(codigo, modelo),
                    codigoComercio,
                    fechaActivacion);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 