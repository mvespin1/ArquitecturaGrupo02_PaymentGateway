package ec.edu.espe.gateway.seguridad.controller;

import ec.edu.espe.gateway.seguridad.model.SeguridadMarca;
import ec.edu.espe.gateway.seguridad.services.SeguridadMarcaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import ec.edu.espe.gateway.exception.InvalidDataException;

@RestController
@RequestMapping("/v1/seguridad-marca")
public class SeguridadMarcaController {

    private static final Logger logger = LoggerFactory.getLogger(SeguridadMarcaController.class);

    private final SeguridadMarcaService marcaService;

    public SeguridadMarcaController(SeguridadMarcaService marcaService) {
        this.marcaService = marcaService;
    }

    @PostMapping("/recibir-clave")
    public ResponseEntity<String> recibirClave(@RequestBody SeguridadMarca nuevaClave) {
        try {
            logger.info("Recibiendo nueva clave para marca: {}", nuevaClave.getMarca());
            marcaService.guardarClave(nuevaClave);
            return ResponseEntity.ok("Clave de marca recibida y guardada exitosamente");
        } catch (InvalidDataException e) {
            logger.error("Datos inválidos al recibir clave de marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al recibir clave de marca: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recibir clave de marca");
        }
    }
} 