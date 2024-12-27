package ec.edu.espe.gateway.transaccion.repository;

import ec.edu.espe.gateway.transaccion.model.GtwTransaccion;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GtwTransaccionRepository extends JpaRepository<GtwTransaccion, Integer> {
    @Query("SELECT t FROM GtwTransaccion t WHERE t.tipo = 'REC' AND t.fechaEjecucionRecurrencia <= :fechaActual AND t.fechaFinRecurrencia >= :fechaActual AND t.estado = 'ENV'")
    List<GtwTransaccion> findRecurrentTransactionsToProcess(@Param("fechaActual") LocalDate fechaActual);
    
    List<GtwTransaccion> findByEstado(String estado);
}