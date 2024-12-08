package ec.edu.espe.comercios.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GTW_POS_COMERCIO")
@IdClass(GtwPosComercioPK.class)
public class GtwPosComercio implements Serializable {

    @Id
    @Column(name = "CODIGO_POS",length = 10 , nullable = false)
    private String codigo;

    @Id
    @Column(name = "MODELO",length = 10 , nullable = false)
    private String modelo;

    @Column(name = "DIRECCION_MAC",length = 32 , nullable = false)
    private String direccionMac;

    @Column(name = "ESTADO",length = 3 , nullable = false)
    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ACTIVACION", nullable = false)
    private Date fechaActivacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ULTIMO_USO", nullable = false)
    private Date ultimoUso;

    @ManyToOne
    @JoinColumn(name = "COD_COMERCIO", nullable = false)
    private GtwComercio gtwComercio;

    // Constructor, Getters and Setters
    public GtwPosComercio() {
    }

    public GtwPosComercio(String codigo) {
        this.codigo = codigo;
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

    public String getDireccionMac() {
        return direccionMac;
    }

    public void setDireccionMac(String direccionMac) {
        this.direccionMac = direccionMac;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaActivacion() {
        return fechaActivacion;
    }

    public void setFechaActivacion(Date fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }

    public Date getUltimoUso() {
        return ultimoUso;
    }

    public void setUltimoUso(Date ultimoUso) {
        this.ultimoUso = ultimoUso;
    }

    public GtwComercio getGtwComercio() {
        return gtwComercio;
    }

    public void setGtwComercio(GtwComercio gtwComercio) {
        this.gtwComercio = gtwComercio;
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
        GtwPosComercio other = (GtwPosComercio) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

    // ToString
    @Override
    public String toString() {
        return "GtwPosComercio [codigo=" + codigo + ", modelo=" + modelo + ", direccionMac=" + direccionMac
                + ", estado=" + estado + ", fechaActivacion=" + fechaActivacion + ", ultimoUso=" + ultimoUso
                + ", gtwComercio=" + gtwComercio + "]";
    }

}