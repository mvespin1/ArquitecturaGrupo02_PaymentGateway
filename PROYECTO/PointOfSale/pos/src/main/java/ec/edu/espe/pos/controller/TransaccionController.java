package ec.edu.espe.pos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.pos.service.TransaccionService;
import ec.edu.espe.pos.dto.ActualizacionEstadoDTO;
import ec.edu.espe.pos.model.Transaccion;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin(origins = "http://localhost:3000")
public class TransaccionController {
    private static final Logger log = LoggerFactory.getLogger(TransaccionController.class);
    private final TransaccionService transaccionService;

    public static final String ESTADO_AUTORIZADO = "AUT";
    public static final String ESTADO_RECHAZADO = "REC";

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping("/{codigoUnicoTransaccion}/estado")
    public ResponseEntity<Map<String, String>> consultarEstado(
            @PathVariable String codigoUnicoTransaccion) {
        log.info("Consultando estado de transacción: {}", codigoUnicoTransaccion);
        try {
            Transaccion transaccion = transaccionService.obtenerPorCodigoUnico(codigoUnicoTransaccion);
            Map<String, String> response = new HashMap<>();
            response.put("estado", transaccion.getEstado());
            response.put("mensaje", transaccion.getDetalle());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al consultar estado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/actualizar-estado")
    public ResponseEntity<Void> actualizarEstado(@RequestBody ActualizacionEstadoDTO actualizacion) {
        log.info("Recibiendo actualización de estado desde Gateway: {}", actualizacion);
        try {
            transaccionService.actualizarEstadoTransaccion(actualizacion);
            
            // Retornar código según el estado
            if (ESTADO_AUTORIZADO.equals(actualizacion.getEstado())) {
                return ResponseEntity.status(201).build();
            } else if (ESTADO_RECHAZADO.equals(actualizacion.getEstado())) {
                return ResponseEntity.status(400).build();
            } else {
                return ResponseEntity.status(201).build();
            }
        } catch (Exception e) {
            log.error("Error al actualizar estado: {}", e.getMessage());
            return ResponseEntity.status(400).build();
        }
    }
} 