package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.services.ComercioService;
import java.util.List;

// implementar logs con import org.slf4j.Logger; y import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
@RestController
@RequestMapping("/v1/comercios")
public class ComercioController {

    private final ComercioService comercioService;

    public ComercioController(ComercioService comercioService) {
        this.comercioService = comercioService;
    }

    @GetMapping
    public ResponseEntity<List<Comercio>> obtenerTodos() {
        try {
            List<Comercio> comercios = comercioService.obtenerTodos();
            return ResponseEntity.ok(comercios);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> registrarComercio(@RequestBody Comercio comercio) {
        try {
            comercioService.registrarComercio(comercio);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Toca actualizar este endpoint a (http://localhost:8082/v1/comercios/1/activar)
    @PutMapping("/{codigo}/estado")
    public ResponseEntity<Void> actualizarEstado(@PathVariable Integer codigo, @RequestParam String nuevoEstado) {
        try {
            comercioService.actualizarEstado(codigo, nuevoEstado);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
