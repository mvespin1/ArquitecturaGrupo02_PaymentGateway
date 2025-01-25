package ec.edu.espe.gateway.transaccion.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidacionTransaccionDTO {
    @Valid
    @NotNull(message = "La información del banco es obligatoria")
    private Integer codigoBanco;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private Double monto;

    @NotBlank(message = "La modalidad es obligatoria")
    @Pattern(regexp = "^(SIM|REC)$", message = "La modalidad debe ser SIM o REC")
    private String modalidad;

    @NotBlank(message = "El código de moneda es obligatorio")
    @Size(min = 3, max = 3, message = "El código de moneda debe tener 3 caracteres")
    private String codigoMoneda;

    @NotBlank(message = "La marca es obligatoria")
    @Size(min = 2, max = 3, message = "La marca debe tener entre 2 y 3 caracteres")
    private String marca;

    @NotBlank(message = "La fecha de expiración es obligatoria")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/([0-9]{2})$", message = "Formato de fecha de expiración inválido (MM/YY)")
    private String fechaExpiracionTarjeta;

    @NotBlank(message = "El nombre del titular es obligatorio")
    @Size(max = 100, message = "El nombre del titular no puede exceder los 100 caracteres")
    private String nombreTarjeta;

    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Size(min = 16, max = 16, message = "El número de tarjeta debe tener 16 dígitos")
    @Pattern(regexp = "\\d{16}", message = "El número de tarjeta debe contener solo dígitos")
    private String numeroTarjeta;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede exceder los 200 caracteres")
    private String direccionTarjeta;

    @NotBlank(message = "El CVV es obligatorio")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "El CVV debe tener 3 o 4 dígitos")
    private String cvv;

    @NotBlank(message = "El país es obligatorio")
    @Size(min = 3, max = 3, message = "El código de país debe tener 3 caracteres")
    private String pais;

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(min = 8, max = 20, message = "El número de cuenta debe tener entre 8 y 20 caracteres")
    private String numeroCuenta;

    @NotBlank(message = "El código único de transacción es obligatorio")
    @Size(min = 10, max = 20, message = "El código único debe tener entre 10 y 20 caracteres")
    private String codigoUnicoTransaccion;

    @NotBlank(message = "La comisión del gateway es obligatoria")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Formato de comisión inválido")
    private String gtwComision;

    @NotBlank(message = "La cuenta del gateway es obligatoria")
    @Size(min = 8, max = 20, message = "El número de cuenta debe tener entre 8 y 20 caracteres")
    private String gtwCuenta;

    private Integer cuotas;

    @NotNull(message = "El campo interés diferido es obligatorio")
    private Boolean interesDiferido;
} 