package ec.edu.espe.gateway.comercio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import java.util.List;
import java.util.Optional;

public interface ComercioRepository extends JpaRepository<Comercio, Integer> {
    Optional<Comercio> findByCodigoInterno(String codigoInterno);
    Optional<Comercio> findByRuc(String ruc);
    List<Comercio> findByEstado(String estado);
    List<Comercio> findByRazonSocialContainingIgnoreCaseOrNombreComercialContainingIgnoreCase(String razonSocial, String nombreComercial);
}
