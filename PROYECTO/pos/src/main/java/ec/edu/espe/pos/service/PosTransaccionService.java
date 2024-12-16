package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.PosTransaccion;
import ec.edu.espe.pos.repository.PosTransaccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PosTransaccionService {

    private final PosTransaccionRepository posTransaccionRepository;

    public PosTransaccionService(PosTransaccionRepository posTransaccionRepository) {
        this.posTransaccionRepository = posTransaccionRepository;
    }

    public List<PosTransaccion> obtenerTodas() {
        return this.posTransaccionRepository.findAll();
    }

    public Optional<PosTransaccion> obtenerPorId(Integer id) {
        return this.posTransaccionRepository.findById(id);
    }

    public PosTransaccion crear(PosTransaccion posTransaccion) {
        return this.posTransaccionRepository.save(posTransaccion);
    }

    public PosTransaccion actualizarEstados(Integer id, String estado, String estadoRecibo) {
        Optional<PosTransaccion> transaccionOpt = this.posTransaccionRepository.findById(id);
        if (transaccionOpt.isPresent()) {
            PosTransaccion transaccion = transaccionOpt.get();
            transaccion.setEstado(estado);
            transaccion.setEstadoRecibo(estadoRecibo);
            return this.posTransaccionRepository.save(transaccion);
        }
        throw new RuntimeException("No se encontró la transacción con el ID proporcionado");
    }
} 