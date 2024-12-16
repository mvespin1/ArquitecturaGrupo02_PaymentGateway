package ec.edu.espe.pos.repository;

import ec.edu.espe.pos.model.POSConfiguracion;
import ec.edu.espe.pos.model.PosConfiguracionPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface POSConfiguracionRepository extends JpaRepository<POSConfiguracion, PosConfiguracionPK> {
}