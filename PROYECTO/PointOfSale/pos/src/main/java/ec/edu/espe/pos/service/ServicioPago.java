package ec.edu.espe.pos.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

@Service
@Transactional
public class ServicioPago {

    private static final String ALGORITMO_ENCRIPTACION = "AES";
    private static final SecretKey claveEncriptacion = generarClave();

    public void capturarDatosPago(String numeroTarjeta, String fechaVencimiento, String cvv, String nombreTarjeta,
            Double monto) {
        // Validar los datos ingresados
        if (numeroTarjeta == null || numeroTarjeta.length() != 16 || !numeroTarjeta.matches("\\d+")) {
            throw new IllegalArgumentException("Número de tarjeta inválido");
        }
        if (cvv == null || cvv.length() != 3 || !cvv.matches("\\d+")) {
            throw new IllegalArgumentException("CVV inválido");
        }
        if (fechaVencimiento == null || !validarFechaVencimiento(fechaVencimiento)) {
            throw new IllegalArgumentException("Fecha de vencimiento inválida o expirada");
        }
        if (nombreTarjeta == null || nombreTarjeta.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre en la tarjeta no puede estar vacío");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        // Encriptar los datos sensibles
        String numeroTarjetaEncriptado = encriptar(numeroTarjeta);
        String cvvEncriptado = encriptar(cvv);
        String nombreTarjetaEncriptado = encriptar(nombreTarjeta);

        // Simular el envío al Gateway de Pagos mediante un canal seguro
        procesarPago(numeroTarjetaEncriptado, fechaVencimiento, cvvEncriptado, nombreTarjetaEncriptado, monto);
    }

    private static SecretKey generarClave() {
        try {
            KeyGenerator generadorClave = KeyGenerator.getInstance(ALGORITMO_ENCRIPTACION);
            generadorClave.init(128);
            return generadorClave.generateKey();
        } catch (Exception ex) {
            throw new RuntimeException("Error al generar la clave de encriptación: " + ex.getMessage());
        }
    }

    private String encriptar(String datos) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO_ENCRIPTACION);
            cipher.init(Cipher.ENCRYPT_MODE, claveEncriptacion);
            byte[] datosEncriptados = cipher.doFinal(datos.getBytes());
            return Base64.getEncoder().encodeToString(datosEncriptados);
        } catch (Exception ex) {
            throw new RuntimeException("Error al encriptar los datos: " + ex.getMessage());
        }
    }

    private Boolean validarFechaVencimiento(String fechaVencimiento) {
        // Lógica para validar que la fecha de vencimiento sea válida y no esté expirada
        return true; // Implementar validación real
    }

    public void procesarPago(String numeroTarjetaEncriptado, String fechaVencimiento, String cvvEncriptado,
            String nombreTarjetaEncriptado, Double monto) {
        // Lógica para simular el envío al Gateway de Pagos a través de un canal seguro
        configurarCanalSeguro();
        System.out.println("Procesando pago en un canal seguro...");
        System.out.println("Datos encriptados enviados al Gateway.");
    }

    private void configurarCanalSeguro() {
        // Configurar TLS u otro protocolo seguro
        System.out.println("Canal TLS configurado.");
    }
}