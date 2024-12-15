package ec.edu.espe.gateway.comercio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.comercio.model.GtwComercio;

public interface GtwComercioRepository extends JpaRepository<GtwComercio, Integer>{
    void Save(GtwComercio gtwComercio);
    void deleteById(Integer id);
    void delete(GtwComercio gtwComercio);
    
}
