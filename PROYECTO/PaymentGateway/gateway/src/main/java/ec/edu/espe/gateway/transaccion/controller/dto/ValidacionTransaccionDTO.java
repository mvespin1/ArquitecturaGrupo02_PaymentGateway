package ec.edu.espe.gateway.transaccion.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidacionTransaccionDTO {
    private BancoDTO banco;
    private Double monto;
    private String modalidad;
    private String codigoMoneda;
    private String marca;
    private String fechaExpiracionTarjeta;
    private String nombreTarjeta;
    private String numeroTarjeta;
    private String direccionTarjeta;
    private String cvv;
    private String pais;
    private String numeroCuenta;
    private String codigoUnicoTransaccion;
    private String gtwComision;
    private String gtwCuenta;
    private Integer cuotas;
    private Boolean interesDiferido;
} 