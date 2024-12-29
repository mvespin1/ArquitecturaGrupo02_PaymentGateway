package ec.edu.espe.gateway.comision.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.repository.ComisionRepository;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ComisionService {

    private final ComisionRepository comisionRepository;

    public ComisionService(ComisionRepository comisionRepository) {
        this.comisionRepository = comisionRepository;
    }

    // Método para encontrar todas las comisiones
    public List<Comision> findAll() {
        return comisionRepository.findAll();
    }

    // Método para actualizar los campos permitidos
    public Comision update(int codigo, Comision comision) {
        return comisionRepository.findById(codigo)
                .map(comisionExistente -> {
                    comisionExistente.setMontoBase(comision.getMontoBase());
                    comisionExistente.setTransaccionesBase(comision.getTransaccionesBase());
                    comisionExistente.setManejaSegmentos(comision.getManejaSegmentos());
                    return comisionRepository.save(comisionExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Comisión con código " + codigo + " no encontrada."));
    }

    public Comision save(Comision comision) {
        if (!"POR".equals(comision.getTipo()) && !"FIJ".equals(comision.getTipo())) {
            throw new IllegalArgumentException("El tipo de comisión debe ser 'POR' o 'FIJ'.");
        }
        return comisionRepository.save(comision);
    }

    public Optional<Comision> findById(int codigo) {
        return comisionRepository.findById(codigo);
    }

    public void deleteById(int codigo) {
        throw new UnsupportedOperationException("No se permite eliminar comisiones.");
    }
}