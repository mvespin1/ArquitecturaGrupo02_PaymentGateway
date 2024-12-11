package ec.edu.espe.pos.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "POS_CONFIGURACION")
public class POSConfiguracion implements Serializable {

    @EmbeddedId
    private POSConfiguracionPK pk;

    @Column(name = "DIRECCION_MAC", length = 32, nullable = false)
    private String direccionMac;

    @Column(name = "CODIGO_COMERCIO", length = 10, nullable = false)
    private String codigoComercio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ACTIVACION", nullable = false)
    private Date fechaActivacion;

    public POSConfiguracion() {
    }

    public POSConfiguracion(POSConfiguracionPK pk) {
        this.pk = pk;
    }

    public POSConfiguracionPK getPk() {
        return pk;
    }

    public void setPk(POSConfiguracionPK pk) {
        this.pk = pk;
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
        result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
        if (pk == null) {
            if (other.pk != null)
                return false;
        } else if (!pk.equals(other.pk))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "POSConfiguracion [pk=" + pk + ", direccionMac=" + direccionMac + ", codigoComercio=" + codigoComercio
                + ", fechaActivacion=" + fechaActivacion + "]";
    }

}