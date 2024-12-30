package ec.edu.espe.gateway.facturacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacturacionComercioRepository extends JpaRepository<FacturacionComercio, Integer>{

    List<FacturacionComercio> findByEstado(String estado);
    
    @Query("SELECT f FROM FacturacionComercio f WHERE f.comercio.codigo = :codigoComercio AND f.estado = 'ACT' ORDER BY f.fechaInicio DESC")
    FacturacionComercio findFacturaActivaPorComercio(@Param("codigoComercio") Integer codigoComercio);
    
    @Query("SELECT f FROM FacturacionComercio f WHERE f.comercio.codigo = :codigoComercio ORDER BY f.fechaFin DESC LIMIT 1")
    FacturacionComercio findUltimaFacturaPorComercio(@Param("codigoComercio") Integer codigoComercio);
}
