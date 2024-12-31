package ec.edu.espe.gateway.transaccion.services;

import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class RecurrenceService {

    private final TransaccionRepository transaccionRepository;
    private final ComercioRepository comercioRepository;
    private final FacturacionComercioRepository facturacionComercioRepository;

    public RecurrenceService(TransaccionRepository transaccionRepository,
                           ComercioRepository comercioRepository,
                           FacturacionComercioRepository facturacionComercioRepository) {
        this.transaccionRepository = transaccionRepository;
        this.comercioRepository = comercioRepository;
        this.facturacionComercioRepository = facturacionComercioRepository;
    }

    @Transactional
    public void procesarTransaccionesRecurrentes() {
        LocalDate fechaActual = LocalDate.now();
        List<Transaccion> transaccionesRecurrentes = transaccionRepository.findRecurrentTransactionsToProcess(fechaActual);

        for (Transaccion transaccionRecurrente : transaccionesRecurrentes) {
            try {
                procesarTransaccionRecurrente(transaccionRecurrente, fechaActual);
            } catch (Exception e) {
                System.err.println("Error al procesar transacción recurrente " + 
                                 transaccionRecurrente.getCodigo() + ": " + e.getMessage());
            }
        }
    }

    private void procesarTransaccionRecurrente(Transaccion transaccionRecurrente, LocalDate fechaActual) {
        // Validar estado del comercio
        Comercio comercio = transaccionRecurrente.getComercio();
        if (!"ACT".equals(comercio.getEstado())) {
            detenerRecurrencia(transaccionRecurrente, "Comercio inactivo o suspendido");
            return;
        }

        // Obtener facturación activa
        FacturacionComercio facturacionActiva = facturacionComercioRepository
                .findByComercioAndEstado(comercio, "ACT")
                .stream()
                .findFirst()
                .orElse(null);

        if (facturacionActiva == null) {
            detenerRecurrencia(transaccionRecurrente, "No existe facturación activa");
            return;
        }

        // Crear nueva transacción
        Transaccion nuevaTransaccion = new Transaccion();
        copiarDatosTransaccion(transaccionRecurrente, nuevaTransaccion);
        nuevaTransaccion.setFacturacionComercio(facturacionActiva);
        nuevaTransaccion.setFecha(fechaActual);
        nuevaTransaccion.setCodigoUnicoTransaccion(generarCodigoUnico());
        nuevaTransaccion.setTipo("SIM");
        nuevaTransaccion.setEstado("ENV");

        // Registrar referencia a la transacción recurrente original
        nuevaTransaccion.setDetalle(String.format("Pago recurrente - %s (Origen: %s)", 
            transaccionRecurrente.getDetalle(), 
            transaccionRecurrente.getCodigoUnicoTransaccion()));

        transaccionRepository.save(nuevaTransaccion);

        // Actualizar próxima fecha de ejecución y registro histórico
        actualizarFechaEjecucion(transaccionRecurrente, nuevaTransaccion);
    }

    private void detenerRecurrencia(Transaccion transaccion, String motivo) {
        transaccion.setEstado("REC");
        transaccion.setDetalle(transaccion.getDetalle() + " - Detenida: " + motivo);
        transaccion.setFechaFinRecurrencia(LocalDate.now());
        transaccionRepository.save(transaccion);
    }

    private void copiarDatosTransaccion(Transaccion origen, Transaccion destino) {
        destino.setComercio(origen.getComercio());
        destino.setMarca(origen.getMarca());
        destino.setDetalle("Pago recurrente - " + origen.getDetalle());
        destino.setMonto(origen.getMonto());
        destino.setMoneda(origen.getMoneda());
        destino.setPais(origen.getPais());
        destino.setTarjeta(origen.getTarjeta());
    }

    private void actualizarFechaEjecucion(Transaccion transaccionRecurrente, Transaccion nuevaTransaccion) {
        LocalDate fechaActual = LocalDate.now();
        LocalDate nuevaFechaEjecucion = transaccionRecurrente.getFechaEjecucionRecurrencia().plusMonths(1);
        
        // Actualizar historial en la transacción recurrente
        String historialActual = transaccionRecurrente.getDetalle();
        String nuevaEjecucion = String.format("\nEjecutada: %s - Transacción: %s", 
            fechaActual, 
            nuevaTransaccion.getCodigoUnicoTransaccion());
        
        transaccionRecurrente.setDetalle(historialActual + nuevaEjecucion);
        
        if (nuevaFechaEjecucion.isAfter(transaccionRecurrente.getFechaFinRecurrencia())) {
            detenerRecurrencia(transaccionRecurrente, "Fecha fin alcanzada");
        } else {
            transaccionRecurrente.setFechaEjecucionRecurrencia(nuevaFechaEjecucion);
            transaccionRepository.save(transaccionRecurrente);
        }
    }

    private String generarCodigoUnico() {
        return "REC-" + UUID.randomUUID().toString();
    }

    @Transactional
    public void detenerRecurrenciasPorComercio(Integer codigoComercio) {
        try {
            // Validar estado del comercio
            Comercio comercio = comercioRepository.findById(codigoComercio)
                    .orElseThrow(() -> new EntityNotFoundException("Comercio no encontrado"));

            if (!"INA".equals(comercio.getEstado()) && !"SUS".equals(comercio.getEstado())) {
                throw new IllegalStateException(
                    "Solo se pueden detener recurrencias de comercios inactivos o suspendidos");
            }

            List<Transaccion> transaccionesRecurrentes = 
                transaccionRepository.findActiveRecurrentTransactionsByComercio(codigoComercio);

            for (Transaccion transaccion : transaccionesRecurrentes) {
                detenerRecurrencia(transaccion, 
                    String.format("Comercio en estado %s", comercio.getEstado()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al detener recurrencias: " + e.getMessage());
        }
    }
}
