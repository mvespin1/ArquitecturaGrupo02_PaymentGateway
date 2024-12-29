package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.Configuracion;
import ec.edu.espe.pos.model.ConfiguracionPK;
import ec.edu.espe.pos.service.ConfiguracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pos-configuracion")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(ConfiguracionService configuracionService) {
        this.configuracionService = configuracionService;
    }

    @GetMapping
    public ResponseEntity<List<Configuracion>> listarTodos() {
        return ResponseEntity.ok(this.configuracionService.obtenerTodos());
    }

    @GetMapping("/{codigo}/{modelo}")
    public ResponseEntity<Configuracion> obtenerPorId(
            @PathVariable String codigo,
            @PathVariable String modelo) {
        return this.configuracionService.obtenerPorId(new ConfiguracionPK(codigo, modelo))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Configuracion> crear(@RequestBody Configuracion configuracion) {
        return ResponseEntity.ok(this.configuracionService.crear(configuracion));
    }

    @PatchMapping("/{codigo}/{modelo}")
    public ResponseEntity<Configuracion> actualizarCampos(
            @PathVariable String codigo,
            @PathVariable String modelo,
            @RequestParam String codigoComercio,
            @RequestParam LocalDate fechaActivacion) {
        try {
            Configuracion actualizado = this.configuracionService.actualizar(
                    new ConfiguracionPK(codigo, modelo),
                    codigoComercio,
                    fechaActivacion);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
} 