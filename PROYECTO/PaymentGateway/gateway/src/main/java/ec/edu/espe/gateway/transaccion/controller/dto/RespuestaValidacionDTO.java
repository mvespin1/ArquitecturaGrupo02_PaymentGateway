package ec.edu.espe.gateway.transaccion.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespuestaValidacionDTO {
    private String mensaje;
    private Integer codigoRespuesta;
} 