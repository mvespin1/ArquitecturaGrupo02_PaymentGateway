package ec.edu.espe.gateway.seguridad.model;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "GTW_SEGURIDAD_MARCA")
public class GtwSeguridadMarca implements Serializable {
    @NotNull
    @Id
    @Column(name = "MARCA", length = 4, nullable = false)
    private String marca;
    @NotNull
    @Column(name = "CLAVE", length = 128, nullable = false)
    private String clave;
    @NotNull
    @Column(name = "FECHA_ACTUALIZACION", nullable = false)
    private LocalDate fechaActualizacion;

    // Empty constructor
    public GtwSeguridadMarca() {
    }

    // Constructor with MARCA
    public GtwSeguridadMarca(String marca) {
        this.marca = marca;
    }

    // Getters and Setters
    public String getMarca() {
        return this.marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getClave() {
        return this.clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public LocalDate getFechaActualizacion() {
        return this.fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // toString
    @Override
    public String toString() {
        return "{" +
                " marca='" + getMarca() + "'" +
                ", clave='" + getClave() + "'" +
                ", fechaActualizacion='" + getFechaActualizacion() + "'" +
                "}";
    }

    // equals and hashCode

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
        GtwSeguridadMarca other = (GtwSeguridadMarca) obj;
        if (marca == null) {
            if (other.marca != null)
                return false;
        } else if (!marca.equals(other.marca))
            return false;
        return true;
    }

}