package ec.edu.espe.gateway.comercio.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.comercio.repository.GtwPosComercioRepository;
import ec.edu.espe.gateway.comercio.model.GtwPosComercio;
import ec.edu.espe.gateway.comercio.model.GtwPosComercioPK;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GtwPosComercioService {

    private final GtwPosComercioRepository gtwPosComercioRepository;

    public GtwPosComercioService(GtwPosComercioRepository gtwPosComercioRepository) {
        this.gtwPosComercioRepository = gtwPosComercioRepository;
    }

    public List<GtwPosComercio> findAll() {
        return gtwPosComercioRepository.findAll();
    }

    public Optional<GtwPosComercio> findById(GtwPosComercioPK id) {
        return gtwPosComercioRepository.findById(id);
    }

    public GtwPosComercio save(GtwPosComercio gtwPosComercio) {
        return gtwPosComercioRepository.save(gtwPosComercio);
    }

    public GtwPosComercio update(GtwPosComercioPK id, GtwPosComercio gtwPosComercio) {
        return gtwPosComercioRepository.findById(id)
                .map(posComercioExistente -> {
                    posComercioExistente.setEstado(gtwPosComercio.getEstado());
                    posComercioExistente.setUltimoUso(gtwPosComercio.getUltimoUso());
                    return gtwPosComercioRepository.save(posComercioExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("PosComercio con código " + id + " no encontrado."));
    }

    /**
     * Eliminación lógica: cambia el estado a INA.
     */
    public void deleteById(GtwPosComercioPK id) {
        GtwPosComercio posComercio = gtwPosComercioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PosComercio con ID " + id + " no encontrado."));
        
        posComercio.setEstado("INA"); // Cambiar estado a inactivo
        gtwPosComercioRepository.save(posComercio); // Guardar cambio
    }
}
