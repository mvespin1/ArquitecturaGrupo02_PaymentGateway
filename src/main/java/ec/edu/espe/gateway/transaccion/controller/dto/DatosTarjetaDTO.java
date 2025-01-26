package ec.edu.espe.gateway.transaccion.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DatosTarjetaDTO {

    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Size(min = 16, max = 16, message = "El número de tarjeta debe tener 16 dígitos")
    @Pattern(regexp = "\\d{16}", message = "El número de tarjeta debe contener solo dígitos")
    private String cardNumber;

    @NotBlank(message = "La fecha de expiración es obligatoria")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Formato de fecha de expiración inválido (MM/YY)")
    private String expiryDate;

    @NotBlank(message = "El CVV es obligatorio")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "El CVV debe tener 3 o 4 dígitos")
    private String cvv;

    @NotBlank(message = "El nombre del titular es obligatorio")
    @Size(max = 100, message = "El nombre del titular no puede exceder los 100 caracteres")
    private String nombreTarjeta;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
    private String direccionTarjeta;
} 