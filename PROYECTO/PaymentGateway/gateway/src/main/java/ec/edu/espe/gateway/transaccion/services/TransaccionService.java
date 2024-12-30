package ec.edu.espe.gateway.transaccion.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.math.BigDecimal;

@Service
public class TransaccionService {

    private final TransaccionRepository repository;

    public TransaccionService(TransaccionRepository repository) {
        this.repository = repository;
    }

    public List<Transaccion> findAll() {
        return repository.findAll();
    }

    public Optional<Transaccion> findById(Integer id) {
        return repository.findById(id);
    }

    @Transactional
    public Transaccion create(Transaccion transaccion) {
        // Validaciones básicas
        if (transaccion.getComercio() == null || transaccion.getComercio().getCodigo() == null) {
            throw new IllegalArgumentException("El comercio es requerido");
        }
        
        validarTipoTransaccion(transaccion.getTipo());
        validarEstadoTransaccion(transaccion.getEstado());
        
        // Validar marca
        if (transaccion.getMarca() == null || transaccion.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca es requerida");
        }
        
        // Validar monto
        if (transaccion.getMonto() == null || transaccion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        
        // Validar detalle
        if (transaccion.getDetalle() == null || transaccion.getDetalle().trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle es requerido");
        }
        
        // Configurar fechas según el tipo de transacción
        if ("REC".equals(transaccion.getTipo())) {
            if (transaccion.getFechaEjecucionRecurrencia() == null) {
                throw new IllegalArgumentException("Las transacciones recurrentes requieren fecha de ejecución");
            }
            if (transaccion.getFechaFinRecurrencia() == null) {
                throw new IllegalArgumentException("Las transacciones recurrentes requieren fecha de fin");
            }
            if (transaccion.getFechaEjecucionRecurrencia().isAfter(transaccion.getFechaFinRecurrencia())) {
                throw new IllegalArgumentException("La fecha de ejecución debe ser anterior a la fecha de fin");
            }
        } else {
            // Para transacciones simples, las fechas de recurrencia deben ser nulas
            transaccion.setFechaEjecucionRecurrencia(null);
            transaccion.setFechaFinRecurrencia(null);
        }
        
        // Establecer fecha actual
        transaccion.setFecha(LocalDate.now());
        
        // Validar código único
        if (transaccion.getCodigoUnicoTransaccion() == null || transaccion.getCodigoUnicoTransaccion().trim().isEmpty()) {
            throw new IllegalArgumentException("El código único de transacción es requerido");
        }
        
        // Validar moneda
        if (transaccion.getMoneda() == null || transaccion.getMoneda().trim().isEmpty()) {
            throw new IllegalArgumentException("La moneda es requerida");
        }
        
        // Validar país
        if (transaccion.getPais() == null || transaccion.getPais().trim().isEmpty()) {
            throw new IllegalArgumentException("El país es requerido");
        }
        
        // Validar tarjeta
        if (transaccion.getTarjeta() == null || transaccion.getTarjeta().trim().isEmpty()) {
            throw new IllegalArgumentException("La tarjeta es requerida");
        }
        
        return repository.save(transaccion);
    }

    @Transactional
    public Transaccion updateEstado(Integer id, String nuevoEstado) {
        validarEstadoTransaccion(nuevoEstado);
        
        return repository.findById(id)
                .map(existingTransaccion -> {
                    validarTransicionEstado(existingTransaccion.getEstado(), nuevoEstado);
                    existingTransaccion.setEstado(nuevoEstado);
                    return repository.save(existingTransaccion);
                })
                .orElseThrow(() -> new EntityNotFoundException("Transacción con ID " + id + " no encontrada"));
    }

    private void validarTipoTransaccion(String tipo) {
        if (!"SIM".equals(tipo) && !"REC".equals(tipo)) {
            throw new IllegalArgumentException("Tipo de transacción inválido. Debe ser 'SIM' o 'REC'");
        }
    }

    private void validarEstadoTransaccion(String estado) {
        if (!"ENV".equals(estado) && !"AUT".equals(estado) && 
            !"REC".equals(estado) && !"REV".equals(estado)) {
            throw new IllegalArgumentException("Estado inválido. Debe ser 'ENV', 'AUT', 'REC' o 'REV'");
        }
    }
    
    private void validarTransicionEstado(String estadoActual, String nuevoEstado) {
        if ("AUT".equals(estadoActual) && !"REV".equals(nuevoEstado)) {
            throw new IllegalStateException("Una transacción autorizada solo puede ser reversada");
        }
        if ("REV".equals(estadoActual)) {
            throw new IllegalStateException("No se puede cambiar el estado de una transacción reversada");
        }
        if ("REC".equals(estadoActual)) {
            throw new IllegalStateException("No se puede cambiar el estado de una transacción rechazada");
        }
    }

    public List<Transaccion> findByEstado(String estado) {
        validarEstadoTransaccion(estado);
        return repository.findByEstado(estado);
    }

    public List<Transaccion> findRecurrentTransactionsToProcess(LocalDate fechaActual) {
        return repository.findRecurrentTransactionsToProcess(fechaActual);
    }

    public List<Transaccion> findByComercioAndFecha(Integer codigoComercio, LocalDate fechaInicio, LocalDate fechaFin) {
        if (codigoComercio == null) {
            throw new IllegalArgumentException("El código de comercio es requerido");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son requeridas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha fin");
        }
        
        return repository.findByComercio_CodigoAndFechaBetween(codigoComercio, fechaInicio, fechaFin);
    }
}
