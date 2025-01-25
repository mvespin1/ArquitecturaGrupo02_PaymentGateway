package ec.edu.espe.gateway.transaccion.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespuestaTransaccionDTO {
    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 200, message = "El mensaje no puede exceder los 200 caracteres")
    private String mensaje;
} 