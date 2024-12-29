package ec.edu.espe.gateway.comercio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;

public interface PosComercioRepository extends JpaRepository<PosComercio, PosComercioPK>{
    
}
