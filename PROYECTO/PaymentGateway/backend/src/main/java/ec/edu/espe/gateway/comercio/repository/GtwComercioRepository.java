package ec.edu.espe.gateway.comercio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.comercio.model.GtwComercio;
import java.util.List;

public interface GtwComercioRepository extends JpaRepository<GtwComercio, Integer> {
    List<GtwComercio> findByEstado(String estado);
    List<GtwComercio> findByPagosAceptados(String pagosAceptados);
}
