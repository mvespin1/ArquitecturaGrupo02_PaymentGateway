package ec.edu.espe.pos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GatewayTransaccionDTO {
    private Integer codigo;
    private String tipo;
    private String marca;
    private String modalidad;
    private String detalle;
    private BigDecimal monto;
    private String codigoUnicoTransaccion;
    private LocalDateTime fecha;
    private String estado;
    private String moneda;
    private GatewayComercioPosDTO comercio;

    // Constructor y getters/setters
}

class GatewayComercioPosDTO {
    private Integer codigo;
    private String codigoInterno;
    private String estado = "ACT";  // Valor por defecto

    public GatewayComercioPosDTO(Integer codigo) {
        this.codigo = codigo;
    }

    // Getters y setters
} 