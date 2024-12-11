package ec.edu.espe.comisiones.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "GTW_COMISION_SEGMENTO")
public class GtwComisionSegmento implements Serializable {

    @EmbeddedId
    private GtwComisionSegmentoPK pk;

    @Column(name = "TRANSACCIONES_HASTA", precision = 9, nullable = false)
    private BigDecimal transaccionesHasta;

    @Column(name = "MONTO", precision = 20, scale = 4, nullable = false)
    private BigDecimal monto;

    @ManyToOne
    @JoinColumn(name = "COD_COMISION", referenceName = "COD_COMISION", nullable = false)
    private GtwComision comision;

    // Empty Constructor
    public GtwComisionSegmento() {
    }

    // Constructor
    public GtwComisionSegmento(GtwComisionSegmentoPK pk) {
        this.pk = pk;
    }

    public GtwComisionSegmentoPK getPk() {
        return pk;
    }

    public void setPk(GtwComisionSegmentoPK pk) {
        this.pk = pk;
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

    public GtwComision getComision() {
        return comision;
    }

    public void setComision(GtwComision comision) {
        this.comision = comision;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
        if (pk == null) {
            if (other.pk != null)
                return false;
        } else if (!pk.equals(other.pk))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GtwComisionSegmento [pk=" + pk + ", transaccionesHasta=" + transaccionesHasta + ", monto=" + monto
                + ", comision=" + comision + "]";
    }

}
