package ec.edu.espe.gateway.seguridad.services;

import ec.edu.espe.gateway.seguridad.model.SeguridadMarca;
import ec.edu.espe.gateway.seguridad.repository.SeguridadMarcaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SeguridadMarcaService {

    private final SeguridadMarcaRepository repository;

    public SeguridadMarcaService(SeguridadMarcaRepository repository) {
        this.repository = repository;
    }

    public SeguridadMarca createMarca(SeguridadMarca marca) {
        marca.setFechaActualizacion(LocalDate.now());
        return repository.save(marca);
    }

    public Optional<SeguridadMarca> getMarcaById(String marca) {
        return repository.findById(marca);
    }

    public List<SeguridadMarca> getAllMarcas() {
        return repository.findAll();
    }

    public SeguridadMarca updateMarca(String marcaId, SeguridadMarca updatedMarcaDetails) {
        return repository.findById(marcaId)
                .map(marcaExistente -> {
                    marcaExistente.setClave(updatedMarcaDetails.getClave());
                    marcaExistente.setFechaActualizacion(LocalDate.now());
                    return repository.save(marcaExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + marcaId + " no encontrada."));
    }

    public void deleteById(String marcaId) {
        repository.findById(marcaId)
                .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + marcaId + " no encontrada."));
        
        repository.deleteById(marcaId);
    }
    
}
