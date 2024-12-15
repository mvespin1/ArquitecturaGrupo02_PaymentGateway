package ec.edu.espe.gateway.seguridad.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.seguridad.model.GtwSeguridadGateway;
import ec.edu.espe.gateway.seguridad.repository.GtwSeguridadGatewayRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GtwSeguridadGatewayService {

    private final GtwSeguridadGatewayRepository repository;

    public GtwSeguridadGatewayService(GtwSeguridadGatewayRepository repository) {
        this.repository = repository;
    }

    public GtwSeguridadGateway createGateway(GtwSeguridadGateway gateway) {
        gateway.setFechaCreacion(LocalDate.now());
        return repository.save(gateway);
    }

    public Optional<GtwSeguridadGateway> getGatewayById(Integer id) {
        return repository.findById(id);
    }

    public List<GtwSeguridadGateway> getAllGateways() {
        return repository.findAll();
    }

    public GtwSeguridadGateway updateGateway(Integer id, GtwSeguridadGateway gatewayDetails) {
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
        GtwSeguridadGateway gateway = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gateway con ID " + id + " no encontrado."));

        if ("INA".equals(gateway.getEstado())) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar un Gateway con estado diferente a 'INA'.");
        }
    }
}
