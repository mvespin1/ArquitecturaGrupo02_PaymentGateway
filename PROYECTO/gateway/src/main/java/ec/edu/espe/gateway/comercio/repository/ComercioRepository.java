package ec.edu.espe.gateway.comercio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Integer> {
    Optional<Comercio> findByCodigoInterno(String codigoInterno);
    Optional<Comercio> findByRuc(String ruc);
    List<Comercio> findByEstado(String estado);
    List<Comercio> findByRazonSocialContainingIgnoreCaseOrNombreComercialContainingIgnoreCase(String razonSocial, String nombreComercial);

    @Query("SELECT c FROM Comercio c WHERE c = :comercio")
    void refresh(@Param("comercio") Comercio comercio);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Comercio c WHERE c.codigo = :codigo")
    Optional<Comercio> findById(@Param("codigo") Integer codigo);
}
