package ec.edu.espe.gateway.comision.model;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class ComisionSegmentoPK implements Serializable {
    
    @NotNull
    @Column(name = "COD_COMISION", nullable = false)
    private Integer comision;
    @NotNull
    @Column(name = "TRANSACCIONES_DESDE", nullable = false)
    private Integer transaccionesDesde;

    // Empty constructor
    public ComisionSegmentoPK() {
    }

    // Constructor
    public ComisionSegmentoPK(Integer comision, Integer transaccionesDesde) {
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

    public Integer getTransaccionesDesde() {
        return this.transaccionesDesde;
    }

    public void setTransaccionesDesde(Integer transaccionesDesde) {
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
        ComisionSegmentoPK other = (ComisionSegmentoPK) obj;
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