package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadProcesador;
import ec.edu.espe.gateway.seguridad.services.SeguridadProcesadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ec.edu.espe.gateway.exception.NotFoundException;
import ec.edu.espe.gateway.exception.InvalidDataException;

@RestController
@RequestMapping("/v1/seguridad-procesador")
public class SeguridadProcesadorController {

    private static final Logger logger = LoggerFactory.getLogger(SeguridadProcesadorController.class);

    private final SeguridadProcesadorService procesadorService;

    public SeguridadProcesadorController(SeguridadProcesadorService procesadorService) {
        this.procesadorService = procesadorService;
    }

    @PostMapping("/recibir-clave")
    public ResponseEntity<String> recibirClave(@RequestBody SeguridadProcesador nuevaClave) {
        try {
            logger.info("Recibiendo nueva clave para procesador con código: {}", nuevaClave.getCodigo());
            procesadorService.guardarClave(nuevaClave);
            return ResponseEntity.ok("Clave de procesador recibida y guardada exitosamente");
        } catch (InvalidDataException e) {
            logger.error("Datos inválidos al recibir clave de procesador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al recibir clave de procesador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recibir clave de procesador");
        }
    }

    @PutMapping("/actualizar-estado/{codigo}")
    public ResponseEntity<String> actualizarEstadoClave(@PathVariable Integer codigo, @RequestParam String nuevoEstado) {
        try {
            logger.info("Actualizando estado de la clave para procesador con código {}: nuevo estado {}", codigo, nuevoEstado);
            procesadorService.actualizarEstadoClave(codigo, nuevoEstado);
            return ResponseEntity.ok("Estado de la clave de procesador actualizado exitosamente");
        } catch (NotFoundException e) {
            logger.error("Clave de procesador no encontrada para actualizar estado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidDataException e) {
            logger.error("Datos inválidos al actualizar estado de clave de procesador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar estado de clave de procesador: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar estado de clave de procesador");
        }
    }
}