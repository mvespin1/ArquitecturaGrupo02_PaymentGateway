package ec.edu.espe.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.pos.model.PosSeguridadGateway;
import ec.edu.espe.pos.services.PosSeguridadGatewayService;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/seguridad-gateway")
public class PosSeguridadGatewayController {

    @Autowired
    private PosSeguridadGatewayService posSeguridadGatewayService;

    @GetMapping("/{codigo}")
    public ResponseEntity<PosSeguridadGateway> obtenerSeguridad(@PathVariable Integer codigo) {
        Optional<PosSeguridadGateway> seguridad = posSeguridadGatewayService.obtenerSeguridadPorCodigo(codigo);

        if (seguridad.isPresent()) {
            return ResponseEntity.ok(seguridad.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{codigo}")
    public ResponseEntity<PosSeguridadGateway> configurarSeguridad(
            @PathVariable Integer codigo,
            @RequestParam String clave,
            @RequestParam LocalDate fechaActivacion) {

        PosSeguridadGateway posSeguridadGateway = posSeguridadGatewayService.configurarSeguridad(
                codigo, clave, fechaActivacion, "ACT");

        return ResponseEntity.ok(posSeguridadGateway);
    }

    @PatchMapping("/activar/{codigo}")
    public ResponseEntity<PosSeguridadGateway> activarSeguridad(@PathVariable Integer codigo) {
        PosSeguridadGateway posSeguridadGateway = posSeguridadGatewayService.activarSeguridad(codigo);

        if (posSeguridadGateway != null) {
            return ResponseEntity.ok(posSeguridadGateway);
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/desactivar/{codigo}")
    public ResponseEntity<PosSeguridadGateway> desactivarSeguridad(@PathVariable Integer codigo) {
        PosSeguridadGateway posSeguridadGateway = posSeguridadGatewayService.desactivarSeguridad(codigo);

        if (posSeguridadGateway != null) {
            return ResponseEntity.ok(posSeguridadGateway);
        }

        return ResponseEntity.notFound().build();
    }
}
