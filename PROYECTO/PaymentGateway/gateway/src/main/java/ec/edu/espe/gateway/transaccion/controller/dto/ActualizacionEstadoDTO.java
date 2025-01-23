package ec.edu.espe.gateway.transaccion.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActualizacionEstadoDTO {
    @NotBlank(message = "El código único de transacción es obligatorio")
    @Size(min = 10, max = 20, message = "El código único debe tener entre 10 y 20 caracteres")
    private String codigoUnicoTransaccion;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(ENV|AUT|REC|REV|ANU)$", message = "Estado inválido")
    private String estado;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 200, message = "El mensaje no puede exceder los 200 caracteres")
    private String mensaje;
} 