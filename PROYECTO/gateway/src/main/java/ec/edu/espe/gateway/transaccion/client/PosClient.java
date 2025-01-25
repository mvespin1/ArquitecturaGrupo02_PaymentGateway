package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import ec.edu.espe.gateway.transaccion.controller.dto.ActualizacionEstadoDTO;

@FeignClient(name = "pos", url = "http://localhost:8081")
public interface PosClient {
    
    @PostMapping("/v1/transacciones/actualizar-estado")
    ResponseEntity<Void> actualizarEstadoTransaccion(@RequestBody ActualizacionEstadoDTO actualizacion);
} 