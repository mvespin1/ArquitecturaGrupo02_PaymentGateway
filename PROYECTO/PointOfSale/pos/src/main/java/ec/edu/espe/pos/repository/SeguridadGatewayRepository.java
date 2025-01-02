package ec.edu.espe.pos.repository;

import ec.edu.espe.pos.model.SeguridadGateway;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeguridadGatewayRepository extends JpaRepository<SeguridadGateway, Integer> {
    List<SeguridadGateway> findByEstado(String estado);
}