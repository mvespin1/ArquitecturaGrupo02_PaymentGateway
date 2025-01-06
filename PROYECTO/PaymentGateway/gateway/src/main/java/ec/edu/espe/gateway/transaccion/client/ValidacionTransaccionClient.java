package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ec.edu.espe.gateway.transaccion.model.ValidacionTransaccionDTO;

@FeignClient(name = "validacionTransaccion", url = "https://34fa-2800-370-d3-b1b0-ede9-5831-df60-d0bb.ngrok-free.app")
public interface ValidacionTransaccionClient {
    
    @PostMapping("/api/v1/transacciones")
    String validarTransaccion(@RequestBody ValidacionTransaccionDTO transaccion);
} 