package ec.edu.espe.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ec.edu.espe.pos.controller.dto.GatewayTransaccionDTO;

@FeignClient(name = "gateway-transaccion", url = "http://18.190.153.55")
public interface GatewayTransaccionClient {

    @PostMapping("/api/transacciones/sincronizar")
    ResponseEntity<String> sincronizarTransaccion(@RequestBody GatewayTransaccionDTO transaccion);
}
