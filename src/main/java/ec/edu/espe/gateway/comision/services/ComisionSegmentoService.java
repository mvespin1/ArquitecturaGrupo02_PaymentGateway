package ec.edu.espe.gateway.comision.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.model.ComisionSegmentoPK;
import ec.edu.espe.gateway.comision.repository.ComisionSegmentoRepository;
import ec.edu.espe.gateway.exception.DuplicateException;
import ec.edu.espe.gateway.exception.InvalidDataException;
import ec.edu.espe.gateway.exception.NotFoundException;

@Service
public class ComisionSegmentoService {

    private final ComisionSegmentoRepository segmentoRepository;
    public static final String ENTITY_NAME = "Segmento de Comisión";

    private static final int MAX_DIGITOS_TRANSACCION_HASTA = 9;
    private static final int MAX_DIGITOS_MONTO = 20;
    private static final int MAX_DIGITOS_ENTERO_MONTO = 16;
    private static final int MAX_DIGITOS_DECIMAL_MONTO = 4;

    private static final Logger logger = LoggerFactory.getLogger(ComisionSegmentoService.class);

    public ComisionSegmentoService(ComisionSegmentoRepository segmentoRepository) {
        this.segmentoRepository = segmentoRepository;
    }

    public List<ComisionSegmento> findAll() {
        try {
            logger.info("Obteniendo todos los segmentos de comisión");
            return segmentoRepository.findAll();
        } catch (Exception ex) {
            logger.error("Error al obtener la lista de segmentos de comisión", ex);
            throw new RuntimeException(
                    "No se pudo obtener la lista de segmentos de comisión. Motivo: " + ex.getMessage());
        }
    }

    public Optional<ComisionSegmento> findById(Integer comision, Integer transaccionesDesde) {
        try {
            logger.info("Obteniendo segmento de comisión por ID: comision={}, transaccionesDesde={}", comision, transaccionesDesde);
            ComisionSegmentoPK pk = new ComisionSegmentoPK(comision, transaccionesDesde);
            ComisionSegmento segmento = segmentoRepository.findById(pk)
                .orElseThrow(() -> new NotFoundException(
                    pk.toString(), 
                    ENTITY_NAME
                ));
            return Optional.of(segmento);
        } catch (NotFoundException e) {
            logger.warn("Segmento de comisión no encontrado: comision={}, transaccionesDesde={}", comision, transaccionesDesde);
            throw e;
        } catch (Exception ex) {
            logger.error("Error al obtener el segmento de comisión", ex);
            throw new RuntimeException("Error al obtener el segmento: " + ex.getMessage());
        }
    }

    @Transactional
    public ComisionSegmento save(ComisionSegmento segmento) {
        try {
            logger.info("Guardando segmento de comisión: {}", segmento);
            validarSegmento(segmento);
            if (segmentoRepository.existsById(segmento.getPk())) {
                throw new DuplicateException(segmento.getPk().toString(), ENTITY_NAME);
            }
            return segmentoRepository.save(segmento);
        } catch (DuplicateException | InvalidDataException e) {
            logger.warn("Error al guardar el segmento de comisión: {}", e.getMessage());
            throw e;
        } catch (Exception ex) {
            logger.error("Error al guardar el segmento de comisión", ex);
            throw new RuntimeException("No se pudo guardar el segmento de comisión. Motivo: " + ex.getMessage());
        }
    }

    @Transactional
    public ComisionSegmento update(Integer comision, Integer transaccionesDesde,
            Integer transaccionesHasta, BigDecimal monto) {
        try {
            logger.info("Actualizando segmento de comisión: comision={}, transaccionesDesde={}, transaccionesHasta={}, monto={}", comision, transaccionesDesde, transaccionesHasta, monto);
            ComisionSegmentoPK pk = new ComisionSegmentoPK(comision, transaccionesDesde);
            return segmentoRepository.findById(pk)
                    .map(segmentoExistente -> {
                        validarRangoTransacciones(transaccionesDesde, transaccionesHasta);
                        validarMonto(monto);
                        segmentoExistente.setTransaccionesHasta(transaccionesHasta);
                        segmentoExistente.setMonto(monto);
                        return segmentoRepository.save(segmentoExistente);
                    })
                    .orElseThrow(() -> new NotFoundException(
                        pk.toString(), 
                        ENTITY_NAME
                    ));
        } catch (NotFoundException e) {
            logger.warn("Segmento de comisión no encontrado para actualizar: comision={}, transaccionesDesde={}", comision, transaccionesDesde);
            throw e;
        } catch (Exception ex) {
            logger.error("Error al actualizar el segmento de comisión", ex);
            throw new RuntimeException("Error al actualizar el segmento: " + ex.getMessage());
        }
    }

    @Transactional
    public void deleteById(Integer comision, Integer transaccionesDesde) {
        try {
            logger.info("Eliminando segmento de comisión: comision={}, transaccionesDesde={}", comision, transaccionesDesde);
            ComisionSegmentoPK pk = new ComisionSegmentoPK(comision, transaccionesDesde);
            ComisionSegmento segmento = segmentoRepository.findById(pk)
                    .orElseThrow(() -> new NotFoundException(
                        pk.toString(), 
                        ENTITY_NAME
                    ));

            if (segmento.getMonto().compareTo(BigDecimal.ZERO) == 0) {
                segmentoRepository.deleteById(pk);
            } else {
                throw new IllegalStateException("No se puede eliminar un segmento con monto diferente a 0.");
            }
        } catch (NotFoundException e) {
            logger.warn("Segmento de comisión no encontrado para eliminar: comision={}, transaccionesDesde={}", comision, transaccionesDesde);
            throw e;
        } catch (Exception ex) {
            logger.error("Error al eliminar el segmento de comisión", ex);
            throw new RuntimeException("Error al eliminar el segmento: " + ex.getMessage());
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
        validarMonto(segmento.getMonto());
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
        if (hasta > Math.pow(10, MAX_DIGITOS_TRANSACCION_HASTA) - 1) {
            throw new IllegalArgumentException("El número de transacciones hasta no puede exceder los "
                    + MAX_DIGITOS_TRANSACCION_HASTA + " dígitos.");
        }
    }

    private void validarMonto(BigDecimal monto) {
        if (monto.precision() - monto.scale() > MAX_DIGITOS_ENTERO_MONTO) {
            throw new IllegalArgumentException("El monto no puede tener más de " + MAX_DIGITOS_ENTERO_MONTO
                    + " dígitos antes del punto decimal.");
        }
        if (monto.scale() > MAX_DIGITOS_DECIMAL_MONTO) {
            throw new IllegalArgumentException("El monto no puede tener más de " + MAX_DIGITOS_DECIMAL_MONTO
                    + " dígitos después del punto decimal.");
        }
        if (monto.precision() > MAX_DIGITOS_MONTO) {
            throw new IllegalArgumentException("El monto no puede tener más de " + MAX_DIGITOS_MONTO
                    + " dígitos en total.");
        }
    }
}