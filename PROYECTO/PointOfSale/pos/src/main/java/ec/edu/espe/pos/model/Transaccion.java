package ec.edu.espe.pos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "POS_TRANSACCION")
public class Transaccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TRANSACCION", nullable = false)
    private Integer codigo;

    @NotNull(message = "El tipo de transacción no puede ser nulo")
    @Column(name = "TIPO", length = 3, nullable = false)
    private String tipo;

    @NotNull(message = "La marca no puede ser nula")
    @Column(name = "MARCA", length = 4, nullable = false)
    private String marca;

    @NotNull(message = "La modalidad no puede ser nula")
    @Column(name = "MODALIDAD", length = 3, nullable = false)
    private String modalidad;

    @NotNull(message = "El detalle no puede ser nulo")
    @Column(name = "DETALLE", length = 255, nullable = false)
    private String detalle;

    @NotNull(message = "El monto no puede ser nulo")
    @Column(name = "MONTO", precision = 20, scale = 2, nullable = false)
    private BigDecimal monto;

    @NotNull(message = "El código único de transacción no puede ser nulo")
    @Column(name = "CODIGO_UNICO_TRANSACCION", length = 64, nullable = false)
    private String codigoUnicoTransaccion;

    @NotNull(message = "La fecha no puede ser nula")
    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "ESTADO", length = 3)
    private String estado;

    @Column(name = "ESTADO_RECIBO", length = 3)
    private String estadoRecibo;

    @NotNull(message = "La moneda no puede ser nula")
    @Column(name = "MONEDA", length = 3, nullable = false)
    private String moneda;

    public Transaccion(Integer codigo) {
        this.codigo = codigo;
    }

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
        Transaccion other = (Transaccion) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PosTransaccion{" +
                "codigo=" + codigo +
                ", tipo='" + tipo + '\'' +
                ", marca='" + marca + '\'' +
                ", modalidad='" + modalidad + '\'' +
                ", detalle='" + detalle + '\'' +
                ", monto=" + monto +
                ", codigoUnicoTransaccion='" + codigoUnicoTransaccion + '\'' +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", estadoRecibo='" + estadoRecibo + '\'' +
                ", moneda='" + moneda + '\'' +
                '}';
    }

}