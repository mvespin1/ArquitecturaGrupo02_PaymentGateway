package ec.edu.espe.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.pos.model.POSConfiguracion;
import ec.edu.espe.pos.model.PosConfiguracionPK;

public interface POSConfiguracionRepository extends JpaRepository<POSConfiguracion, PosConfiguracionPK> {
}