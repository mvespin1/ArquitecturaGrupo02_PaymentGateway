package ec.edu.espe.gateway.comercio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.comercio.services.PosComercioService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import ec.edu.espe.gateway.comercio.controller.dto.Configuracion;
import ec.edu.espe.gateway.comercio.controller.mapper.ConfiguracionMapper;

@RestController
@RequestMapping("/v1/pos-comercio")
public class PosComercioController {

    private final PosComercioService posComercioService;
    private final ConfiguracionMapper configuracionMapper;

    public PosComercioController(
            PosComercioService posComercioService,
            ConfiguracionMapper configuracionMapper) {
        this.posComercioService = posComercioService;
        this.configuracionMapper = configuracionMapper;
    }

    @GetMapping
    public ResponseEntity<List<PosComercio>> obtenerTodos() {
        List<PosComercio> posComercios = posComercioService.obtenerTodos();
        return ResponseEntity.ok(posComercios);
    }

    @GetMapping("/{codigoPos}/{tipo}")
    public ResponseEntity<PosComercio> obtenerPorId(@PathVariable String codigoPos, @PathVariable String tipo) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            PosComercio posComercio = posComercioService.obtenerPorId(id);
            return ResponseEntity.ok(posComercio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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

    @PutMapping("/{codigoPos}/{tipo}/ultimo-uso")
    public ResponseEntity<Void> actualizarUltimoUso(
            @PathVariable String codigoPos, 
            @PathVariable String tipo,
            @RequestParam LocalDateTime fechaUltimoUso) {
        try {
            posComercioService.actualizarUltimoUso(codigoPos, tipo, fechaUltimoUso);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{codigoPos}/{tipo}/comercio/{nuevoCodigoComercio}")
    public ResponseEntity<Void> cambiarComercioAsociado(
            @PathVariable String codigoPos, 
            @PathVariable String tipo,
            @PathVariable Integer nuevoCodigoComercio) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            posComercioService.cambiarComercioAsociado(id, nuevoCodigoComercio);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/configuracion/{codigoPos}/{tipo}")
    public ResponseEntity<Configuracion> obtenerConfiguracion(
            @PathVariable String codigoPos, 
            @PathVariable String tipo) {
        try {
            PosComercioPK id = new PosComercioPK(codigoPos, tipo);
            PosComercio posComercio = posComercioService.obtenerPorId(id);
            Configuracion configuracion = configuracionMapper.toDTO(posComercio);
            configuracion.setCodigoComercio(posComercio.getComercio().getCodigo());
            return ResponseEntity.ok(configuracion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/configuracion")
    public ResponseEntity<Void> procesarConfiguracion(@RequestBody Configuracion configuracion) {
        try {
            PosComercio posComercio = configuracionMapper.toModel(configuracion);
            posComercioService.procesarConfiguracion(posComercio);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}