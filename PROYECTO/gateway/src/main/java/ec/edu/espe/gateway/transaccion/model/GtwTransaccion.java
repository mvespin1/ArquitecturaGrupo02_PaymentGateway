package ec.edu.espe.gateway.transaccion.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

import java.io.Serializable;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

import ec.edu.espe.gateway.facturacion.model.GtwFacturacionComercio;
import ec.edu.espe.gateway.comercio.model.GtwComercio;

@Entity
@Table(name = "GTW_TRANSACCION")
public class GtwTransaccion implements Serializable {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TRANSACCION", nullable = false)
    private Integer codigo;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_COMERCIO", referencedColumnName = "COD_COMERCIO", nullable = false, insertable = false, updatable = false)
    private GtwComercio comercio;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_FACTURACION_COMERCIO", referencedColumnName = "COD_FACTURACION_COMERCIO", nullable = false, insertable = false, updatable = false)
    private GtwFacturacionComercio facturacionComercio;
    @NotNull
    @Column(name = "TIPO", length = 3, nullable = false)
    private String tipo;
    @NotNull
    @Column(name = "MARCA", length = 4, nullable = false)
    private String marca;
    @NotNull
    @Column(name = "DETALLE", length = 50, nullable = false)
    private String detalle;
    @NotNull
    @Column(name = "MONTO", precision = 20, scale = 2, nullable = false)
    private BigDecimal monto;
    @NotNull
    @Column(name = "CODIGO_UNICO_TRANSACCION", length = 64, nullable = false)
    private String codigoUnicoTransaccion;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;
    @NotNull
    @Column(name = "ESTADO", length = 3, nullable = false)
    private String estado;
    @NotNull
    @Column(name = "MONEDA", length = 3, nullable = false)
    private String moneda;
    @NotNull
    @Column(name = "PAIS", length = 2, nullable = false)
    private String pais;
    @NotNull
    @Column(name = "TARJETA", length = 256, nullable = false)
    private String tarjeta;
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_EJECUCION_RECURRENCIA")
    private LocalDate fechaEjecucionRecurrencia;
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FIN_RECURRENCIA")
    private LocalDate fechaFinRecurrencia;

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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
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

    public LocalDate getFechaEjecucionRecurrencia() {
        return fechaEjecucionRecurrencia;
    }

    public void setFechaEjecucionRecurrencia(LocalDate fechaEjecucionRecurrencia) {
        this.fechaEjecucionRecurrencia = fechaEjecucionRecurrencia;
    }

    public LocalDate getFechaFinRecurrencia() {
        return fechaFinRecurrencia;
    }

    public void setFechaFinRecurrencia(LocalDate fechaFinRecurrencia) {
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
