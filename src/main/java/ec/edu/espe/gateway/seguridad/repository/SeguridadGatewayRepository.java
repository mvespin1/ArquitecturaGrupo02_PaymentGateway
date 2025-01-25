package ec.edu.espe.gateway.seguridad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.seguridad.model.SeguridadGateway;

public interface SeguridadGatewayRepository extends JpaRepository<SeguridadGateway, Integer>{
    List<SeguridadGateway> findByEstado(String estado);

}
