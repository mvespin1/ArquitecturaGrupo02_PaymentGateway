package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ec.edu.espe.gateway.transaccion.controller.dto.RespuestaValidacionDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.ValidacionTransaccionDTO;

import org.springframework.http.ResponseEntity;

@FeignClient(name = "validacionTransaccion", url = "http://3.145.167.151")
public interface ValidacionTransaccionClient {
    
    @PostMapping("/api/v1/transacciones")
    ResponseEntity<RespuestaValidacionDTO> validarTransaccion(@RequestBody ValidacionTransaccionDTO transaccion);
} 