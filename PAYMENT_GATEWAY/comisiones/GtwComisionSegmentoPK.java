package ec.edu.espe.comisiones.model;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;

@Embeddable
public class GtwComisionSegmentoPK implements Serializable {

    @Column(name = "COD_COMISION", nullable = false)
    private Integer comision;

    @Column(name = "TRANSACCIONES_DESDE", precision = 9, nullable = false)
    private BigDecimal transaccionesDesde;

    // Empty constructor
    public GtwComisionSegmentoPK() {
    }

    // Constructor
    public GtwComisionSegmentoPK(Integer comision, BigDecimal transaccionesDesde) {
        this.comision = comision;
        this.transaccionesDesde = transaccionesDesde;
    }

    // Getters and Setters
    public Integer getComision() {
        return this.comision;
    }

    public void setComision(Integer comision) {
        this.comision = comision;
    }

    public BigDecimal getTransaccionesDesde() {
        return this.transaccionesDesde;
    }

    public void setTransaccionesDesde(BigDecimal transaccionesDesde) {
        this.transaccionesDesde = transaccionesDesde;
    }

    // ToString
    @Override
    public String toString() {
        return "{" +
                " comision='" + getComision() + "'" +
                ", transaccionesDesde='" + getTransaccionesDesde() + "'" +
                "}";
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
        GtwComisionSegmentoPK other = (GtwComisionSegmentoPK) obj;
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