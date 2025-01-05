package ec.edu.espe.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ec.edu.espe.pos.model.Configuracion;

@FeignClient(name = "gateway-configuracion-client", url = "http://localhost:8083")
public interface GatewayConfiguracionClient {
    
    @PostMapping("/api/pos-comercio/configuracion")
    ResponseEntity<Void> sincronizarConfiguracion(@RequestBody Configuracion configuracion);
} 