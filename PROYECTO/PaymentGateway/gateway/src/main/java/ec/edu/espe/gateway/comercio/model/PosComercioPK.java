package ec.edu.espe.gateway.comercio.model;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class PosComercioPK implements Serializable {
    
    @NotNull
    @Column(name = "MODELO", length = 10, nullable = false)
    private String modelo;
    @NotNull
    @Column(name = "CODIGO_POS", length = 10, nullable = false)
    private String codigo;

    // EMPTY CONSTRUCTOR
    public PosComercioPK() {
    }

    // CONSTRUCTOR
    public PosComercioPK(String modelo, String codigo) {
        this.modelo = modelo;
        this.codigo = codigo;
    }

    // GETTERS AND SETTERS
    public String getModelo() {
        return this.modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    // toString
    @Override
    public String toString() {
        return "{" +
                " modelo='" + getModelo() + "'" +
                ", codigo='" + getCodigo() + "'" +
                "}";
    }

    // equals and hashCode

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
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
        PosComercioPK other = (PosComercioPK) obj;
        if (modelo == null) {
            if (other.modelo != null)
                return false;
        } else if (!modelo.equals(other.modelo))
            return false;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

}
