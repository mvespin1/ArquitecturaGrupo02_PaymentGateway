package ec.edu.espe.gateway.comercio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import java.util.List;

public interface ComercioRepository extends JpaRepository<Comercio, Integer> {
    List<Comercio> findByEstado(String estado);
    List<Comercio> findByPagosAceptados(String pagosAceptados);
}
