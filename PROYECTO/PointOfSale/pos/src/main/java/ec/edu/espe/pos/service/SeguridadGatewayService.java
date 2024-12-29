package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.SeguridadGateway;
import ec.edu.espe.pos.repository.SeguridadGatewayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SeguridadGatewayService {

    private final SeguridadGatewayRepository seguridadGatewayRepository;

    public SeguridadGatewayService(SeguridadGatewayRepository seguridadGatewayRepository) {
        this.seguridadGatewayRepository = seguridadGatewayRepository;
    }

    public List<SeguridadGateway> obtenerTodos() {
        return this.seguridadGatewayRepository.findAll();
    }

    public Optional<SeguridadGateway> obtenerPorId(Integer id) {
        return this.seguridadGatewayRepository.findById(id);
    }

    public SeguridadGateway crear(SeguridadGateway seguridadGateway) {
        return this.seguridadGatewayRepository.save(seguridadGateway);
    }

    public SeguridadGateway actualizar(Integer id, String clave, String estado) {
        Optional<SeguridadGateway> gatewayOpt = this.seguridadGatewayRepository.findById(id);
        if (gatewayOpt.isPresent()) {
            SeguridadGateway gateway = gatewayOpt.get();
            gateway.setClave(clave);
            gateway.setEstado(estado);
            gateway.setFechaActualizacion(LocalDate.now());
            return this.seguridadGatewayRepository.save(gateway);
        }
        throw new RuntimeException("No se encontró el gateway con el ID proporcionado");
    }
} 