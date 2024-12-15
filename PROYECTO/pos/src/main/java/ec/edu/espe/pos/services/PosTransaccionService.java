package ec.edu.espe.pos.services;

import ec.edu.espe.pos.model.PosTransaccion;
import ec.edu.espe.pos.repository.PosTransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.List;

@Service
public class PosTransaccionService {

    private final PosTransaccionRepository posTransaccionRepository;

    public PosTransaccionService(PosTransaccionRepository posTransaccionRepository) {
        this.posTransaccionRepository = posTransaccionRepository;
    }

    public List<PosTransaccion> obtenerTodasLasTransacciones() {
        return posTransaccionRepository.findAll();
    }

    public Optional<PosTransaccion> obtenerTransaccionPorCodigo(Integer codigo) {
        return posTransaccionRepository.findById(codigo);
    }

    @Transactional
    public PosTransaccion crearTransaccion(PosTransaccion posTransaccion) {
        if (posTransaccion.getEstado() == null) {
            posTransaccion.setEstado("PEN");
        }
        if (posTransaccion.getEstadoRecibo() == null) {
            posTransaccion.setEstadoRecibo("PEN");
        }
        return posTransaccionRepository.save(posTransaccion);
    }

    @Transactional
    public PosTransaccion actualizarTransaccion(Integer codigo, PosTransaccion posTransaccion) {
        return posTransaccionRepository.findById(codigo)
                .map(transaccionExistente -> {
                    transaccionExistente.setEstado(posTransaccion.getEstado());
                    transaccionExistente.setEstadoRecibo(posTransaccion.getEstadoRecibo());
                    transaccionExistente.setMonto(posTransaccion.getMonto());
                    transaccionExistente.setDetalle(posTransaccion.getDetalle());
                    return posTransaccionRepository.save(transaccionExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Transacción no encontrada con código: " + codigo));
    }

    @Transactional
    public void eliminarTransaccion(Integer codigo) {
        if (!posTransaccionRepository.existsById(codigo)) {
            throw new EntityNotFoundException("Transacción no encontrada con código: " + codigo);
        }
        posTransaccionRepository.deleteById(codigo);
    }
}
