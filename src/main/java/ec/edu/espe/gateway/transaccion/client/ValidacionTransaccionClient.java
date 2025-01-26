package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ec.edu.espe.gateway.transaccion.controller.dto.RespuestaValidacionDTO;
import ec.edu.espe.gateway.transaccion.controller.dto.ValidacionTransaccionDTO;

import org.springframework.http.ResponseEntity;

@FeignClient(name = "validacionTransaccion", url = "https://681c-2800-370-d3-b1b0-cc63-28bd-d680-d142.ngrok-free.app")
public interface ValidacionTransaccionClient {

    @PostMapping("/api/v1/transacciones")
    ResponseEntity<RespuestaValidacionDTO> validarTransaccion(
            @RequestBody ValidacionTransaccionDTO validacionDTO);
}