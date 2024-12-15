package ec.edu.espe.pos.services;

import org.springframework.stereotype.Service;

import ec.edu.espe.pos.model.POSConfiguracion;
import ec.edu.espe.pos.repository.POSConfiguracionRepository;
import ec.edu.espe.pos.model.PosConfiguracionPK;

import java.time.LocalDate;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class POSConfiguracionService {

    private final POSConfiguracionRepository posConfiguracionRepository;

    public POSConfiguracionService(POSConfiguracionRepository posConfiguracionRepository) {
        this.posConfiguracionRepository = posConfiguracionRepository;
    }

    public List<POSConfiguracion> findAll() {
        return posConfiguracionRepository.findAll();
    }

    @Transactional
    public POSConfiguracion update(PosConfiguracionPK id, String codigoComercio, LocalDate fechaActivacion) {
        return posConfiguracionRepository.findById(id)
                .map(configExistente -> {
                    configExistente.setCodigoComercio(codigoComercio);
                    configExistente.setFechaActivacion(fechaActivacion);
                    return posConfiguracionRepository.save(configExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "Configuración con ID [" + id.getModelo() + ", " + id.getCodigo()+ "] no encontrada."));
    }

    public Optional<POSConfiguracion> findById(PosConfiguracionPK id) {
        return posConfiguracionRepository.findById(id);
    }

    @Transactional
    public POSConfiguracion save(POSConfiguracion posConfiguracion) {
        return posConfiguracionRepository.save(posConfiguracion);
    }

    @Transactional
    public void deleteById(PosConfiguracionPK id) {
        posConfiguracionRepository.deleteById(id);
    }
}
