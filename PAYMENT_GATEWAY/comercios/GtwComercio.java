package ec.edu.espe.comercios.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "GTW_COMERCIO")
public class GtwComercio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_COMERCIO", nullable = false)
    private Integer codigo;

    @Column(name = "CODIGO_INTERNO", length = 10, nullable = false)
    private String codigoInterno;

    @Column(name = "RUC", length = 13, nullable = false)
    private String ruc;

    @Column(name = "RAZON_SOCIAL", length = 100, nullable = false)
    private String razonSocial;

    @Column(name = "NOMBRE_COMERCIAL", length = 100, nullable = false)
    private String nombreComercial;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION", nullable = false)
    private Date fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "COD_COMISION", nullable = false)
    private GtwComision comision;

    @Column(name = "PAGOS_ACEPTADOS", length = 3, nullable = false)
    private String pagosAceptados;

    @Column(name = "ESTADO", length = 3, nullable = false)
    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ACTIVACION")
    private Date fechaActivacion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_SUSPENSION")
    private Date fechaSuspension;

    public GtwComercio() {
    }

    public GtwComercio(Integer codigo) {
        this.codigo = codigo;
    }

    // Getters y Setters

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public GtwComision getComision() {
        return comision;
    }

    public void setComision(GtwComision comision) {
        this.comision = comision;
    }

    public String getPagosAceptados() {
        return pagosAceptados;
    }

    public void setPagosAceptados(String pagosAceptados) {
        this.pagosAceptados = pagosAceptados;
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

    public Date getFechaSuspension() {
        return fechaSuspension;
    }

    public void setFechaSuspension(Date fechaSuspension) {
        this.fechaSuspension = fechaSuspension;
    }

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
        GtwComercio other = (GtwComercio) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GtwComercio [codigo=" + codigo + ", codigoInterno=" + codigoInterno + ", ruc=" + ruc + ", razonSocial="
                + razonSocial + ", nombreComercial=" + nombreComercial + ", fechaCreacion=" + fechaCreacion
                + ", comision=" + comision + ", pagosAceptados=" + pagosAceptados + ", estado=" + estado
                + ", fechaActivacion=" + fechaActivacion + ", fechaSuspension=" + fechaSuspension + "]";
    }

    
}
