package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ec.edu.espe.gateway.transaccion.model.ValidacionTransaccionDTO;
import ec.edu.espe.gateway.transaccion.model.RespuestaValidacionDTO;

@FeignClient(name = "validacionTransaccion", url = "https://fb2e-192-188-58-23.ngrok-free.app")
public interface ValidacionTransaccionClient {
    
    @PostMapping("/api/v1/transacciones")
    RespuestaValidacionDTO validarTransaccion(@RequestBody ValidacionTransaccionDTO transaccion);
} 