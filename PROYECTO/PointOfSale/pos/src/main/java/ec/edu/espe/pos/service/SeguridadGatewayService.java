package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.SeguridadGateway;
import ec.edu.espe.pos.repository.SeguridadGatewayRepository;
import ec.edu.espe.pos.exception.NotFoundException;
import ec.edu.espe.pos.exception.SecurityException;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeguridadGatewayService {

    private static final Logger log = LoggerFactory.getLogger(SeguridadGatewayService.class);
    private static final String ENTITY_NAME = "Seguridad Gateway";
    private static final String SEGURIDAD_URL = "http://18.190.153.55/api/seguridad/clave-activa";
    private static final String ESTADO_ACTIVO = "ACT";
    private static final String ESTADO_INACTIVO = "INA";

    private final RestTemplate restTemplate;
    private final SeguridadGatewayRepository seguridadGatewayRepository;

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
            ResponseEntity<SeguridadGateway> response = obtenerClaveDesdeGateway();
            procesarRespuestaGateway(response);
        } catch (Exception e) {
            log.error("Error al actualizar clave desde gateway: ", e);
            throw new SecurityException("actualización de clave", "Gateway");
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
            log.error("Error al encriptar información: {}", e.getMessage());
            throw new SecurityException("encriptación", "Proceso de encriptación");
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
            log.error("Error al desencriptar información: {}", e.getMessage());
            throw new SecurityException("desencriptación", "Proceso de desencriptación");
        }
    }

    public SeguridadGateway obtenerClaveActiva() {
        return seguridadGatewayRepository.findByEstado(ESTADO_ACTIVO)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ESTADO_ACTIVO, ENTITY_NAME));
    }

    private ResponseEntity<SeguridadGateway> obtenerClaveDesdeGateway() {
        try {
            return restTemplate.getForEntity(SEGURIDAD_URL, SeguridadGateway.class);
        } catch (Exception e) {
            log.error("Error al obtener clave desde gateway: {}", e.getMessage());
            throw new SecurityException(SEGURIDAD_URL, "Conexión con Gateway");
        }
    }

    private void procesarRespuestaGateway(ResponseEntity<SeguridadGateway> response) {
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            SeguridadGateway nuevaClave = response.getBody();
            if (esClaveValida(nuevaClave)) {
                actualizarClaves(nuevaClave);
            } else {
                throw new SecurityException(
                    nuevaClave != null ? nuevaClave.getClave() : "null", 
                    "Validación de clave");
            }
        } else {
            throw new SecurityException(
                response.getStatusCode().toString(), 
                "Respuesta del Gateway");
        }
    }

    private boolean esClaveValida(SeguridadGateway nuevaClave) {
        return nuevaClave != null && 
               nuevaClave.getClave() != null && 
               !seguridadGatewayRepository.existsByClave(nuevaClave.getClave());
    }

    private void actualizarClaves(SeguridadGateway nuevaClave) {
        log.info("Actualizando claves activas a inactivas");
        seguridadGatewayRepository.updateEstadoForActiveCodes(ESTADO_INACTIVO);
        
        SeguridadGateway claveAGuardar = crearNuevaClave(nuevaClave);
        log.info("Guardando nueva clave: {}", claveAGuardar.getClave());
        seguridadGatewayRepository.save(claveAGuardar);
        log.info("Clave actualizada exitosamente");
    }

    private SeguridadGateway crearNuevaClave(SeguridadGateway nuevaClave) {
        return SeguridadGateway.builder()
                .clave(nuevaClave.getClave())
                .estado(ESTADO_ACTIVO)
                .fechaActivacion(nuevaClave.getFechaActivacion())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    private SecretKey reconstruirClaveDesdeBase64(String claveBase64) {
        try {
            String claveAjustada = ajustarPaddingBase64(claveBase64);
            byte[] claveBytes = Base64.getDecoder().decode(claveAjustada);
            byte[] clave16Bytes = ajustarTamanoClave(claveBytes);
            return new javax.crypto.spec.SecretKeySpec(clave16Bytes, "AES");
        } catch (Exception e) {
            log.error("Error al reconstruir clave desde Base64: {}", e.getMessage());
            throw new SecurityException(claveBase64, "Reconstrucción de clave Base64");
        }
    }

    private String ajustarPaddingBase64(String claveBase64) {
        String claveAjustada = claveBase64;
        while (claveAjustada.length() % 4 != 0) {
            claveAjustada += "=";
        }
        return claveAjustada;
    }

    private byte[] ajustarTamanoClave(byte[] claveBytes) {
        byte[] clave16Bytes = new byte[16];
        System.arraycopy(claveBytes, 0, clave16Bytes, 0, Math.min(claveBytes.length, 16));
        return clave16Bytes;
    }
}  
