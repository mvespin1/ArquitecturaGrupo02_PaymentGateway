package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.comercio.services.PosComercioService;
import java.util.List;
import ec.edu.espe.gateway.comercio.controller.mapper.ConfiguracionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ec.edu.espe.gateway.exception.InvalidDataException;
import ec.edu.espe.gateway.exception.NotFoundException;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
@RestController
@RequestMapping("/v1/pos-comercio")
public class PosComercioController {

    private final PosComercioService posComercioService;
    private static final Logger logger = LoggerFactory.getLogger(PosComercioController.class);

    public PosComercioController(
            PosComercioService posComercioService,
            ConfiguracionMapper configuracionMapper) {
        this.posComercioService = posComercioService;
    }

    @GetMapping
    public ResponseEntity<List<PosComercio>> obtenerTodos() {
        logger.info("Obteniendo todos los POS de comercio");
        List<PosComercio> posComercios = posComercioService.obtenerTodos();
        return ResponseEntity.ok(posComercios);
    }

    @PostMapping
    public ResponseEntity<String> crear(@RequestBody PosComercio posComercio) {
        try {
            logger.info("Creando un nuevo POS de comercio: {}", posComercio);
            posComercioService.crear(posComercio);
            return ResponseEntity.ok().build();
        } catch (InvalidDataException e) {
            logger.warn("Error al crear POS de comercio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Se utilziaran botones
    @PutMapping("/{codigoPos}/{tipo}/activar")
    public ResponseEntity<String> activarPOS(@PathVariable String codigoPos, @PathVariable String tipo) {
        try {
            logger.info("Activando POS de comercio con código {} y tipo {}", codigoPos, tipo);
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.activarPOS(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException | IllegalStateException e) {
            logger.warn("Error al activar POS de comercio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Se utilziaran botones
    @PutMapping("/{codigoPos}/{tipo}/inactivar")
    public ResponseEntity<String> inactivarPOS(@PathVariable String codigoPos, @PathVariable String tipo) {
        try {
            logger.info("Inactivando POS de comercio con código {} y tipo {}", codigoPos, tipo);
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.inactivarPOS(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            logger.error("POS de comercio no encontrado para inactivar: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}