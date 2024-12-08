package ec.edu.espe.comisiones.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "GTW_COMISION_SEGMENTO")
@IdClass(GtwComisionSegmentoPK.class)
public class GtwComisionSegmento implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "COD_COMISION", nullable = false)
    private GtwComision comision;

    @Id
    @Column(name = "TRANSACCIONES_DESDE", precision = 9, nullable = false)
    private BigDecimal transaccionesDesde;

    @Column(name = "TRANSACCIONES_HASTA", precision = 9, nullable = false)
    private BigDecimal transaccionesHasta;

    @Column(name = "MONTO", precision = 20, scale = 4, nullable = false)
    private BigDecimal monto;

    // Empty Constructor
    public GtwComisionSegmento() {
    }

    // Constructor with COD_COMISION and TRANSACCIONES_DESDE
    public GtwComisionSegmento(GtwComision comision, BigDecimal transaccionesDesde) {
        this.comision = comision;
        this.transaccionesDesde = transaccionesDesde;
    }

    // Getters y Setters
    public GtwComision getComision() {
        return comision;
    }

    public void setComision(GtwComision comision) {
        this.comision = comision;
    }

    public BigDecimal getTransaccionesDesde() {
        return transaccionesDesde;
    }

    public void setTransaccionesDesde(BigDecimal transaccionesDesde) {
        this.transaccionesDesde = transaccionesDesde;
    }

    public BigDecimal getTransaccionesHasta() {
        return transaccionesHasta;
    }

    public void setTransaccionesHasta(BigDecimal transaccionesHasta) {
        this.transaccionesHasta = transaccionesHasta;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    // toString
    @Override
    public String toString() {
        return "GtwComisionSegmento{" +
                "comision=" + comision +
                ", transaccionesDesde=" + transaccionesDesde +
                ", transaccionesHasta=" + transaccionesHasta +
                ", monto=" + monto +
                '}';
    }

    // Equals and HashCode

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((comision == null) ? 0 : comision.hashCode());
        result = prime * result + ((transaccionesDesde == null) ? 0 : transaccionesDesde.hashCode());
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
        GtwComisionSegmento other = (GtwComisionSegmento) obj;
        if (comision == null) {
            if (other.comision != null)
                return false;
        } else if (!comision.equals(other.comision))
            return false;
        if (transaccionesDesde == null) {
            if (other.transaccionesDesde != null)
                return false;
        } else if (!transaccionesDesde.equals(other.transaccionesDesde))
            return false;
        return true;
    }

}
