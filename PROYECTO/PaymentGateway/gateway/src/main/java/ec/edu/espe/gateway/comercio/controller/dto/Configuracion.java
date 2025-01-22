package ec.edu.espe.gateway.comercio.controller.dto;

import java.time.LocalDateTime;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;

public class Configuracion {

    private PosComercioPK pk;
    private String direccionMac;
    private Integer codigoComercio;
    private LocalDateTime fechaActivacion;

    public Configuracion() {
    }

    public Configuracion(PosComercioPK pk) {
        this.pk = pk;
    }

    public PosComercioPK getPk() {
        return pk;
    }

    public void setPk(PosComercioPK pk) {
        this.pk = pk;
    }

    public String getDireccionMac() {
        return direccionMac;
    }

    public void setDireccionMac(String direccionMac) {
        this.direccionMac = direccionMac;
    }

    public Integer getCodigoComercio() {
        return codigoComercio;
    }

    public void setCodigoComercio(Integer codigoComercio) {
        this.codigoComercio = codigoComercio;
    }

    public LocalDateTime getFechaActivacion() {
        return fechaActivacion;
    }

    public void setFechaActivacion(LocalDateTime fechaActivacion) {
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
        Configuracion other = (Configuracion) obj;
        if (pk == null) {
            if (other.pk != null)
                return false;
        } else if (!pk.equals(other.pk))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ConfiguracionDTO [pk=" + pk + ", direccionMac=" + direccionMac + ", codigoComercio=" + codigoComercio
                + ", fechaActivacion=" + fechaActivacion + "]";
    }

}