package ec.edu.espe.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "gateway-comercio", url = "http://localhost:8083")
public interface GatewayComercioClient {
    
    @GetMapping("/api/comercios/codigo/{codigoInterno}")
    Integer obtenerIdPorCodigoInterno(@PathVariable("codigoInterno") String codigoInterno);
} 