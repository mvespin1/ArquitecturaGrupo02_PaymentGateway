package ec.edu.espe.pos.repository;

import ec.edu.espe.pos.model.PosTransaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosTransaccionRepository extends JpaRepository<PosTransaccion, Integer> {
}