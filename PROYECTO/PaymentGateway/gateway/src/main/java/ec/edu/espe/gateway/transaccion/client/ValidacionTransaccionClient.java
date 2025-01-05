package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ec.edu.espe.gateway.transaccion.model.ValidacionTransaccionDTO;

@FeignClient(name = "validacionTransaccion", url = "https://06da-2800-bf0-29c-1b03-8d62-c4a3-6db7-34e4.ngrok-free.app")
public interface ValidacionTransaccionClient {
    
    @PostMapping("/api/v1/transacciones")
    String validarTransaccion(@RequestBody ValidacionTransaccionDTO transaccion);
} 