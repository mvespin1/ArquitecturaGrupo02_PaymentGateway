package ec.edu.espe.gateway.transaccion.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActualizacionEstadoDTO {
    private String codigoUnicoTransaccion;
    private String estado;
    private String mensaje;
} 