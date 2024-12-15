package ec.edu.espe.gateway.seguridad.services;

import ec.edu.espe.gateway.seguridad.model.GtwSeguridadMarca;
import ec.edu.espe.gateway.seguridad.repository.GtwSeguridadMarcaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GtwSeguridadMarcaService {

    private final GtwSeguridadMarcaRepository repository;

    public GtwSeguridadMarcaService(GtwSeguridadMarcaRepository repository) {
        this.repository = repository;
    }

    public GtwSeguridadMarca createMarca(GtwSeguridadMarca marca) {
        marca.setFechaActualizacion(LocalDate.now());
        return repository.save(marca);
    }

    public Optional<GtwSeguridadMarca> getMarcaById(String marca) {
        return repository.findById(marca);
    }

    public List<GtwSeguridadMarca> getAllMarcas() {
        return repository.findAll();
    }

    public GtwSeguridadMarca updateMarca(String marcaId, GtwSeguridadMarca updatedMarcaDetails) {
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
