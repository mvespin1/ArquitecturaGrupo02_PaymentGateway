package ec.edu.espe.pos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.pos.service.TransaccionService;
import ec.edu.espe.pos.controller.dto.ActualizacionEstadoDTO;
import ec.edu.espe.pos.controller.dto.GatewayTransaccionDTO;
import ec.edu.espe.pos.controller.mapper.TransaccionMapper;
import ec.edu.espe.pos.model.Transaccion;
import ec.edu.espe.pos.exception.NotFoundException;

import java.util.Map;
import java.util.HashMap;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/transacciones")
@CrossOrigin(origins = "https://frontend-blond-theta.vercel.app")
@RequiredArgsConstructor
public class TransaccionController {
    
    private static final Logger log = LoggerFactory.getLogger(TransaccionController.class);
    private static final String ESTADO_AUTORIZADO = "AUT";
    private static final String ESTADO_RECHAZADO = "REC";

    private final TransaccionService transaccionService;
    private final TransaccionMapper mapper;

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
        } catch (NotFoundException e) {
            log.error("Transacción no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al consultar estado: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/actualizar-estado")
    public ResponseEntity<Void> actualizarEstado(@RequestBody ActualizacionEstadoDTO actualizacion) {
        log.info("Recibiendo actualización de estado desde Gateway: {}", actualizacion);
        try {
            transaccionService.actualizarEstadoTransaccion(actualizacion);
            
            if (ESTADO_AUTORIZADO.equals(actualizacion.getEstado())) {
                return ResponseEntity.status(201).build();
            } else if (ESTADO_RECHAZADO.equals(actualizacion.getEstado())) {
                return ResponseEntity.status(400).build();
            } else {
                return ResponseEntity.status(201).build();
            }
        } catch (NotFoundException e) {
            log.error("Transacción no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar estado: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "No encontrado");
        response.put("mensaje", e.getMessage());
        return ResponseEntity.status(404).body(response);
    }
} 