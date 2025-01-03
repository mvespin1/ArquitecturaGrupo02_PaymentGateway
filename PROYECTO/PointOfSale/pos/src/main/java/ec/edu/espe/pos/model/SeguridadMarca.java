package ec.edu.espe.pos.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "POS_SEGURIDAD_MARCA")
public class SeguridadMarca implements Serializable {

    @Id
    @Column(name = "MARCA", length = 4, nullable = false)
    private String marca;
    @NotNull
    @Column(name = "CLAVE", length = 128, nullable = false)
    private String clave;
    @NotNull
    @Column(name = "FECHA_ACTUALIZACION", nullable = false)
    private LocalDateTime fechaActualizacion;

    public SeguridadMarca() {
    }

    public SeguridadMarca(String marca) {
        this.marca = marca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((marca == null) ? 0 : marca.hashCode());
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
        SeguridadMarca other = (SeguridadMarca) obj;
        if (marca == null) {
            if (other.marca != null)
                return false;
        } else if (!marca.equals(other.marca))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PosSeguridadMarca{" +
                "marca='" + marca + '\'' +
                ", clave='" + clave + '\'' +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}