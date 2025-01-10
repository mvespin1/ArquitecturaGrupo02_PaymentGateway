package ec.edu.espe.pos.client;

import ec.edu.espe.pos.dto.GatewayTransaccionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gateway-transaccion", url = "http://18.191.49.5")
public interface GatewayTransaccionClient {

    @PostMapping("/api/transacciones/sincronizar")
    ResponseEntity<String> sincronizarTransaccion(@RequestBody GatewayTransaccionDTO transaccion);
}
