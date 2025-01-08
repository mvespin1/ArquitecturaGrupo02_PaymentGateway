package ec.edu.espe.pos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.pos.service.TransaccionService;
import ec.edu.espe.pos.dto.ActualizacionEstadoDTO;

@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin(origins = "http://localhost:3000")
public class TransaccionController {
    private static final Logger log = LoggerFactory.getLogger(TransaccionController.class);
    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping("/actualizar-estado")
    public ResponseEntity<Void> actualizarEstado(@RequestBody ActualizacionEstadoDTO actualizacion) {
        log.info("Recibiendo actualización de estado desde Gateway: {}", actualizacion);
        try {
            transaccionService.actualizarEstadoTransaccion(actualizacion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error al actualizar estado: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
} 