package ec.edu.espe.gateway.comercio.model;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class GtwPosComercioPK implements Serializable {
    
    @NotNull
    @Column(name = "MODELO", length = 10, nullable = false)
    private String modelo;
    @NotNull
    @Column(name = "CODIGO_POS", length = 10, nullable = false)
    private String codigoPos;

    // EMPTY CONSTRUCTOR
    public GtwPosComercioPK() {
    }

    // CONSTRUCTOR
    public GtwPosComercioPK(String modelo, String codigoPos) {
        this.modelo = modelo;
        this.codigoPos = codigoPos;
    }

    // GETTERS AND SETTERS
    public String getModelo() {
        return this.modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCodigoPos() {
        return this.codigoPos;
    }

    public void setCodigoPos(String codigoPos) {
        this.codigoPos = codigoPos;
    }

    // toString
    @Override
    public String toString() {
        return "{" +
                " modelo='" + getModelo() + "'" +
                ", codigoPos='" + getCodigoPos() + "'" +
                "}";
    }

    // equals and hashCode

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
        result = prime * result + ((codigoPos == null) ? 0 : codigoPos.hashCode());
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
        GtwPosComercioPK other = (GtwPosComercioPK) obj;
        if (modelo == null) {
            if (other.modelo != null)
                return false;
        } else if (!modelo.equals(other.modelo))
            return false;
        if (codigoPos == null) {
            if (other.codigoPos != null)
                return false;
        } else if (!codigoPos.equals(other.codigoPos))
            return false;
        return true;
    }

}
