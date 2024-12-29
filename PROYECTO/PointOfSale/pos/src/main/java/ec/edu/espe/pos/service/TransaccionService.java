package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.Transaccion;
import ec.edu.espe.pos.repository.TransaccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    public List<Transaccion> obtenerTodas() {
        return this.transaccionRepository.findAll();
    }

    public Optional<Transaccion> obtenerPorId(Integer id) {
        return this.transaccionRepository.findById(id);
    }

    public Transaccion crear(Transaccion transaccion) {
        return this.transaccionRepository.save(transaccion);
    }

    public Transaccion actualizarEstados(Integer id, String estado, String estadoRecibo) {
        Optional<Transaccion> transaccionOpt = this.transaccionRepository.findById(id);
        if (transaccionOpt.isPresent()) {
            Transaccion transaccion = transaccionOpt.get();
            transaccion.setEstado(estado);
            transaccion.setEstadoRecibo(estadoRecibo);
            return this.transaccionRepository.save(transaccion);
        }
        throw new RuntimeException("No se encontró la transacción con el ID proporcionado");
    }
} 