package ec.edu.espe.gateway.transaccion.repository;

import ec.edu.espe.gateway.transaccion.model.GtwTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GtwTransaccionRepository extends JpaRepository<GtwTransaccion, Integer> {
}