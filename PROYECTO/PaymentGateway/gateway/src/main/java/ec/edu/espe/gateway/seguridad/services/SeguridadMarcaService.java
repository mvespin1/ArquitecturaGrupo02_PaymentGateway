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

    private static final String REGEX_MARCA = "^[a-zA-Z]{1,4}$";
    private static final String REGEX_CLAVE = "^[a-zA-Z0-9]{1,128}$";
    private static final String ERROR_MARCA_INVALIDA = "La marca debe ser solo letras y de máximo 4 caracteres.";
    private static final String ERROR_CLAVE_INVALIDA = "La clave debe ser alfanumérica y tener máximo 128 caracteres.";

    public SeguridadMarcaService(SeguridadMarcaRepository repository) {
        this.repository = repository;
    }

    public SeguridadMarca createMarca(SeguridadMarca marca) {
        if (marca.getMarca() == null || !marca.getMarca().matches(REGEX_MARCA)) {
            throw new IllegalArgumentException(ERROR_MARCA_INVALIDA);
        }

        if (marca.getClave() == null || !marca.getClave().matches(REGEX_CLAVE)) {
            throw new IllegalArgumentException(ERROR_CLAVE_INVALIDA);
        }

        marca.setFechaActualizacion(LocalDate.now());

        try {
            return repository.save(marca);
        } catch (Exception ex) {
            throw new RuntimeException("Error al crear la marca. Motivo: " + ex.getMessage());
        }
    }

    public Optional<SeguridadMarca> getMarcaById(String marca) {
        try {
            return repository.findById(marca);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener la marca con ID " + marca + ". Motivo: " + ex.getMessage());
        }
    }

    public List<SeguridadMarca> getAllMarcas() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener la lista de marcas. Motivo: " + ex.getMessage());
        }
    }

    public SeguridadMarca updateMarca(String marcaId, SeguridadMarca updatedMarcaDetails) {
        if (updatedMarcaDetails.getMarca() == null || !updatedMarcaDetails.getMarca().matches(REGEX_MARCA)) {
            throw new IllegalArgumentException(ERROR_MARCA_INVALIDA);
        }

        if (updatedMarcaDetails.getClave() == null || !updatedMarcaDetails.getClave().matches(REGEX_CLAVE)) {
            throw new IllegalArgumentException(ERROR_CLAVE_INVALIDA);
        }

        updatedMarcaDetails.setFechaActualizacion(LocalDate.now());

        try {
            return repository.findById(marcaId)
                    .map(marcaExistente -> {
                        marcaExistente.setClave(updatedMarcaDetails.getClave());
                        marcaExistente.setFechaActualizacion(LocalDate.now());
                        return repository.save(marcaExistente);
                    })
                    .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + marcaId + " no encontrada."));
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Error al actualizar la marca con ID " + marcaId + ". Motivo: " + ex.getMessage());
        }
    }

    public void deleteById(String marcaId) {
        try {
            repository.findById(marcaId)
                    .orElseThrow(() -> new EntityNotFoundException("Marca con ID " + marcaId + " no encontrada."));
            repository.deleteById(marcaId);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar la marca con ID " + marcaId + ". Motivo: " + ex.getMessage());
        }
    }
}
