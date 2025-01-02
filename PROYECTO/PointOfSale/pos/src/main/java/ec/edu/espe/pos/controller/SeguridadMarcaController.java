package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.SeguridadMarca;
import ec.edu.espe.pos.service.SeguridadMarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seguridad-marca")
public class SeguridadMarcaController {

    private final SeguridadMarcaService seguridadMarcaService;

    public SeguridadMarcaController(SeguridadMarcaService seguridadMarcaService) {
        this.seguridadMarcaService = seguridadMarcaService;
    }

    @GetMapping("/{marca}")
    public ResponseEntity<Object> obtenerPorMarca(@PathVariable String marca) {
        try {
            return ResponseEntity.ok(this.seguridadMarcaService.obtenerPorMarca(marca));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/actualizar-automatico")
    public ResponseEntity<Object> procesarActualizacionAutomatica(@RequestBody SeguridadMarca seguridadMarca) {
        try {
            return ResponseEntity.ok(this.seguridadMarcaService.procesarActualizacionAutomatica(seguridadMarca));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al procesar la actualización automática: " + e.getMessage());
        }
    }
} 