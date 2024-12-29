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

    public SeguridadGatewayService(SeguridadGatewayRepository repository) {
        this.repository = repository;
    }

    public SeguridadGateway createGateway(SeguridadGateway gateway) {
        gateway.setFechaCreacion(LocalDate.now());
        return repository.save(gateway);
    }

    public Optional<SeguridadGateway> getGatewayById(Integer id) {
        return repository.findById(id);
    }

    public List<SeguridadGateway> getAllGateways() {
        return repository.findAll();
    }

    public SeguridadGateway updateGateway(Integer id, SeguridadGateway gatewayDetails) {
        return repository.findById(id)
                .map(gatewayExistente -> {
                    gatewayExistente.setClave(gatewayDetails.getClave());
                    gatewayExistente.setFechaActivacion(gatewayDetails.getFechaActivacion());
                    gatewayExistente.setEstado(gatewayDetails.getEstado());
                    return repository.save(gatewayExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Gateway con ID " + id + " no encontrado."));
    }

    public void deleteById(Integer id) {
        SeguridadGateway gateway = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gateway con ID " + id + " no encontrado."));

        if ("INA".equals(gateway.getEstado())) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar un Gateway con estado diferente a 'INA'.");
        }
    }
}
