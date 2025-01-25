package ec.edu.espe.gateway.comercio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.comercio.model.Comercio;
import java.util.List;

public interface PosComercioRepository extends JpaRepository<PosComercio, PosComercioPK> {
    boolean existsByDireccionMac(String direccionMac);
    List<PosComercio> findByComercio_Codigo(Integer codigoComercio);
    List<PosComercio> findByEstado(String estado);
    List<PosComercio> findByComercio(Comercio comercio);
}
