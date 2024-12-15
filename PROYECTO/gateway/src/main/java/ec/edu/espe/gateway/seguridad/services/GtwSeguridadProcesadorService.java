package ec.edu.espe.gateway.seguridad.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.seguridad.model.GtwSeguridadProcesador;
import ec.edu.espe.gateway.seguridad.repository.GtwSeguridadProcesadorRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GtwSeguridadProcesadorService {

    private final GtwSeguridadProcesadorRepository repository;

    public GtwSeguridadProcesadorService(GtwSeguridadProcesadorRepository repository) {
        this.repository = repository;
    }

    public GtwSeguridadProcesador createProcesador(GtwSeguridadProcesador procesador) {
        procesador.setFechaActualizacion(LocalDate.now());
        return repository.save(procesador);
    }

    public Optional<GtwSeguridadProcesador> getProcesadorById(Integer id) {
        return repository.findById(id);
    }

    public List<GtwSeguridadProcesador> getAllProcesadores() {
        return repository.findAll();
    }

    public GtwSeguridadProcesador updateProcesador(Integer id, GtwSeguridadProcesador updatedProcesadorDetails) {
        return repository.findById(id)
                .map(procesadorExistente -> {
                    procesadorExistente.setClave(updatedProcesadorDetails.getClave());
                    procesadorExistente.setFechaActualizacion(LocalDate.now());
                    procesadorExistente.setFechaActivacion(updatedProcesadorDetails.getFechaActivacion());
                    procesadorExistente.setEstado(updatedProcesadorDetails.getEstado());
                    return repository.save(procesadorExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Procesador con ID " + id + " no encontrado."));
    }

    public GtwSeguridadProcesador deactivateProcesador(Integer id) {
        return repository.findById(id)
                .map(procesadorExistente -> {
                    procesadorExistente.setEstado("INA"); // Cambiar el estado a "Inactivo"
                    procesadorExistente.setFechaActualizacion(LocalDate.now()); // Actualizar la fecha de actualización
                    return repository.save(procesadorExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Procesador con ID " + id + " no encontrado."));
    }

    public void deleteById(Integer id) {
        GtwSeguridadProcesador procesador = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Procesador con ID " + id + " no encontrado."));

        if ("INA".equals(procesador.getEstado())) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar un Procesador con estado diferente a 'INA'.");
        }
    }
}