package ec.edu.espe.gateway.transaccion.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.transaccion.model.GtwTransaccion;
import ec.edu.espe.gateway.transaccion.repository.GtwTransaccionRepository;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GtwTransaccionService {

    private final GtwTransaccionRepository repository;

    public GtwTransaccionService(GtwTransaccionRepository repository) {
        this.repository = repository;
    }

    public List<GtwTransaccion> findAll() {
        return repository.findAll();
    }

    public Optional<GtwTransaccion> findById(Integer id) {
        return repository.findById(id);
    }

    public GtwTransaccion create(GtwTransaccion gtwTransaccion) {
        return repository.save(gtwTransaccion);
    }

    public GtwTransaccion updateEstado(Integer id, String nuevoEstado) {
        return repository.findById(id)
                .map(existingTransaccion -> {
                    existingTransaccion.setEstado(nuevoEstado);
                    return repository.save(existingTransaccion);
                })
                .orElseThrow(() -> new EntityNotFoundException("Transacción con ID " + id + " no encontrada."));
    }

    public void delete(Integer id) {
        throw new UnsupportedOperationException("No se permite eliminar transacciones.");
    }
}
