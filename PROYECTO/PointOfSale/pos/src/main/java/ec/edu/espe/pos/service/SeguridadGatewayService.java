package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.SeguridadGateway;
import ec.edu.espe.pos.repository.SeguridadGatewayRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

@Service
public class SeguridadGatewayService {

    private final RestTemplate restTemplate;
    private final SeguridadGatewayRepository seguridadGatewayRepository;
    private static final Logger log = LoggerFactory.getLogger(SeguridadGatewayService.class);

    
    private static final String SEGURIDAD_URL = "http://localhost:8083/api/seguridad/clave-activa";

    public SeguridadGatewayService(RestTemplate restTemplate, 
                                  SeguridadGatewayRepository seguridadGatewayRepository) {
        this.restTemplate = restTemplate;
        this.seguridadGatewayRepository = seguridadGatewayRepository;
    }

    @PostConstruct
    @Transactional
    public void inicializarClave() {
        log.info("Inicializando clave al arrancar el servicio");
        actualizarClaveDesdeGateway();
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void actualizarClaveDesdeGateway() {
        try {
            log.info("Intentando actualizar clave desde: {}", SEGURIDAD_URL);
            
            ResponseEntity<SeguridadGateway> response = restTemplate.getForEntity(
                SEGURIDAD_URL,
                SeguridadGateway.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                SeguridadGateway nuevaClave = response.getBody();
                
                if (nuevaClave != null && nuevaClave.getClave() != null && 
                    !seguridadGatewayRepository.existsByClave(nuevaClave.getClave())) {
                    
                    log.info("Actualizando claves activas a inactivas");
                    seguridadGatewayRepository.updateEstadoForActiveCodes("INA");
                    
                    // Crear una nueva instancia para evitar problemas de versión
                    SeguridadGateway claveAGuardar = new SeguridadGateway();
                    claveAGuardar.setClave(nuevaClave.getClave());
                    claveAGuardar.setEstado("ACT");
                    claveAGuardar.setFechaActivacion(nuevaClave.getFechaActivacion());
                    claveAGuardar.setFechaActualizacion(LocalDateTime.now());
                    
                    log.info("Guardando nueva clave: {}", claveAGuardar.getClave());
                    seguridadGatewayRepository.save(claveAGuardar);
                    log.info("Clave actualizada exitosamente");
                }
            }
        } catch (Exception e) {
            log.error("Error al actualizar clave desde gateway: ", e);
        }
    }

    public String encriptarInformacion(String informacion, String clave) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey secretKey = reconstruirClaveDesdeBase64(clave);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encriptado = cipher.doFinal(informacion.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encriptado);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar la información: " + e.getMessage());
        }
    }

    public String desencriptarInformacion(String informacionEncriptada, String clave) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey secretKey = reconstruirClaveDesdeBase64(clave);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] desencriptado = cipher.doFinal(Base64.getDecoder().decode(informacionEncriptada));
            return new String(desencriptado, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar la información: " + e.getMessage());
        }
    }

    private SecretKey reconstruirClaveDesdeBase64(String claveBase64) {
        try {
            // Asegurarse de que la clave tenga un padding Base64 correcto
            String claveAjustada = claveBase64;
            while (claveAjustada.length() % 4 != 0) {
                claveAjustada += "=";
            }
            
            byte[] claveBytes = Base64.getDecoder().decode(claveAjustada);
            // Asegurarse de que la clave tenga exactamente 16 bytes para AES-128
            byte[] clave16Bytes = new byte[16];
            System.arraycopy(claveBytes, 0, clave16Bytes, 0, Math.min(claveBytes.length, 16));
            
            return new javax.crypto.spec.SecretKeySpec(clave16Bytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Error al reconstruir la clave desde Base64: " + e.getMessage());
        }
    }

    public SeguridadGateway obtenerClaveActiva() {
        return seguridadGatewayRepository.findByEstado("ACT")
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró una clave activa"));
    }
}  
