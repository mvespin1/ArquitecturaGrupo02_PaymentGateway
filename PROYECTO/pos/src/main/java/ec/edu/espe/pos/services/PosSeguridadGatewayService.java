package ec.edu.espe.pos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.espe.pos.model.PosSeguridadGateway;
import ec.edu.espe.pos.repository.PosSeguridadGatewayRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PosSeguridadGatewayService {

    @Autowired
    private PosSeguridadGatewayRepository posSeguridadGatewayRepository;

    public Optional<PosSeguridadGateway> obtenerSeguridadPorCodigo(Integer codigo) {
        return posSeguridadGatewayRepository.findById(codigo);
    }

    public PosSeguridadGateway configurarSeguridad(Integer codigo, String clave, LocalDate fechaActivacion,
            String estado) {
        Optional<PosSeguridadGateway> seguridadExistente = posSeguridadGatewayRepository.findById(codigo);

        PosSeguridadGateway posSeguridadGateway;

        if (seguridadExistente.isPresent()) {
            posSeguridadGateway = seguridadExistente.get();
            posSeguridadGateway.setEstado(estado);
            posSeguridadGateway.setFechaActualizacion(LocalDate.now());
        } else {
            posSeguridadGateway = new PosSeguridadGateway(codigo);
            posSeguridadGateway.setClave(clave);
            posSeguridadGateway.setFechaActivacion(LocalDate.now());
            posSeguridadGateway.setFechaActualizacion(LocalDate.now());
            posSeguridadGateway.setEstado("ACT");
        }

        return posSeguridadGatewayRepository.save(posSeguridadGateway);
    }

    public PosSeguridadGateway activarSeguridad(Integer codigo) {
        Optional<PosSeguridadGateway> seguridadExistente = posSeguridadGatewayRepository.findById(codigo);

        if (seguridadExistente.isPresent()) {
            PosSeguridadGateway posSeguridadGateway = seguridadExistente.get();
            posSeguridadGateway.setEstado("ACT");
            posSeguridadGateway.setFechaActualizacion(LocalDate.now());
            return posSeguridadGatewayRepository.save(posSeguridadGateway);
        }

        return null;
    }

    public PosSeguridadGateway desactivarSeguridad(Integer codigo) {
        Optional<PosSeguridadGateway> seguridadExistente = posSeguridadGatewayRepository.findById(codigo);

        if (seguridadExistente.isPresent()) {
            PosSeguridadGateway posSeguridadGateway = seguridadExistente.get();
            posSeguridadGateway.setEstado("INA");
            posSeguridadGateway.setFechaActualizacion(LocalDate.now());
            return posSeguridadGatewayRepository.save(posSeguridadGateway);
        }

        return null;
    }

    
}
