package ec.edu.espe.gateway.comercio.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import ec.edu.espe.gateway.comercio.controller.dto.ConfiguracionDTO;

@FeignClient(name = "pos-configuracion", url = "http://3.142.98.0")
public interface PosConfiguracionClient {
    @PostMapping("/v1/pos-configuracion/sincronizar")
    void enviarConfiguracion(@RequestBody ConfiguracionDTO configuracion);
}