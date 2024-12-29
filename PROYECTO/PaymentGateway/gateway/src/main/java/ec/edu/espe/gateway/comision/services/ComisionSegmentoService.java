package ec.edu.espe.gateway.comision.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.model.ComisionSegmentoPK;
import ec.edu.espe.gateway.comision.repository.ComisionSegmentoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ComisionSegmentoService {

    private final ComisionSegmentoRepository segmentoRepository;

    public ComisionSegmentoService(ComisionSegmentoRepository segmentoRepository) {
        this.segmentoRepository = segmentoRepository;
    }

    public List<ComisionSegmento> findAll() {
        return segmentoRepository.findAll();
    }

    public Optional<ComisionSegmento> findById(Integer comision, Integer transaccionesDesde) {
        ComisionSegmentoPK pk = new ComisionSegmentoPK(comision, transaccionesDesde);
        return segmentoRepository.findById(pk);
    }

    @Transactional
    public ComisionSegmento save(ComisionSegmento segmento) {
        validarSegmento(segmento);
        return segmentoRepository.save(segmento);
    }

    @Transactional
    public ComisionSegmento update(Integer comision, Integer transaccionesDesde,
                                   Integer transaccionesHasta, BigDecimal monto) {
            ComisionSegmentoPK pk = new ComisionSegmentoPK(comision, transaccionesDesde);
        return segmentoRepository.findById(pk)
                .map(segmentoExistente -> {
                    validarRangoTransacciones(transaccionesDesde, transaccionesHasta);
                    segmentoExistente.setTransaccionesHasta(transaccionesHasta);
                    segmentoExistente.setMonto(monto);
                    return segmentoRepository.save(segmentoExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException(
                    "Segmento no encontrado para comisión " + comision + 
                    " y transacciones desde " + transaccionesDesde));
    }

    @Transactional
        public void deleteById(Integer comision, Integer transaccionesDesde) {
        ComisionSegmentoPK pk = new ComisionSegmentoPK(comision, transaccionesDesde);
        ComisionSegmento segmento = segmentoRepository.findById(pk)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Segmento no encontrado para comisión " + comision + 
                    " y transacciones desde " + transaccionesDesde));

        if (segmento.getMonto().compareTo(BigDecimal.ZERO) == 0) {
            segmentoRepository.deleteById(pk);
        } else {
            throw new IllegalStateException("No se puede eliminar un segmento con monto diferente a 0.");
        }
    }

    private void validarSegmento(ComisionSegmento segmento) {
        if (segmento.getPk() == null) {
            throw new IllegalArgumentException("La clave primaria no puede ser nula");
        }
        if (segmento.getTransaccionesHasta() == null) {
            throw new IllegalArgumentException("El número de transacciones hasta no puede ser nulo");
        }
        if (segmento.getMonto() == null) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
        validarRangoTransacciones(segmento.getPk().getTransaccionesDesde(), segmento.getTransaccionesHasta());
    }

    private void validarRangoTransacciones(Integer desde, Integer hasta) {
        if (desde.compareTo(hasta) >= 0) {
            throw new IllegalArgumentException(
                "El rango de transacciones es inválido. 'Desde' debe ser menor que 'Hasta'");
        }
            if (desde.compareTo(0) < 0 || hasta.compareTo(0) < 0) {
            throw new IllegalArgumentException(
                "El número de transacciones no puede ser negativo");
        }
    }
}
