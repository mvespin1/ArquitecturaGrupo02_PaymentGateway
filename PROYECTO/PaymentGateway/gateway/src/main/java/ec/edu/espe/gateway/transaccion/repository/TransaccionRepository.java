package ec.edu.espe.gateway.transaccion.repository;

import ec.edu.espe.gateway.transaccion.model.Transaccion;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
    @Query("SELECT t FROM Transaccion t WHERE t.tipo = 'REC' AND t.fechaEjecucionRecurrencia <= :fechaActual AND t.fechaFinRecurrencia >= :fechaActual AND t.estado = 'ENV'")
    List<Transaccion> findRecurrentTransactionsToProcess(@Param("fechaActual") LocalDate fechaActual);
    
    List<Transaccion> findByEstado(String estado);
    
    List<Transaccion> findByComercio_CodigoAndFechaBetween(Integer codigoComercio, LocalDate fechaInicio, LocalDate fechaFin);
}