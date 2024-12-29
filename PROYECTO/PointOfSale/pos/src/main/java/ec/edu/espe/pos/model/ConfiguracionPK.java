package ec.edu.espe.pos.model;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class ConfiguracionPK implements Serializable {

    @NotNull
    @Column(name = "CODIGO_POS", length = 10, nullable = false)
    private String codigo;
    @NotNull
    @Column(name = "MODELO", length = 10, nullable = false)
    private String modelo;

    public ConfiguracionPK() {
    }

    public ConfiguracionPK(@NotNull String codigo, @NotNull String modelo) {
        this.codigo = codigo;
        this.modelo = modelo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

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
        ConfiguracionPK other = (ConfiguracionPK) obj;
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

    @Override
    public String toString() {
        return "PosConfiguracionPK [codigo=" + codigo + ", modelo=" + modelo + "]";
    }

}