package ec.edu.espe.pos.repository;

import ec.edu.espe.pos.model.SeguridadGateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SeguridadGatewayRepository extends JpaRepository<SeguridadGateway, Integer> {
    boolean existsByClave(String clave);
    
    @Query("SELECT s FROM SeguridadGateway s WHERE s.estado = :estado")
    List<SeguridadGateway> findByEstado(@Param("estado") String estado);

    @Modifying
    @Transactional
    @Query("UPDATE SeguridadGateway s SET s.estado = :estado WHERE s.estado = 'ACT'")
    void updateEstadoForActiveCodes(@Param("estado") String estado);
}