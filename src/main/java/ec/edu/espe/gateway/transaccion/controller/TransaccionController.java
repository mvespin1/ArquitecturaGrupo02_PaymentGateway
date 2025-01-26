package ec.edu.espe.gateway.transaccion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.services.TransaccionService;
import ec.edu.espe.gateway.transaccion.services.RecurrenceService;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import jakarta.persistence.EntityNotFoundException;
@RestController
@RequestMapping("/v1/transacciones")
public class TransaccionController {

    private static final Logger log = LoggerFactory.getLogger(TransaccionController.class);
    private final TransaccionService transaccionService;
    private final TransaccionRepository transaccionRepository;

    public static final String ESTADO_AUTORIZADO = "AUT";
    public static final String ESTADO_RECHAZADO = "REC";

    public TransaccionController(TransaccionService transaccionService,
            TransaccionRepository transaccionRepository,
            RecurrenceService recurrenceService) {
        this.transaccionService = transaccionService;
        this.transaccionRepository = transaccionRepository;
    }

    @PostMapping("/sincronizar")
    public ResponseEntity<String> sincronizarTransaccion(@RequestBody Transaccion transaccion) {
        log.info("Recibiendo petición de sincronización desde POS");
        log.info("Datos de transacción recibidos: {}", transaccion);
        log.info("Comercio ID: {}", transaccion.getComercio().getCodigo());
        log.info("Facturación ID: {}", transaccion.getFacturacionComercio().getCodigo());

        try {
            transaccionService.procesarTransaccionPOS(transaccion);
            log.info("Sincronización completada exitosamente");

            // Obtener la transacción actualizada para acceder a su estado
            Transaccion transaccionActualizada = transaccionRepository.findById(transaccion.getCodigo())
                    .orElseThrow(() -> new EntityNotFoundException("Transacción no encontrada"));

            // Devolver el mensaje y código HTTP según el estado
            if (ESTADO_AUTORIZADO.equals(transaccionActualizada.getEstado())) {
                return ResponseEntity.status(201)
                        .body("Transacción aceptada");
            } else if (ESTADO_RECHAZADO.equals(transaccionActualizada.getEstado())) {
                return ResponseEntity.status(400)
                        .body("Transacción rechazada");
            } else {
                return ResponseEntity.status(201)
                        .body("Transacción en proceso de validación");
            }

        } catch (EntityNotFoundException e) {
            log.error("Entidad no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            log.error("Error inesperado al sincronizar: {}", e.getMessage());
            return ResponseEntity.status(400)
                    .body("Transacción rechazada");
        }
    }
}
