package ec.edu.espe.gateway.seguridad.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.seguridad.model.SeguridadProcesador;
import ec.edu.espe.gateway.seguridad.repository.SeguridadProcesadorRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SeguridadProcesadorService {

    private final SeguridadProcesadorRepository repository;

    public SeguridadProcesadorService(SeguridadProcesadorRepository repository) {
        this.repository = repository;
    }

    public SeguridadProcesador createProcesador(SeguridadProcesador procesador) {
        procesador.setFechaActualizacion(LocalDate.now());
        return repository.save(procesador);
    }

    public Optional<SeguridadProcesador> getProcesadorById(Integer id) {
        return repository.findById(id);
    }

    public List<SeguridadProcesador> getAllProcesadores() {
        return repository.findAll();
    }

    public SeguridadProcesador updateProcesador(Integer id, SeguridadProcesador updatedProcesadorDetails) {
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

    public SeguridadProcesador deactivateProcesador(Integer id) {
        return repository.findById(id)
                .map(procesadorExistente -> {
                    procesadorExistente.setEstado("INA"); // Cambiar el estado a "Inactivo"
                    procesadorExistente.setFechaActualizacion(LocalDate.now()); // Actualizar la fecha de actualización
                    return repository.save(procesadorExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Procesador con ID " + id + " no encontrado."));
    }

    public void deleteById(Integer id) {
        SeguridadProcesador procesador = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Procesador con ID " + id + " no encontrado."));

        if ("INA".equals(procesador.getEstado())) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar un Procesador con estado diferente a 'INA'.");
        }
    }
}