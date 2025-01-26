package ec.edu.espe.gateway.seguridad.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.seguridad.model.SeguridadProcesador;
import ec.edu.espe.gateway.seguridad.repository.SeguridadProcesadorRepository;
import ec.edu.espe.gateway.exception.NotFoundException;
import ec.edu.espe.gateway.exception.InvalidDataException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SeguridadProcesadorService {

    private final SeguridadProcesadorRepository repository;
    public static final String ENTITY_NAME = "Procesador de Seguridad";

    private static final String ESTADO_PENDIENTE = "PEN";
    private static final String ESTADO_ACTIVO = "ACT";
    private static final String ESTADO_INACTIVO = "INA";
    private static final String REGEX_CLAVE = "^[a-zA-Z0-9]{1,128}$";
    private static final String ERROR_CLAVE_INVALIDA = "La clave debe ser alfanumérica y tener máximo 128 caracteres.";

    public SeguridadProcesadorService(SeguridadProcesadorRepository repository) {
        this.repository = repository;
    }

    public SeguridadProcesador createProcesador(SeguridadProcesador procesador) {
        try {
            validarClave(procesador);
            procesador.setEstado(ESTADO_PENDIENTE);
            procesador.setFechaActualizacion(LocalDateTime.now());
            return repository.save(procesador);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el procesador. Motivo: " + ex.getMessage());
        }
    }

    public Optional<SeguridadProcesador> getProcesadorById(Integer id) {
        try {
            SeguridadProcesador procesador = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    id.toString(), 
                    ENTITY_NAME
                ));
            return Optional.of(procesador);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener el procesador. Motivo: " + ex.getMessage());
        }
    }

    public List<SeguridadProcesador> getAllProcesadores() {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo obtener la lista de procesadores. Motivo: " + ex.getMessage());
        }
    }

    public SeguridadProcesador updateProcesador(Integer id, SeguridadProcesador updatedProcesadorDetails) {
        try {
            validarClave(updatedProcesadorDetails);
            return repository.findById(id)
                    .map(procesadorExistente -> {
                        procesadorExistente.setClave(updatedProcesadorDetails.getClave());
                        procesadorExistente.setFechaActualizacion(LocalDateTime.now());
                        procesadorExistente.setFechaActivacion(
                            updatedProcesadorDetails.getEstado().equals(ESTADO_ACTIVO)
                            ? LocalDate.now()
                            : procesadorExistente.getFechaActivacion()
                        );
                        procesadorExistente.setEstado(updatedProcesadorDetails.getEstado());
                        return repository.save(procesadorExistente);
                    })
                    .orElseThrow(() -> new NotFoundException(
                        id.toString(), 
                        ENTITY_NAME
                    ));
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar el procesador. Motivo: " + ex.getMessage());
        }
    }

    public SeguridadProcesador deactivateProcesador(Integer id) {
        try {
            return repository.findById(id)
                    .map(procesadorExistente -> {
                        procesadorExistente.setEstado(ESTADO_INACTIVO);
                        procesadorExistente.setFechaActualizacion(LocalDateTime.now());
                        return repository.save(procesadorExistente);
                    })
                    .orElseThrow(() -> new NotFoundException(
                        id.toString(), 
                        ENTITY_NAME
                    ));
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new RuntimeException("Error al desactivar el procesador. Motivo: " + ex.getMessage());
        }
    }

    public void deleteById(Integer id) {
        try {
            SeguridadProcesador procesador = repository.findById(id)
                    .orElseThrow(() -> new NotFoundException(
                        id.toString(), 
                        ENTITY_NAME
                    ));

            if (ESTADO_INACTIVO.equals(procesador.getEstado())) {
                repository.deleteById(id);
            } else {
                throw new IllegalStateException("No se puede eliminar un Procesador con estado diferente a 'INA'.");
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar el procesador. Motivo: " + ex.getMessage());
        }
    }

    public void guardarClave(SeguridadProcesador nuevaClave) {
        validarClave(nuevaClave);
        nuevaClave.setEstado(ESTADO_PENDIENTE);
        nuevaClave.setFechaActualizacion(LocalDateTime.now());
        nuevaClave.setFechaActivacion(LocalDate.now());
        repository.save(nuevaClave);
    }

    public void actualizarEstadoClave(Integer codigo, String nuevoEstado) {
        SeguridadProcesador procesador = repository.findById(codigo)
                .orElseThrow(() -> new NotFoundException(codigo.toString(), ENTITY_NAME));
        procesador.setEstado(nuevoEstado);
        procesador.setFechaActualizacion(LocalDateTime.now());
        repository.save(procesador);
    }

    private void validarClave(SeguridadProcesador procesador) {
        if (procesador.getClave() == null || !procesador.getClave().matches(REGEX_CLAVE)) {
            throw new InvalidDataException(ERROR_CLAVE_INVALIDA);
        }
    }
}