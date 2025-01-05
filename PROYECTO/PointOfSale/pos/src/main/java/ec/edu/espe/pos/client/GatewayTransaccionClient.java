package ec.edu.espe.pos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ec.edu.espe.pos.dto.GatewayTransaccionDTO;

@FeignClient(name = "gateway-transaccion", url = "http://localhost:8083")
public interface GatewayTransaccionClient {
    
    @PostMapping("/api/transacciones/sincronizar")
    void sincronizarTransaccion(@RequestBody GatewayTransaccionDTO transaccion);
    
    @PutMapping("/api/transacciones/{id}/estado")
    void actualizarEstadoTransaccion(@PathVariable("id") Integer id, @RequestParam("estado") String estado);
}
