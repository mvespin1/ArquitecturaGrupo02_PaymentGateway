package ec.edu.espe.pos.controller;

import ec.edu.espe.pos.model.SeguridadGateway;
import ec.edu.espe.pos.service.SeguridadGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "https://frontend-blond-theta.vercel.app")
@RestController
@RequestMapping("/api/seguridad-gateway")
public class SeguridadGatewayController {

    private final SeguridadGatewayService seguridadGatewayService;

    public SeguridadGatewayController(SeguridadGatewayService seguridadGatewayService) {
        this.seguridadGatewayService = seguridadGatewayService;
    }
    
    @GetMapping("/clave-activa")
    public ResponseEntity<Object> obtenerClaveActiva() {
        try {
            SeguridadGateway claveActiva = this.seguridadGatewayService.obtenerClaveActiva();
            return ResponseEntity.ok(claveActiva);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al obtener clave activa: " + e.getMessage());
        }
    }

    @PostMapping("/encriptar")
    public ResponseEntity<Object> encriptarDatos(@RequestBody Map<String, String> datos) {
        try {
            // Validación de datos de entrada
            if (datos.get("informacion") == null || datos.get("clave") == null) {
                return ResponseEntity.badRequest()
                        .body("Los campos 'informacion' y 'clave' son requeridos");
            }

            String informacion = datos.get("informacion");
            String clave = datos.get("clave");
            
            // Log para depuración
            System.out.println("Información a encriptar: " + informacion);
            System.out.println("Clave utilizada: " + clave);

            String datosEncriptados = this.seguridadGatewayService.encriptarInformacion(informacion, clave);
            return ResponseEntity.ok(Map.of("datosEncriptados", datosEncriptados));
        } catch (Exception e) {
            e.printStackTrace(); // Para ver el stack trace completo en los logs
            return ResponseEntity.internalServerError()
                    .body("Error al encriptar datos: " + e.getMessage() + " | Causa: " + e.getCause());
        }
    }
}