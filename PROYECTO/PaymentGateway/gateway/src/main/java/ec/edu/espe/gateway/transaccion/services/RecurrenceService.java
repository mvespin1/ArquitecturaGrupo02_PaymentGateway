package ec.edu.espe.gateway.transaccion.services;

import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecurrenceService {

    private final TransaccionRepository transaccionRepository;

    public RecurrenceService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    /**
     * Identifica y ejecuta transacciones recurrentes.
     */
    @Transactional
    public void processRecurrentTransactions() {
        // Identificar transacciones recurrentes que deben ejecutarse hoy
        LocalDate today = LocalDate.now();
        List<Transaccion> recurrentTransactions = transaccionRepository.findRecurrentTransactionsToProcess(today);

        for (Transaccion transaccion : recurrentTransactions) {
            try {
                // Generar nueva transacción
                Transaccion newTransaction = new Transaccion();
                newTransaction.setComercio(transaccion.getComercio());
                newTransaction.setFacturacionComercio(transaccion.getFacturacionComercio());
                newTransaction.setTipo("SIM"); // Transacción simple generada
                newTransaction.setMarca(transaccion.getMarca());
                newTransaction.setDetalle("Pago recurrente generado automáticamente");
                newTransaction.setMonto(transaccion.getMonto());
                newTransaction.setCodigoUnicoTransaccion(generateUniqueCode());
                newTransaction.setFecha(today);
                newTransaction.setEstado("AUT"); // Estado inicial "AUTORIZADO"
                newTransaction.setMoneda(transaccion.getMoneda());
                newTransaction.setPais(transaccion.getPais());
                newTransaction.setTarjeta(transaccion.getTarjeta());
                transaccionRepository.save(newTransaction);

                // Notificación o registro de éxito
                System.out.println("Transacción recurrente generada exitosamente: " + newTransaction.getCodigo());
            } catch (Exception e) {
                // Manejo de fallos en la transacción
                transaccion.setEstado("REC"); // Actualizar a estado fallido
                transaccionRepository.save(transaccion);
                System.err.println("Error al procesar transacción recurrente: " + transaccion.getCodigo());
            }
        }
    }

    /**
     * Genera un código único para la nueva transacción.
     */
    private String generateUniqueCode() {
        return "TX-" + System.currentTimeMillis();
    }
}
