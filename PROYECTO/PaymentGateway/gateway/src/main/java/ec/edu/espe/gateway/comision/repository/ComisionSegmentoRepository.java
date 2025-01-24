package ec.edu.espe.gateway.comision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.model.ComisionSegmentoPK;

import java.util.List;

@Repository
public interface ComisionSegmentoRepository extends JpaRepository<ComisionSegmento, ComisionSegmentoPK> {
    List<ComisionSegmento> findByComisionOrderByPkTransaccionesDesdeAsc(Comision comision);
}
