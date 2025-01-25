package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.comercio.services.PosComercioService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import ec.edu.espe.gateway.comercio.controller.mapper.ConfiguracionMapper;

// implementar logs con import org.slf4j.Logger; y import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
@RestController
@RequestMapping("/v1/pos-comercio")
public class PosComercioController {

    private final PosComercioService posComercioService;

    public PosComercioController(
            PosComercioService posComercioService,
            ConfiguracionMapper configuracionMapper) {
        this.posComercioService = posComercioService;
    }

    @GetMapping
    public ResponseEntity<List<PosComercio>> obtenerTodos() {
        List<PosComercio> posComercios = posComercioService.obtenerTodos();
        return ResponseEntity.ok(posComercios);
    }

    @PostMapping
    public ResponseEntity<PosComercio> crear(@RequestBody PosComercio posComercio) {
        try {
            PosComercio createdPos = posComercioService.crear(posComercio);
            return ResponseEntity.ok(createdPos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Se utilziaran botones
    @PutMapping("/{codigoPos}/{tipo}/activar")
    public ResponseEntity<Void> activarPOS(@PathVariable String codigoPos, @PathVariable String tipo) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.activarPOS(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Se utilziaran botones
    @PutMapping("/{codigoPos}/{tipo}/inactivar")
    public ResponseEntity<Void> inactivarPOS(@PathVariable String codigoPos, @PathVariable String tipo) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.inactivarPOS(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}