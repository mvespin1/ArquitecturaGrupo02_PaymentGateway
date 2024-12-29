package ec.edu.espe.gateway.comision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.comision.model.ComisionSegmento;
import ec.edu.espe.gateway.comision.model.ComisionSegmentoPK;

public interface ComisionSegmentoRepository extends JpaRepository<ComisionSegmento, ComisionSegmentoPK>{

}
