package ec.edu.espe.gateway.transaccion.services;

import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import ec.edu.espe.gateway.exception.NotFoundException;
import ec.edu.espe.gateway.exception.StateException;
import ec.edu.espe.gateway.exception.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RecurrenceService {

    private static final Logger log = LoggerFactory.getLogger(RecurrenceService.class);

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
        try {
            LocalDateTime fechaActual = LocalDateTime.now();
            List<Transaccion> transaccionesRecurrentes = transaccionRepository.findRecurrentTransactionsToProcess(fechaActual);

            for (Transaccion transaccionRecurrente : transaccionesRecurrentes) {
                try {
                    procesarTransaccionRecurrente(transaccionRecurrente, fechaActual);
                } catch (StateException | ValidationException e) {
                    log.error("Error controlado al procesar recurrencia {}: {}", 
                        transaccionRecurrente.getCodigo(), e.getMessage());
                    detenerRecurrencia(transaccionRecurrente, e.getMessage());
                } catch (Exception e) {
                    log.error("Error no controlado al procesar recurrencia {}: {}", 
                        transaccionRecurrente.getCodigo(), e.getMessage());
                    detenerRecurrencia(transaccionRecurrente, "Error interno: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error al procesar transacciones recurrentes: {}", e.getMessage());
            throw new ValidationException("procesamiento", 
                "Error al procesar transacciones recurrentes: " + e.getMessage());
        }
    }

    private void procesarTransaccionRecurrente(Transaccion transaccionRecurrente, LocalDateTime fechaActual) {
        // Validar estado del comercio
        Comercio comercio = transaccionRecurrente.getComercio();
        if (!"ACT".equals(comercio.getEstado())) {
            throw new StateException(comercio.getEstado(), 
                "procesar recurrencia con comercio inactivo o suspendido");
        }

        // Obtener facturación activa
        FacturacionComercio facturacionActiva = facturacionComercioRepository
                .findByComercioAndEstado(comercio, "ACT")
                .stream()
                .findFirst()
                .orElseThrow(() -> new ValidationException("comercio", 
                    "No existe facturación activa para el comercio"));

        try {
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
            
        } catch (Exception e) {
            log.error("Error al crear nueva transacción recurrente: {}", e.getMessage());
            throw new ValidationException("creacion", 
                "Error al crear nueva transacción recurrente: " + e.getMessage());
        }
    }

    private void detenerRecurrencia(Transaccion transaccion, String motivo) {
        try {
            transaccion.setEstado("REC");
            transaccion.setDetalle(transaccion.getDetalle() + " - Detenida: " + motivo);
            transaccion.setFechaFinRecurrencia(LocalDate.now());
            transaccionRepository.save(transaccion);
            log.info("Recurrencia {} detenida. Motivo: {}", transaccion.getCodigo(), motivo);
        } catch (Exception e) {
            log.error("Error al detener recurrencia {}: {}", transaccion.getCodigo(), e.getMessage());
            throw new StateException("ACTIVO", 
                "No se pudo detener la recurrencia: " + e.getMessage());
        }
    }

    private void copiarDatosTransaccion(Transaccion origen, Transaccion destino) {
        try {
            destino.setComercio(origen.getComercio());
            destino.setMarca(origen.getMarca());
            destino.setDetalle("Pago recurrente - " + origen.getDetalle());
            destino.setMonto(origen.getMonto());
            destino.setMoneda(origen.getMoneda());
            destino.setPais(origen.getPais());
            destino.setTarjeta(origen.getTarjeta());
        } catch (Exception e) {
            throw new ValidationException("copia_datos", 
                "Error al copiar datos de transacción: " + e.getMessage());
        }
    }

    private void actualizarFechaEjecucion(Transaccion transaccionRecurrente, Transaccion nuevaTransaccion) {
        try {
            LocalDate fechaActual = LocalDate.now();
            LocalDate nuevaFechaEjecucion = transaccionRecurrente.getFechaEjecucionRecurrencia().plusMonths(1);
            
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
        } catch (Exception e) {
            throw new ValidationException("actualizacion_fecha", 
                "Error al actualizar fecha de ejecución: " + e.getMessage());
        }
    }

    private String generarCodigoUnico() {
        return "REC-" + UUID.randomUUID().toString();
    }

    @Transactional
    public void detenerRecurrenciasPorComercio(Integer codigoComercio) {
        try {
            Comercio comercio = comercioRepository.findById(codigoComercio)
                    .orElseThrow(() -> new NotFoundException(codigoComercio.toString(), "Comercio"));

            if (!"INA".equals(comercio.getEstado()) && !"SUS".equals(comercio.getEstado())) {
                throw new StateException(comercio.getEstado(),
                    "detener recurrencias (solo permitido para comercios inactivos o suspendidos)");
            }

            List<Transaccion> transaccionesRecurrentes = 
                transaccionRepository.findActiveRecurrentTransactionsByComercio(codigoComercio);

            for (Transaccion transaccion : transaccionesRecurrentes) {
                detenerRecurrencia(transaccion, 
                    String.format("Comercio en estado %s", comercio.getEstado()));
            }
        } catch (NotFoundException | StateException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al detener recurrencias por comercio {}: {}", codigoComercio, e.getMessage());
            throw new ValidationException("detencion_masiva", 
                "Error al detener recurrencias: " + e.getMessage());
        }
    }
}
