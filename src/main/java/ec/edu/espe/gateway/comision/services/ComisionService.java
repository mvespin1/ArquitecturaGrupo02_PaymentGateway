package ec.edu.espe.gateway.comision.services;

import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.model.ComisionSegmentoPK;
import ec.edu.espe.gateway.comision.repository.ComisionRepository;
import ec.edu.espe.gateway.comision.repository.ComisionSegmentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ec.edu.espe.gateway.exception.DuplicateException;
import ec.edu.espe.gateway.exception.InvalidDataException;
import ec.edu.espe.gateway.exception.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static final String TIPO_COMISION_POR = "POR";
    public static final String TIPO_COMISION_FIJ = "FIJ";
    public static final String ENTITY_NAME = "Comisión";
    public static final String ENTITY_SEGMENTO = "Segmento de Comisión";

    // Crear una instancia del logger
    private static final Logger logger = LoggerFactory.getLogger(ComisionService.class);

    public ComisionService(ComisionRepository comisionRepository, ComisionSegmentoRepository segmentoRepository) {
        this.comisionRepository = comisionRepository;
        this.segmentoRepository = segmentoRepository;
    }

    public List<Comision> findAll() {
        try {
            logger.info("Obteniendo todas las comisiones");
            return comisionRepository.findAll();
        } catch (Exception ex) {
            logger.error("Error al obtener la lista de comisiones", ex);
            throw new RuntimeException("No se pudo obtener la lista de comisiones. Motivo: " + ex.getMessage());
        }
    }

    public Optional<Comision> findById(Integer codigo) {
        try {
            logger.info("Obteniendo comisión por ID: {}", codigo);
            Comision comision = comisionRepository.findById(codigo)
                .orElseThrow(() -> new NotFoundException(
                    codigo.toString(), 
                    ENTITY_NAME
                ));
            return Optional.of(comision);
        } catch (NotFoundException e) {
            logger.warn("Comisión no encontrada: {}", codigo);
            throw e;
        } catch (Exception ex) {
            logger.error("Error al obtener la comisión", ex);
            throw new RuntimeException("Error al obtener la comisión: " + ex.getMessage());
        }
    }

    @Transactional
    public Comision save(Comision comision) {
        try {
            logger.info("Guardando comisión: {}", comision);
            validarComision(comision);
            if (comisionRepository.existsById(comision.getCodigo())) {
                throw new DuplicateException(comision.getCodigo().toString(), ENTITY_NAME);
            }
            Comision nuevaComision = comisionRepository.save(comision);

            if (Boolean.TRUE.equals(comision.getManejaSegmentos())) {
                crearSegmentoInicial(nuevaComision);
            }

            return nuevaComision;
        } catch (DuplicateException | InvalidDataException e) {
            logger.warn("Error al guardar la comisión: {}", e.getMessage());
            throw e;
        } catch (Exception ex) {
            logger.error("Error al guardar la comisión", ex);
            throw new RuntimeException("No se pudo guardar la comisión. Motivo: " + ex.getMessage());
        }
    }

    @Transactional
    public Comision update(Integer codigo, Comision comision) {
        try {
            logger.info("Actualizando comisión: {}", comision);
            if (codigo == null) {
                throw new IllegalArgumentException("El código de comisión no puede ser nulo");
            }
            if (comision == null) {
                throw new IllegalArgumentException("La comisión no puede ser nula");
            }
            
            return comisionRepository.findById(codigo)
                    .map(comisionExistente -> {
                        try {
                            if (comision.getMontoBase() == null) {
                                throw new IllegalArgumentException("El monto base no puede ser nulo");
                            }
                            if (comision.getTransaccionesBase() == null) {
                                throw new IllegalArgumentException("Las transacciones base no pueden ser nulas");
                            }
                            comision.setCodigo(codigo);
                            comision.setTipo(comisionExistente.getTipo());
                            comision.setManejaSegmentos(comisionExistente.getManejaSegmentos());

                            validarComision(comision);
                            
                            comisionExistente.setMontoBase(comision.getMontoBase());
                            comisionExistente.setTransaccionesBase(comision.getTransaccionesBase());

                            if (Boolean.TRUE.equals(comisionExistente.getManejaSegmentos())) {
                                try {
                                    actualizarSegmento(comisionExistente);
                                } catch (Exception e) {
                                    throw new RuntimeException("Error al actualizar segmento: " + e.getMessage());
                                }
                            }

                            try {
                                return comisionRepository.save(comisionExistente);
                            } catch (Exception e) {
                                throw new RuntimeException("Error al guardar la comisión: " + e.getMessage());
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("Error durante la actualización: " + e.getMessage());
                        }
                    })
                    .orElseThrow(() -> new NotFoundException(
                        codigo.toString(), 
                        ENTITY_NAME
                    ));
        } catch (NotFoundException e) {
            logger.warn("Comisión no encontrada para actualizar: {}", codigo);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos al actualizar comisión: {}", e.getMessage());
            throw e;
        } catch (Exception ex) {
            logger.error("Error al actualizar la comisión", ex);
            throw new RuntimeException("Error al actualizar la comisión: " + ex.getMessage());
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
        if (comision.getTransaccionesBase() >= 1000000000) {
            throw new IllegalArgumentException(
                    "Las transacciones base no pueden exceder los 9 dígitos.");
        }
    }

    private void crearSegmentoInicial(Comision comision) {
        try {
            ComisionSegmentoPK pk = new ComisionSegmentoPK(comision.getCodigo(), comision.getTransaccionesBase());
            ComisionSegmento segmento = new ComisionSegmento(pk);
            segmento.setTransaccionesHasta(0);
            segmento.setMonto(BigDecimal.ZERO);

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
                    .orElseThrow(() -> new NotFoundException(
                        pk.toString(), 
                        ENTITY_SEGMENTO
                    ));
            segmento.setTransaccionesHasta(comision.getTransaccionesBase());
            segmento.setMonto(comision.getMontoBase());
            segmentoRepository.save(segmento);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar segmento: " + ex.getMessage());
        }
    }

    public void deleteById(Integer codigo) {
        throw new UnsupportedOperationException("No se permite eliminar comisiones.");
    }
}