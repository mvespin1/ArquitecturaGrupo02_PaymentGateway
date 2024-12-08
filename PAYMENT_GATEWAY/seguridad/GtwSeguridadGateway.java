package ec.edu.espe.seguridad.model;

import jakarta.persistence.*;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "GTW_SEGURIDAD_GATEWAY")
public class GtwSeguridadGateway implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_CLAVE_GATEWAY", nullable = false)
    private Integer codigo;

    @Column(name = "CLAVE", length = 128, nullable = false)
    private String clave;

    @Column(name = "FECHA_CREACION", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "FECHA_ACTIVACION", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaActivacion;

    @Column(name = "ESTADO", length = 3, nullable = false)
    private String estado;

    // Empty Constructor
    public GtwSeguridadGateway() {
    }

    // Constructor with COD_CLAVE_GATEWAY
    public GtwSeguridadGateway(Integer codigo) {
        this.codigo = codigo;
    }

    // Getters and Setters

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActivacion() {
        return fechaActivacion;
    }

    public void setFechaActivacion(Date fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // HashCode and Equals
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        GtwSeguridadGateway other = (GtwSeguridadGateway) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

    // toString

    @Override
    public String toString() {
        return "GtwSeguridadGateway [codigo=" + codigo + ", clave=" + clave + ", fechaCreacion=" + fechaCreacion
                + ", fechaActivacion=" + fechaActivacion + ", estado=" + estado + "]";
    }

}
