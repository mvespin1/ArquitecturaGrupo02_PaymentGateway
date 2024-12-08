package ec.edu.espe.pos.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "POS_CONFIGURACION")
@IdClass(PosConfiguracionPK.class)
public class POSConfiguracion implements Serializable {
    @Id
    @Column(name = "CODIGO_POS", length = 10, nullable = false)
    private String codigo;

    @Id
    @Column(name = "MODELO", length = 10, nullable = false)
    private String modelo;

    @Column(name = "DIRECCION_MAC", length = 32, nullable = false)
    private String direccionMac;

    @Column(name = "CODIGO_COMERCIO", length = 10, nullable = false)
    private String codigoComercio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ACTIVACION", nullable = false)
    private Date fechaActivacion;

    public POSConfiguracion() {
    }

    public POSConfiguracion(String codigo, String modelo) {
        this.codigo = codigo;
        this.modelo = modelo;
    }

    public String getcodigo() {
        return codigo;
    }

    public void setcodigo(String codigo) {
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

    public String getCodigoComercio() {
        return codigoComercio;
    }

    public void setCodigoComercio(String codigoComercio) {
        this.codigoComercio = codigoComercio;
    }

    public Date getFechaActivacion() {
        return fechaActivacion;
    }

    public void setFechaActivacion(Date fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
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
        POSConfiguracion other = (POSConfiguracion) obj;
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
        return "POSConfiguracion{" +
                "codigo='" + codigo + '\'' +
                ", modelo='" + modelo + '\'' +
                ", direccionMac='" + direccionMac + '\'' +
                ", codigoComercio='" + codigoComercio + '\'' +
                ", fechaActivacion=" + fechaActivacion +
                '}';
    }
}