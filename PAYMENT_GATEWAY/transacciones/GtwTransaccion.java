package ec.edu.espe.transacciones.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GTW_TRANSACCION")
public class GtwTransaccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TRANSACCION", nullable = false)
    private Integer codigo;

    @ManyToOne
    @JoinColumn(name = "COD_COMERCIO")
    private GtwComercio comercio;

    @ManyToOne
    @JoinColumn(name = "COD_FACTURACION_COMERCIO")
    private GtwFacturacionComercio facturacionComercio;

    @Column(name = "TIPO", length = 3, nullable = false)
    private String tipo;

    @Column(name = "MARCA", length = 4, nullable = false)
    private String marca;

    @Column(name = "DETALLE", length = 50, nullable = false)
    private String detalle;

    @Column(name = "MONTO", precision = 20, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "CODIGO_UNICO_TRANSACCION", length = 64, nullable = false)
    private String codigoUnicoTransaccion;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA", nullable = false)
    private Date fecha;

    @Column(name = "ESTADO", length = 3, nullable = false)
    private String estado;

    @Column(name = "MONEDA", length = 3, nullable = false)
    private String moneda;

    @Column(name = "PAIS", length = 2, nullable = false)
    private String pais;

    @Column(name = "TARJETA", length = 256, nullable = false)
    private String tarjeta;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_EJECUCION_RECURRENCIA")
    private Date fechaEjecucionRecurrencia;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FIN_RECURRENCIA")
    private Date fechaFinRecurrencia;

    // Empty Constructor

    public GtwTransaccion() {
    }

    // Constructor with codigo
    public GtwTransaccion(Integer codigo) {
        this.codigo = codigo;
    }

    // Getters and Setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public GtwComercio getComercio() {
        return comercio;
    }

    public void setComercio(GtwComercio comercio) {
        this.comercio = comercio;
    }

    public GtwFacturacionComercio getFacturacionComercio() {
        return facturacionComercio;
    }

    public void setFacturacionComercio(GtwFacturacionComercio facturacionComercio) {
        this.facturacionComercio = facturacionComercio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getCodigoUnicoTransaccion() {
        return codigoUnicoTransaccion;
    }

    public void setCodigoUnicoTransaccion(String codigoUnicoTransaccion) {
        this.codigoUnicoTransaccion = codigoUnicoTransaccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Date getFechaEjecucionRecurrencia() {
        return fechaEjecucionRecurrencia;
    }

    public void setFechaEjecucionRecurrencia(Date fechaEjecucionRecurrencia) {
        this.fechaEjecucionRecurrencia = fechaEjecucionRecurrencia;
    }

    public Date getFechaFinRecurrencia() {
        return fechaFinRecurrencia;
    }

    public void setFechaFinRecurrencia(Date fechaFinRecurrencia) {
        this.fechaFinRecurrencia = fechaFinRecurrencia;
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
        GtwTransaccion other = (GtwTransaccion) obj;
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
        return "GtwTransaccion [codigo=" + codigo + ", comercio=" + comercio + ", facturacionComercio="
                + facturacionComercio + ", tipo=" + tipo + ", marca=" + marca + ", detalle=" + detalle + ", monto="
                + monto + ", codigoUnicoTransaccion=" + codigoUnicoTransaccion + ", fecha=" + fecha + ", estado="
                + estado + ", moneda=" + moneda + ", pais=" + pais + ", tarjeta=" + tarjeta
                + ", fechaEjecucionRecurrencia=" + fechaEjecucionRecurrencia + ", fechaFinRecurrencia="
                + fechaFinRecurrencia + "]";
    }

}
