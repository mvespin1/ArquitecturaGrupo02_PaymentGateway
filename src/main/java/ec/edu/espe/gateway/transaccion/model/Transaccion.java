package ec.edu.espe.gateway.transaccion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.comercio.model.Comercio;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GTW_TRANSACCION")
public class Transaccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TRANSACCION", nullable = false)
    private Integer codigo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_COMERCIO", nullable = false)
    private Comercio comercio;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_FACTURACION_COMERCIO", nullable = false)
    private FacturacionComercio facturacionComercio;

    @NotNull
    @Column(name = "TIPO", length = 3, nullable = false)
    private String tipo;

    @NotNull
    @Column(name = "MARCA", length = 4, nullable = false)
    private String marca;

    @NotNull
    @Column(name = "DETALLE", length = 255, nullable = false)
    private String detalle;

    @NotNull
    @Column(name = "MONTO", precision = 20, scale = 2, nullable = false)
    private BigDecimal monto;

    @NotNull
    @Column(name = "CODIGO_UNICO_TRANSACCION", length = 64, nullable = false)
    private String codigoUnicoTransaccion;

    @NotNull
    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @NotNull
    @Column(name = "ESTADO", length = 3, nullable = false)
    private String estado;

    @NotNull
    @Column(name = "MONEDA", length = 3, nullable = false)
    private String moneda;

    @NotNull
    @Column(name = "PAIS", length = 2, nullable = false)
    private String pais;

    @NotNull
    @Column(name = "TARJETA", length = 256, nullable = false)
    private String tarjeta;

    @Column(name = "FECHA_EJECUCION_RECURRENCIA")
    private LocalDate fechaEjecucionRecurrencia;

    @Column(name = "FECHA_FIN_RECURRENCIA")
    private LocalDate fechaFinRecurrencia;

    @Transient
    private String codigoInterno;

    @Transient
    private Boolean interesDiferido;

    @Transient
    private Integer cuotas;

    public Transaccion(Integer codigo) {
        this.codigo = codigo;
    }

    public void setTipo(String tipo) {
        if (!"SIM".equals(tipo) && !"REC".equals(tipo)) {
            throw new IllegalArgumentException("Tipo debe ser 'SIM' o 'REC'.");
        }
        this.tipo = tipo;
    }

    public void setEstado(String estado) {
        if (!"ENV".equals(estado) && !"AUT".equals(estado) && !"REC".equals(estado) && !"REV".equals(estado)) {
            throw new IllegalArgumentException("Estado debe ser 'ENV', 'AUT', 'REC' o 'REV'.");
        }
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Transaccion [codigo=" + codigo 
            + ", comercio=" + comercio 
            + ", tipo=" + tipo 
            + ", marca=" + marca 
            + ", monto=" + monto 
            + ", codigoUnicoTransaccion=" + codigoUnicoTransaccion 
            + ", fecha=" + fecha 
            + ", estado=" + estado + "]";
    }
}
