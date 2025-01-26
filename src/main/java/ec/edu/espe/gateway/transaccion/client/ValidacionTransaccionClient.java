package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ec.edu.espe.gateway.transaccion.controller.dto.ValidacionTransaccionDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.RespuestaValidacionDTO;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "validacionTransaccion", url = "http://localhost:3001")
public interface ValidacionTransaccionClient {
    
    @PostMapping("/v1/transacciones")
    ResponseEntity<RespuestaValidacionDTO> validarTransaccion(@RequestBody ValidacionTransaccionDTO transaccion);
} 