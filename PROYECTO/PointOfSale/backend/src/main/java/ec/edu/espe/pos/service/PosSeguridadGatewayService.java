package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.PosSeguridadGateway;
import ec.edu.espe.pos.repository.PosSeguridadGatewayRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PosSeguridadGatewayService {

    private final PosSeguridadGatewayRepository posSeguridadGatewayRepository;

    public PosSeguridadGatewayService(PosSeguridadGatewayRepository posSeguridadGatewayRepository) {
        this.posSeguridadGatewayRepository = posSeguridadGatewayRepository;
    }

    public List<PosSeguridadGateway> obtenerTodos() {
        return this.posSeguridadGatewayRepository.findAll();
    }

    public Optional<PosSeguridadGateway> obtenerPorId(Integer id) {
        return this.posSeguridadGatewayRepository.findById(id);
    }

    public PosSeguridadGateway crear(PosSeguridadGateway posSeguridadGateway) {
        return this.posSeguridadGatewayRepository.save(posSeguridadGateway);
    }

    public PosSeguridadGateway actualizar(Integer id, String clave, String estado) {
        Optional<PosSeguridadGateway> gatewayOpt = this.posSeguridadGatewayRepository.findById(id);
        if (gatewayOpt.isPresent()) {
            PosSeguridadGateway gateway = gatewayOpt.get();
            gateway.setClave(clave);
            gateway.setEstado(estado);
            gateway.setFechaActualizacion(LocalDate.now());
            return this.posSeguridadGatewayRepository.save(gateway);
        }
        throw new RuntimeException("No se encontró el gateway con el ID proporcionado");
    }
} 