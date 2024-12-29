package ec.edu.espe.gateway.comercio.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.comercio.repository.PosComercioRepository;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PosComercioService {

    private final PosComercioRepository posComercioRepository;

    public PosComercioService(PosComercioRepository posComercioRepository) {
        this.posComercioRepository = posComercioRepository;
    }

    public List<PosComercio> findAll() {
        return posComercioRepository.findAll();
    }

    public Optional<PosComercio> findById(PosComercioPK id) {
        return posComercioRepository.findById(id);
    }

    public PosComercio save(PosComercio posComercio) {
        return posComercioRepository.save(posComercio);
    }

    public PosComercio update(PosComercioPK id, PosComercio posComercio) {
        return posComercioRepository.findById(id)
                .map(posComercioExistente -> {
                    posComercioExistente.setEstado(posComercio.getEstado());
                    posComercioExistente.setUltimoUso(posComercio.getUltimoUso());
                    return posComercioRepository.save(posComercioExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("PosComercio con código " + id + " no encontrado."));
    }

    /**
     * Eliminación lógica: cambia el estado a INA.
     */
    public void deleteById(PosComercioPK id) {
        PosComercio posComercio = posComercioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PosComercio con ID " + id + " no encontrado."));
        
        posComercio.setEstado("INA"); // Cambiar estado a inactivo
        posComercioRepository.save(posComercio); // Guardar cambio
    }
}
