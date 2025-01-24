package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.SeguridadMarca;
import ec.edu.espe.pos.service.SeguridadMarcaService;
import ec.edu.espe.pos.exception.NotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/v1/seguridad-marca")
@RequiredArgsConstructor
public class SeguridadMarcaController {

    private static final Logger log = LoggerFactory.getLogger(SeguridadMarcaController.class);
    private final SeguridadMarcaService seguridadMarcaService;

    @GetMapping("/{marca}")
    public ResponseEntity<Object> obtenerPorMarca(@PathVariable String marca) {
        log.info("Buscando seguridad marca: {}", marca);
        try {
            SeguridadMarca seguridadMarca = seguridadMarcaService.obtenerPorMarca(marca);
            log.info("Seguridad marca encontrada");
            return ResponseEntity.ok(seguridadMarca);
        } catch (NotFoundException e) {
            log.error("Seguridad marca no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener seguridad marca: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(crearRespuestaError("Error interno", "Error al obtener la seguridad marca"));
        }
    }

    @PostMapping("/actualizar-automatico")
    public ResponseEntity<Object> procesarActualizacionAutomatica(@RequestBody SeguridadMarca seguridadMarca) {
        log.info("Procesando actualización automática para marca: {}", seguridadMarca.getMarca());
        try {
            SeguridadMarca marcaActualizada = seguridadMarcaService.procesarActualizacionAutomatica(seguridadMarca);
            log.info("Actualización automática procesada exitosamente");
            return ResponseEntity.ok(marcaActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al procesar actualización automática: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(crearRespuestaError("Error interno", "Error al procesar la actualización automática"));
        }
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(404).body(crearRespuestaError("No encontrado", e.getMessage()));
    }

    private Map<String, String> crearRespuestaError(String tipo, String mensaje) {
        Map<String, String> response = new HashMap<>();
        response.put("error", tipo);
        response.put("mensaje", mensaje);
        return response;
    }
} 