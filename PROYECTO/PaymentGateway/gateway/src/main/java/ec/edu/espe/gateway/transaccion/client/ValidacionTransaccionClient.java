package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ec.edu.espe.gateway.transaccion.model.ValidacionTransaccionDTO;
import ec.edu.espe.gateway.transaccion.model.RespuestaValidacionDTO;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "validacionTransaccion", url = "https://3.135.191.61")
public interface ValidacionTransaccionClient {
    
    @PostMapping("/api/v1/transacciones")
    ResponseEntity<RespuestaValidacionDTO> validarTransaccion(@RequestBody ValidacionTransaccionDTO transaccion);
} 