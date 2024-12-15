package ec.edu.espe.pos.services;

import ec.edu.espe.pos.model.PosTransaccion;
import ec.edu.espe.pos.repository.PosTransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PosTransaccionService {

    @Autowired
    private final PosTransaccionRepository posTransaccionRepository;

    public PosTransaccionService(PosTransaccionRepository posTransaccionRepository) {
        this.posTransaccionRepository = posTransaccionRepository;
    }

    public Optional<PosTransaccion> obtenerTransaccionPorCodigo(Integer codigo) {
        return posTransaccionRepository.findById(codigo);
    }

    @Transactional
    public PosTransaccion crearOActualizarTransaccion(PosTransaccion posTransaccion) {

        Optional<PosTransaccion> transaccionExistente = posTransaccionRepository.findById(posTransaccion.getCodigo());

        if (transaccionExistente.isPresent()) {
            PosTransaccion transaccion = transaccionExistente.get();
            transaccion.setEstado(posTransaccion.getEstado());
            transaccion.setEstadoRecibo(posTransaccion.getEstadoRecibo());
            return posTransaccionRepository.save(transaccion);
        } else {
            if (posTransaccion.getEstado() == null) {
                posTransaccion.setEstado("PEN");
            }
            return posTransaccionRepository.save(posTransaccion);
        }
    }
}
