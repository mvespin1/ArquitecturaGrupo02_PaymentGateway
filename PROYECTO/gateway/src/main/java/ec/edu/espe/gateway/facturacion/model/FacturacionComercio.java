package ec.edu.espe.gateway.facturacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comision.model.Comision;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GTW_FACTURACION_COMERCIO")
public class FacturacionComercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_FACTURACION_COMERCIO", nullable = false)
    private Integer codigo;

    @NotNull
    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "FECHA_FIN", nullable = false)
    private LocalDate fechaFin;

    @NotNull
    @Column(name = "TRANSACCIONES_PROCESADAS", precision = 9, nullable = false)
    private Integer transaccionesProcesadas;

    @NotNull
    @Column(name = "TRANSACCIONES_AUTORIZADAS", precision = 9, nullable = false)
    private Integer transaccionesAutorizadas;

    @NotNull
    @Column(name = "TRANSACCIONES_RECHAZADAS", precision = 9, nullable = false)
    private Integer transaccionesRechazadas;

    @NotNull
    @Column(name = "TRANSACCIONES_REVERSADAS", precision = 9, nullable = false)
    private Integer transaccionesReversadas;

    @NotNull
    @Column(name = "VALOR", precision = 20, scale = 4, nullable = false)
    private BigDecimal valor;

    @NotNull
    @Column(name = "ESTADO", length = 3, nullable = false)
    private String estado;

    @Column(name = "CODIGO_FACTURACION", length = 20)
    private String codigoFacturacion;

    @Column(name = "FECHA_FACTURACION")
    private LocalDate fechaFacturacion;

    @Column(name = "FECHA_PAGO")
    private LocalDate fechaPago;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_COMERCIO", nullable = false)
    private Comercio comercio;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_COMISION", nullable = false)
    private Comision comision;

    public FacturacionComercio(Integer codigo) {
        this.codigo = codigo;
    }

    // ToString

    @Override
    public String toString() {
        return "GtwFacturacionComercio [codigo=" + codigo + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin
                + ", transaccionesProcesadas=" + transaccionesProcesadas + ", transaccionesAutorizadas="
                + transaccionesAutorizadas + ", transaccionesRechazadas=" + transaccionesRechazadas
                + ", transaccionesReversadas=" + transaccionesReversadas + ", valor=" + valor + ", estado=" + estado
                + ", codigoFacturacion=" + codigoFacturacion + ", fechaFacturacion=" + fechaFacturacion + ", fechaPago="
                + fechaPago + ", comercio=" + comercio + ", comision=" + comision + "]";
    }

    // HashCode and Equals

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FacturacionComercio other = (FacturacionComercio) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

}
