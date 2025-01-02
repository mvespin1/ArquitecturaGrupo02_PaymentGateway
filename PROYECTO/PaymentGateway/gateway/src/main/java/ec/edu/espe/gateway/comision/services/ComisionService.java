package ec.edu.espe.gateway.comision.services;

import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.model.ComisionSegmentoPK;
import ec.edu.espe.gateway.comision.repository.ComisionRepository;
import ec.edu.espe.gateway.comision.repository.ComisionSegmentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ComisionService {

    private final ComisionRepository comisionRepository;
    private final ComisionSegmentoRepository segmentoRepository;

    private static final int MAX_DIGITOS_MONTO_BASE = 20;
    private static final int MAX_DIGITOS_ENTERO_MONTO_BASE = 16;
    private static final int MAX_DIGITOS_DECIMAL_MONTO_BASE = 4;
    private static final int MAX_DIGITOS_TRANSACCION_BASE = 9;
    public static final String TIPO_COMISION_POR = "POR";
    public static final String TIPO_COMISION_FIJ = "FIJ";

    public ComisionService(ComisionRepository comisionRepository, ComisionSegmentoRepository segmentoRepository) {
        this.comisionRepository = comisionRepository;
        this.segmentoRepository = segmentoRepository;
    }

    public List<Comision> findAll() {
        try {
            return comisionRepository.findAll();
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo obtener la lista de comisiones. Motivo: " + ex.getMessage());
        }
    }

    public Optional<Comision> findById(Integer codigo) {
        try {
            return comisionRepository.findById(codigo);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "No se pudo obtener la comisión con código " + codigo + ". Motivo: " + ex.getMessage());
        }
    }

    @Transactional
    public Comision save(Comision comision) {
        try {
            validarComision(comision);
            Comision nuevaComision = comisionRepository.save(comision);

            if (Boolean.TRUE.equals(comision.getManejaSegmentos())) {
                crearSegmentoInicial(nuevaComision);
            }

            return nuevaComision;
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo guardar la comisión. Motivo: " + ex.getMessage());
        }
    }

    @Transactional
    public Comision update(Integer codigo, Comision comision) {
        try {
            return comisionRepository.findById(codigo)
                    .map(comisionExistente -> {
                        comisionExistente.setMontoBase(comision.getMontoBase());
                        comisionExistente.setTransaccionesBase(comision.getTransaccionesBase());

                        if (Boolean.TRUE.equals(comisionExistente.getManejaSegmentos())) {
                            actualizarSegmento(comisionExistente);
                        }

                        return comisionRepository.save(comisionExistente);
                    })
                    .orElseThrow(
                            () -> new EntityNotFoundException("Comisión con código " + codigo + " no encontrada."));
        } catch (Exception ex) {
            throw new RuntimeException(
                    "No se pudo actualizar la comisión con código " + codigo + ". Motivo: " + ex.getMessage());
        }
    }

    private void validarComision(Comision comision) {
        if (!TIPO_COMISION_POR.equals(comision.getTipo()) && !TIPO_COMISION_FIJ.equals(comision.getTipo())) {
            throw new IllegalArgumentException("El tipo de comisión debe ser 'POR' o 'FIJ'.");
        }
        if (comision.getMontoBase() == null || comision.getMontoBase().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto base debe ser mayor a 0.");
        }
        if (comision.getMontoBase().precision() - comision.getMontoBase().scale() > MAX_DIGITOS_ENTERO_MONTO_BASE) {
            throw new IllegalArgumentException("El monto base no puede tener más de " + MAX_DIGITOS_ENTERO_MONTO_BASE
                    + " dígitos antes del punto decimal.");
        }
        if (comision.getMontoBase().scale() > MAX_DIGITOS_DECIMAL_MONTO_BASE) {
            throw new IllegalArgumentException("El monto base no puede tener más de " + MAX_DIGITOS_DECIMAL_MONTO_BASE
                    + " dígitos después del punto decimal.");
        }
        if (comision.getMontoBase().precision() > MAX_DIGITOS_MONTO_BASE) {
            throw new IllegalArgumentException(
                    "El monto base no puede tener más de " + MAX_DIGITOS_MONTO_BASE + " dígitos en total.");
        }
        if (comision.getTransaccionesBase() == null || comision.getTransaccionesBase() <= 0) {
            throw new IllegalArgumentException("Las transacciones base deben ser mayores a 0.");
        }
        if (comision.getTransaccionesBase() > MAX_DIGITOS_TRANSACCION_BASE) {
            throw new IllegalArgumentException(
                    "Las transacciones base no pueden exceder los " + MAX_DIGITOS_TRANSACCION_BASE + " dígitos.");
        }
    }

    private void crearSegmentoInicial(Comision comision) {
        try {
            ComisionSegmentoPK pk = new ComisionSegmentoPK(comision.getCodigo(), comision.getTransaccionesBase());
            ComisionSegmento segmento = new ComisionSegmento(pk);
            segmento.setTransaccionesHasta(0);
            segmento.setMonto(BigDecimal.ZERO);
            segmento.setComision(comision);

            segmentoRepository.save(segmento);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "No se pudo crear el segmento inicial para la comisión. Motivo: " + ex.getMessage());
        }
    }

    private void actualizarSegmento(Comision comision) {
        try {
            ComisionSegmentoPK pk = new ComisionSegmentoPK(comision.getCodigo(), comision.getTransaccionesBase());
            ComisionSegmento segmento = segmentoRepository.findById(pk)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Segmento no encontrado para la comisión con código " + comision.getCodigo()));
            segmento.setTransaccionesHasta(comision.getTransaccionesBase());
            segmentoRepository.save(segmento);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo actualizar el segmento para la comisión con código "
                    + comision.getCodigo() + ". Motivo: " + ex.getMessage());
        }
    }

    public void deleteById(Integer codigo) {
        throw new UnsupportedOperationException("No se permite eliminar comisiones.");
    }
}