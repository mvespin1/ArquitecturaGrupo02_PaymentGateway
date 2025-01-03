package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.SeguridadGateway;
import ec.edu.espe.pos.repository.SeguridadGatewayRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import jakarta.annotation.PostConstruct;

@Service
@Transactional
@EnableScheduling
public class SeguridadGatewayService {

    private final SeguridadGatewayRepository seguridadGatewayRepository;

    public SeguridadGatewayService(SeguridadGatewayRepository seguridadGatewayRepository) {
        this.seguridadGatewayRepository = seguridadGatewayRepository;
    }

    public String generarClaveSegura() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            return Base64.getEncoder().encodeToString(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la clave segura: " + e.getMessage());
        }
    }

    public void guardarClaveEnBaseDeDatos() {
        SeguridadGateway nuevaClave = new SeguridadGateway();
        String claveGenerada = generarClaveSegura();
        nuevaClave.setClave(claveGenerada);
        nuevaClave.setFechaActivacion(LocalDate.now());
        nuevaClave.setFechaActualizacion(LocalDateTime.now());
        nuevaClave.setEstado("ACT");
        seguridadGatewayRepository.save(nuevaClave);
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

    @Scheduled(cron = "0 0 0 */20 * *")
    public void generarClaveAutomaticamente() {
        try {
            SeguridadGateway claveActual = obtenerClaveActiva();
            if (claveActual != null) {
                LocalDateTime fechaUltimaActualizacion = claveActual.getFechaActualizacion();
                if (fechaUltimaActualizacion.plusDays(20).isAfter(LocalDateTime.now())) {
                    return;
                }
                claveActual.setEstado("INA");
                seguridadGatewayRepository.save(claveActual);
            }

            guardarClaveEnBaseDeDatos();
        } catch (Exception e) {
            System.err.println("Error al generar clave automáticamente: " + e.getMessage());
        }
    }

    public boolean requiereActualizacionClave(SeguridadGateway clave) {
        if (clave == null) return true;
        LocalDateTime fechaUltimaActualizacion = clave.getFechaActualizacion();
        return fechaUltimaActualizacion.plusDays(20).isBefore(LocalDateTime.now());
    }

    @PostConstruct
    public void inicializarClave() {
        try {
            // Verificar si ya existe una clave activa
            boolean existeClaveActiva = seguridadGatewayRepository.findByEstado("ACT")
                    .stream()
                    .findFirst()
                    .isPresent();

            // Si no existe clave activa, crear una nueva
            if (!existeClaveActiva) {
                guardarClaveEnBaseDeDatos();
                System.out.println("Primera clave de seguridad generada exitosamente");
            }
        } catch (Exception e) {
            System.err.println("Error al inicializar la primera clave: " + e.getMessage());
        }
    }
}  
