package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.Configuracion;
import ec.edu.espe.pos.model.ConfiguracionPK;
import ec.edu.espe.pos.service.ConfiguracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        try {
            return ResponseEntity.ok(this.configuracionService.obtenerTodos());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{codigo}/{modelo}")
    public ResponseEntity<Configuracion> obtenerPorId(
            @PathVariable String codigo,
            @PathVariable String modelo) {
        try {
            return ResponseEntity.ok(this.configuracionService.obtenerPorId(new ConfiguracionPK(codigo, modelo)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody Configuracion configuracion) {
        try {
            return ResponseEntity.ok(this.configuracionService.crear(configuracion));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear la configuración: " + e.getMessage());
        }
    }

    @PatchMapping("/{codigo}/{modelo}/fecha-activacion")
    public ResponseEntity<Object> actualizarFechaActivacion(
            @PathVariable String codigo,
            @PathVariable String modelo,
            @RequestParam LocalDateTime nuevaFechaActivacion) {
        try {
            Configuracion actualizado = this.configuracionService.actualizarFechaActivacion(
                    new ConfiguracionPK(codigo, modelo),
                    nuevaFechaActivacion);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar la fecha de activación: " + e.getMessage());
        }
    }

    @PostMapping("/sincronizar")
    public ResponseEntity<Void> recibirConfiguracion(@RequestBody Configuracion configuracion) {
        try {
            configuracionService.crear(configuracion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 