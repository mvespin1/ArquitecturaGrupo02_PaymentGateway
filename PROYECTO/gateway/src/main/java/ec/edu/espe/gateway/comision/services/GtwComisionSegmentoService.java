package ec.edu.espe.gateway.comision.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import ec.edu.espe.gateway.comision.model.GtwComisionSegmento;
import ec.edu.espe.gateway.comision.model.GtwComisionSegmentoPK;
import ec.edu.espe.gateway.comision.repository.GtwComisionSegmentoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GtwComisionSegmentoService {

    private final GtwComisionSegmentoRepository segmentoRepository;

    public GtwComisionSegmentoService(GtwComisionSegmentoRepository segmentoRepository) {
        this.segmentoRepository = segmentoRepository;
    }

    public List<GtwComisionSegmento> findAll() {
        return segmentoRepository.findAll();
    }

    public Optional<GtwComisionSegmento> findById(GtwComisionSegmentoPK id) {
        return segmentoRepository.findById(id);
    }

    public GtwComisionSegmento save(GtwComisionSegmento segmento) {
        return segmentoRepository.save(segmento);
    }

    public GtwComisionSegmento update(GtwComisionSegmentoPK id, BigDecimal transaccionesHasta, BigDecimal monto) {
        return segmentoRepository.findById(id)
                .map(segmentoExistente -> {
                    segmentoExistente.setTransaccionesHasta(transaccionesHasta);
                    segmentoExistente.setMonto(monto);
                    return segmentoRepository.save(segmentoExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Segmento con ID " + id + " no encontrado."));
    }

    public void deleteById(GtwComisionSegmentoPK id) {
        GtwComisionSegmento segmento = segmentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Segmento con ID " + id + " no encontrado."));

        if (segmento.getMonto().compareTo(BigDecimal.ZERO) == 0) {
            segmentoRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar el segmento con monto diferente a 0.");
        }
    }
}
