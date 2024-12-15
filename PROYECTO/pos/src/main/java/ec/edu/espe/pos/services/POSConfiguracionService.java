package ec.edu.espe.pos.services;

import org.springframework.stereotype.Service;

import ec.edu.espe.pos.model.POSConfiguracion;
import ec.edu.espe.pos.repository.POSConfiguracionRepository;
import ec.edu.espe.pos.model.PosConfiguracionPK;

import java.time.LocalDateTime;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;


@Service
public class POSConfiguracionService {

    private final POSConfiguracionRepository posConfiguracionRepository;

    public POSConfiguracionService(POSConfiguracionRepository posConfiguracionRepository) {
        this.posConfiguracionRepository = posConfiguracionRepository;
    }

    public List<POSConfiguracion> findAll() {
        return posConfiguracionRepository.findAll();
    }

    public POSConfiguracion update(PosConfiguracionPK id, String codigoComercio, LocalDateTime fechaActivacion) {
        return posConfiguracionRepository.findById(id)
                .map(configExistente -> {
                    configExistente.setCodigoComercio(codigoComercio);
                    configExistente.setFechaActivacion(fechaActivacion.toLocalDate());
                    return posConfiguracionRepository.save(configExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "Configuración con ID [" + id.getModelo() + ", " + id.getCodigo()+ "] no encontrada."));
    }

    public Optional<POSConfiguracion> findById(PosConfiguracionPK id) {
        return posConfiguracionRepository.findById(id);
    }

    public POSConfiguracion save(POSConfiguracion posConfiguracion) {
        return posConfiguracionRepository.save(posConfiguracion);
    }

    public void deleteById(PosConfiguracionPK id) {
        posConfiguracionRepository.deleteById(id);
    }
}
