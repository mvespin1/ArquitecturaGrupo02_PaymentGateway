package ec.edu.espe.pos.model;

import java.io.Serializable;
import jakarta.persistence.*;

@Embeddable
public class PosConfiguracionPK implements Serializable {

    @Column(name = "CODIGO_POS", length = 10, nullable = false)
    private String codigo;

    @Column(name = "MODELO", length = 10, nullable = false)
    private String modelo;

    // Empty constructor
    public PosConfiguracionPK() {
    }

    // Constructor
    public PosConfiguracionPK(String codigo, String modelo) {
        this.codigo = codigo;
        this.modelo = modelo;
    }

    // Getters and Setters
    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getModelo() {
        return this.modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    // toString
    @Override
    public String toString() {
        return "{" +
                " codigo='" + getCodigo() + "'" +
                ", modelo='" + getModelo() + "'" +
                "}";
    }

    // equals and hashCode

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
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
        PosConfiguracionPK other = (PosConfiguracionPK) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        if (modelo == null) {
            if (other.modelo != null)
                return false;
        } else if (!modelo.equals(other.modelo))
            return false;
        return true;
    }

}
