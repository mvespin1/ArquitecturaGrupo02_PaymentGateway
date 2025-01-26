package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ec.edu.espe.gateway.transaccion.controller.dto.RespuestaValidacionDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.ValidacionTransaccionDTO;

import org.springframework.http.ResponseEntity;

@FeignClient(name = "validacionTransaccion", url = "https://f580-2800-370-d3-b1b0-d8dc-9894-4f9c-9dc5.ngrok-free.app")
public interface ValidacionTransaccionClient {

    @PostMapping("/v1/transacciones")
    ResponseEntity<RespuestaValidacionDTO> validarTransaccion(
            @RequestBody ValidacionTransaccionDTO validacionDTO);
}