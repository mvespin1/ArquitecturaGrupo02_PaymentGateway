package ec.edu.espe.pos.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GatewayTransaccionDTO {
    private ComercioDTO comercio;
    private FacturacionComercioDTO facturacionComercio;
    private String tipo;
    private String marca;
    private String detalle;
    private BigDecimal monto;
    private String codigoUnicoTransaccion;
    private LocalDateTime fecha;
    private String estado;
    private String moneda;
    private String pais;
    private String tarjeta;
    private String codigoPos;
    private String modeloPos;
    private Boolean interesDiferido;
    private Integer cuotas;
}
