package ec.edu.espe.gateway.transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import ec.edu.espe.gateway.transaccion.controller.dto.ActualizacionEstadoDTO;

@FeignClient(name = "pos", url = "http://3.142.98.0")
public interface PosClient {
    
    @PutMapping("/v1/transacciones/actualizar-estado")
    ResponseEntity<Void> actualizarEstadoTransaccion(@RequestBody ActualizacionEstadoDTO actualizacion);
} 