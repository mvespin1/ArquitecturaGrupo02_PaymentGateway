package ec.edu.espe.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ec.edu.espe.pos.dto.ComercioDTO;
import ec.edu.espe.pos.dto.FacturacionComercioDTO;

@FeignClient(name = "gateway-comercio", url = "http://18.116.69.173")
public interface GatewayComercioClient {
    
    @GetMapping("/api/comercios/activo")
    ComercioDTO obtenerComercioActivo();
    
    @GetMapping("/api/comercios/facturacion-activa")
    FacturacionComercioDTO obtenerFacturacionActiva();
    
    @GetMapping("/api/comercios/{codigoComercio}/facturacion")
    FacturacionComercioDTO obtenerFacturacionPorComercio(@PathVariable Integer codigoComercio);
} 