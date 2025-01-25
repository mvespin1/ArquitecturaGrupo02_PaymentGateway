package ec.edu.espe.gateway.transaccion.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BancoDTO {
    @NotNull(message = "El código del banco es obligatorio")
    @Positive(message = "El código del banco debe ser un número positivo")
    private Integer codigo;
} 