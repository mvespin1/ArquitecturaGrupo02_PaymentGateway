package ec.edu.espe.gateway.seguridad.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.seguridad.model.SeguridadGateway;
import ec.edu.espe.gateway.seguridad.repository.SeguridadGatewayRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
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
}
