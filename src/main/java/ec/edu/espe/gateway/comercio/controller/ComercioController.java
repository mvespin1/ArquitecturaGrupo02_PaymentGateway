package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.services.ComercioService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ec.edu.espe.gateway.exception.DuplicateException;
import ec.edu.espe.gateway.exception.InvalidDataException;
import ec.edu.espe.gateway.exception.NotFoundException;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
@RestController
@RequestMapping("/v1/comercios")
public class ComercioController {

    private final ComercioService comercioService;
    private static final Logger logger = LoggerFactory.getLogger(ComercioController.class);

    public ComercioController(ComercioService comercioService) {
        this.comercioService = comercioService;
    }

    @GetMapping
    public ResponseEntity<List<Comercio>> obtenerTodos() {
        try {
            logger.info("Obteniendo todos los comercios");
            List<Comercio> comercios = comercioService.obtenerTodos();
            return ResponseEntity.ok(comercios);
        } catch (Exception e) {
            logger.error("Error al obtener todos los comercios", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> registrarComercio(@RequestBody Comercio comercio) {
        try {
            logger.info("Registrando un nuevo comercio: {}", comercio);
            comercioService.registrarComercio(comercio);
            return ResponseEntity.ok().build();
        } catch (DuplicateException | InvalidDataException e) {
            logger.warn("Error al registrar comercio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{codigo}/estado")
    public ResponseEntity<String> actualizarEstado(@PathVariable Integer codigo, @RequestParam String nuevoEstado) {
        try {
            logger.info("Actualizando estado del comercio con código {}: nuevo estado {}", codigo, nuevoEstado);
            comercioService.actualizarEstado(codigo, nuevoEstado);
            return ResponseEntity.ok().build();
        } catch (NotFoundException | IllegalStateException e) {
            logger.warn("Error al actualizar estado del comercio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}