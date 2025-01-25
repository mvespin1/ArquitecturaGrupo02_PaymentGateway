package ec.edu.espe.gateway.facturacion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.gateway.facturacion.services.FacturacionService;
import ec.edu.espe.gateway.exception.NotFoundException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/v1/facturacion")
public class FacturacionComercioController {

    private static final Logger logger = LoggerFactory.getLogger(FacturacionComercioController.class);

    private final FacturacionService facturacionService;

    public FacturacionComercioController(FacturacionService facturacionService) {
        this.facturacionService = facturacionService;
    }

    // Este endpoint se lo va a utilziar para actualizar el estado de la factura cuando
    // el banco nos confirme que se hizo el pago del comercio al gateway
    @PostMapping("/marcar-pagado/{codigo}")
    public ResponseEntity<String> marcarComoPagado(@PathVariable Integer codigo) {
        try {
            logger.info("Intentando marcar como pagada la facturación con código: {}", codigo);
            facturacionService.marcarComoPagado(codigo);
            return ResponseEntity.ok("Facturación marcada como pagada exitosamente");
        } catch (NotFoundException e) {
            logger.error("No se encontró la facturación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la facturación: " + e.getMessage());
        } catch (IllegalStateException e) {
            logger.warn("Error de estado al marcar como pagada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de estado: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error al marcar la facturación como pagada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al marcar la facturación como pagada: " + e.getMessage());
        }
    }
}
