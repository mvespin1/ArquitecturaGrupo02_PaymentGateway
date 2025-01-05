package ec.edu.espe.pos.client;

import ec.edu.espe.pos.dto.GatewayTransaccionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "gateway-transaccion", url = "http://localhost:8083")
public interface GatewayTransaccionClient {

    @PostMapping("/api/transacciones/sincronizar")
    @ResponseBody
    String sincronizarTransaccion(@RequestBody GatewayTransaccionDTO transaccion);
}
