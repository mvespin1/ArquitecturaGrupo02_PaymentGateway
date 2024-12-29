package ec.edu.espe.pos.repository;

import ec.edu.espe.pos.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
}