package ec.edu.espe.gateway.seguridad.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.seguridad.model.SeguridadGateway;
import ec.edu.espe.gateway.seguridad.repository.SeguridadGatewayRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import javax.crypto.KeyGenerator;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import java.util.Base64;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;

@Service
@EnableScheduling
public class SeguridadGatewayService {

    private final SeguridadGatewayRepository repository;

    public static final String ESTADO_ACTIVO = "ACT";
    public static final String ESTADO_INACTIVO = "INA";
    public static final String ESTADO_PENDIENTE = "PEN";

    public SeguridadGatewayService(SeguridadGatewayRepository repository) {
        this.repository = repository;
    }

    public List<SeguridadGateway> getGatewaysByEstadoActivo() {
        try {
            return repository.findByEstado(ESTADO_ACTIVO);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo obtener los gateways activos. Motivo: " + ex.getMessage());
        }
    }

    public SeguridadGateway createGateway(SeguridadGateway gateway) {
        try {
            gateway.setFechaCreacion(LocalDate.now());
            gateway.setEstado(ESTADO_PENDIENTE);
            if (gateway.getClave().length() > 128) {
                throw new IllegalArgumentException("La clave no puede exceder los 128 caracteres.");
            }
            return repository.save(gateway);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el gateway. Motivo: " + ex.getMessage());
        }
    }

    public Optional<SeguridadGateway> getGatewayById(Integer id) {
        try {
            return repository.findById(id);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo obtener el gateway con ID " + id + ". Motivo: " + ex.getMessage());
        }
    }

    public SeguridadGateway updateGateway(Integer id, SeguridadGateway gatewayDetails) {
        try {
            return repository.findById(id)
                    .map(gatewayExistente -> {
                        gatewayExistente.setClave(gatewayDetails.getClave());
                        if (gatewayExistente.getClave().length() > 128) {
                            throw new IllegalArgumentException("La clave no puede exceder los 128 caracteres.");
                        }
                        gatewayExistente.setEstado(gatewayDetails.getEstado());
                        if (ESTADO_ACTIVO.equals(gatewayDetails.getEstado())
                                && gatewayExistente.getFechaActivacion() == null) {
                            gatewayExistente.setFechaActivacion(LocalDate.now());
                        }
                        return repository.save(gatewayExistente);
                    })
                    .orElseThrow(() -> new EntityNotFoundException("Gateway con ID " + id + " no encontrado."));
        } catch (EntityNotFoundException ex) {
            throw ex; // Dejar que las excepciones específicas sean manejadas por el controlador.
        } catch (Exception ex) {
            throw new RuntimeException(
                    "No se pudo actualizar el gateway con ID " + id + ". Motivo: " + ex.getMessage());
        }
    }

    public void deleteById(Integer id) {
        try {
            SeguridadGateway gateway = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Gateway con ID " + id + " no encontrado."));

            if (ESTADO_INACTIVO.equals(gateway.getEstado())) {
                repository.deleteById(id);
            } else {
                throw new IllegalStateException("No se puede eliminar un Gateway con estado diferente a 'INA'.");
            }
        } catch (EntityNotFoundException | IllegalStateException ex) {
            throw ex; // Estas excepciones serán manejadas directamente por el controlador.
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo eliminar el gateway con ID " + id + ". Motivo: " + ex.getMessage());
        }
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

    @Scheduled(cron = "0 0 0 */20 * *")
    public void generarClaveAutomaticamente() {
        try {
            List<SeguridadGateway> clavesActivas = repository.findByEstado(ESTADO_ACTIVO);
            if (!clavesActivas.isEmpty()) {
                SeguridadGateway claveActual = clavesActivas.get(0);
                if (claveActual.getFechaActivacion().plusDays(20).isAfter(LocalDate.now())) {
                    return;
                }
                claveActual.setEstado(ESTADO_INACTIVO);
                repository.save(claveActual);
            }

            SeguridadGateway nuevaClave = new SeguridadGateway();
            nuevaClave.setClave(generarClaveSegura());
            nuevaClave.setFechaCreacion(LocalDate.now());
            nuevaClave.setFechaActivacion(LocalDate.now());
            nuevaClave.setEstado(ESTADO_ACTIVO);
            repository.save(nuevaClave);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar clave automáticamente: " + e.getMessage());
        }
    }

    @PostConstruct
    public void inicializarClave() {
        try {
            if (repository.findByEstado(ESTADO_ACTIVO).isEmpty()) {
                SeguridadGateway nuevaClave = new SeguridadGateway();
                nuevaClave.setClave(generarClaveSegura());
                nuevaClave.setFechaCreacion(LocalDate.now());
                nuevaClave.setFechaActivacion(LocalDate.now());
                nuevaClave.setEstado(ESTADO_ACTIVO);
                repository.save(nuevaClave);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar la primera clave: " + e.getMessage());
        }
    }

    public SeguridadGateway obtenerClaveActiva() {
        return repository.findByEstado(ESTADO_ACTIVO)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró una clave activa"));
    }
}
