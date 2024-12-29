package ec.edu.espe.gateway.comision.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "GTW_COMISION_SEGMENTO")
public class ComisionSegmento implements Serializable {

    @EmbeddedId
    private ComisionSegmentoPK pk;
    @NotNull
    @Column(name = "TRANSACCIONES_HASTA", precision = 9, nullable = false)
    private BigDecimal transaccionesHasta;
    @NotNull
    @Column(name = "MONTO", precision = 20, scale = 4, nullable = false)
    private BigDecimal monto;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_COMISION", referencedColumnName = "COD_COMISION", nullable = false, insertable = false, updatable = false)
    private Comision comision;

    // Empty Constructor
    public ComisionSegmento() {
    }

    // Constructor
    public ComisionSegmento(ComisionSegmentoPK pk) {
        this.pk = pk;
    }

    public ComisionSegmentoPK getPk() {
        return pk;
    }

    public void setPk(ComisionSegmentoPK pk) {
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

    public Comision getComision() {
        return comision;
    }

    public void setComision(Comision comision) {
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
        ComisionSegmento other = (ComisionSegmento) obj;
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
