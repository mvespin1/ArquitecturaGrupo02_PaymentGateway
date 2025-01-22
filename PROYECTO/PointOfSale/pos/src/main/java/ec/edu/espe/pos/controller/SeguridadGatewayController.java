package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.SeguridadGateway;
import ec.edu.espe.pos.service.SeguridadGatewayService;
import ec.edu.espe.pos.exception.NotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "https://frontend-blond-theta.vercel.app")
@RestController
@RequestMapping("/api/v1/seguridad-gateway")
@RequiredArgsConstructor
public class SeguridadGatewayController {

    private static final Logger log = LoggerFactory.getLogger(SeguridadGatewayController.class);
    private final SeguridadGatewayService seguridadGatewayService;
    
    @GetMapping("/clave-activa")
    public ResponseEntity<Object> obtenerClaveActiva() {
        log.info("Obteniendo clave activa");
        try {
            SeguridadGateway claveActiva = seguridadGatewayService.obtenerClaveActiva();
            log.info("Clave activa obtenida exitosamente");
            return ResponseEntity.ok(claveActiva);
        } catch (NotFoundException e) {
            log.error("Clave activa no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al obtener clave activa: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(crearRespuestaError("Error interno", "Error al obtener clave activa"));
        }
    }

    @PostMapping("/encriptar")
    public ResponseEntity<Object> encriptarDatos(@RequestBody Map<String, String> datos) {
        log.info("Iniciando proceso de encriptación de datos");
        try {
            validarDatosEncriptacion(datos);
            
            String informacion = datos.get("informacion");
            String clave = datos.get("clave");
            
            log.debug("Procesando encriptación con clave de longitud: {}", clave.length());
            String datosEncriptados = seguridadGatewayService.encriptarInformacion(informacion, clave);
            
            log.info("Datos encriptados exitosamente");
            return ResponseEntity.ok(Map.of("datosEncriptados", datosEncriptados));
        } catch (IllegalArgumentException e) {
            log.error("Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(crearRespuestaError("Error de validación", e.getMessage()));
        } catch (Exception e) {
            log.error("Error al encriptar datos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(crearRespuestaError("Error de encriptación", "Error al procesar los datos"));
        }
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(404).body(crearRespuestaError("No encontrado", e.getMessage()));
    }

    private void validarDatosEncriptacion(Map<String, String> datos) {
        if (datos.get("informacion") == null || datos.get("clave") == null) {
            throw new IllegalArgumentException("Los campos 'informacion' y 'clave' son requeridos");
        }
    }

    private Map<String, String> crearRespuestaError(String tipo, String mensaje) {
        Map<String, String> response = new HashMap<>();
        response.put("error", tipo);
        response.put("mensaje", mensaje);
        return response;
    }
}