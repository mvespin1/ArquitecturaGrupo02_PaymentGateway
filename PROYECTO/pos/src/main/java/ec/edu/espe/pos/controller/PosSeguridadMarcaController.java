package ec.edu.espe.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.pos.model.PosSeguridadMarca;
import ec.edu.espe.pos.services.PosSeguridadMarcaService;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/api/seguridad-marcas")
public class PosSeguridadMarcaController {

    @Autowired
    private PosSeguridadMarcaService posSeguridadMarcaService;

    @GetMapping("/{marca}")
    public ResponseEntity<PosSeguridadMarca> obtenerSeguridadMarca(@PathVariable String marca) {
        Optional<PosSeguridadMarca> seguridadMarca = posSeguridadMarcaService.obtenerSeguridadPorMarca(marca);
        if (seguridadMarca.isPresent()) {
            return ResponseEntity.ok(seguridadMarca.get());
        } else {
            throw new EntityNotFoundException("La marca " + marca + " no tiene configuración de seguridad.");
        }
    }

    @PostMapping("/{marca}")
    public ResponseEntity<PosSeguridadMarca> configurarSeguridadMarca(
            @PathVariable String marca, @RequestParam String clave) {
        PosSeguridadMarca configuracion = posSeguridadMarcaService.configurarSeguridadMarca(marca, clave);
        return ResponseEntity.ok(configuracion);
    }

    @DeleteMapping("/{marca}")
    public ResponseEntity<Void> eliminarSeguridadMarca(@PathVariable String marca) {
        posSeguridadMarcaService.eliminarSeguridadMarca(marca);
        return ResponseEntity.noContent().build();
    }
}
