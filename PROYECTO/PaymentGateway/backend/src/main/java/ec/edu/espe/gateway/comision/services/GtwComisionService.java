package ec.edu.espe.gateway.comision.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.comision.model.GtwComision;
import ec.edu.espe.gateway.comision.repository.GtwComisionRepository;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class GtwComisionService {

    private final GtwComisionRepository gtwComisionRepository;

    public GtwComisionService(GtwComisionRepository gtwComisionRepository) {
        this.gtwComisionRepository = gtwComisionRepository;
    }

    // Método para encontrar todas las comisiones
    public List<GtwComision> findAll() {
        return gtwComisionRepository.findAll();
    }

    // Método para actualizar los campos permitidos
    public GtwComision update(int codigo, GtwComision gtwComision) {
        return gtwComisionRepository.findById(codigo)
                .map(comisionExistente -> {
                    comisionExistente.setMontoBase(gtwComision.getMontoBase());
                    comisionExistente.setTransaccionesBase(gtwComision.getTransaccionesBase());
                    comisionExistente.setManejaSegmentos(gtwComision.getManejaSegmentos());
                    return gtwComisionRepository.save(comisionExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Comisión con código " + codigo + " no encontrada."));
    }

    public GtwComision save(GtwComision gtwComision) {
        if (!"POR".equals(gtwComision.getTipo()) && !"FIJ".equals(gtwComision.getTipo())) {
            throw new IllegalArgumentException("El tipo de comisión debe ser 'POR' o 'FIJ'.");
        }
        return gtwComisionRepository.save(gtwComision);
    }

    public Optional<GtwComision> findById(int codigo) {
        return gtwComisionRepository.findById(codigo);
    }

    public void deleteById(int codigo) {
        throw new UnsupportedOperationException("No se permite eliminar comisiones.");
    }
}