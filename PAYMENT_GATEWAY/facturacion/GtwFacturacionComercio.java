package ec.edu.espe.facturacion.model;

import jakarta.persistence.*;
import java.util.Date;
import java.math.BigDecimal;

@Entity
@Table(name = "GTW_FACTURACION_COMERCIO")
public class GtwFacturacionComercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_FACTURACION_COMERCIO", nullable = false)
    private Integer codigo;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_INICIO", nullable = false)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FIN", nullable = false)
    private Date fechaFin;

    @Column(name = "TRANSACCIONES_PROCESADAS", precision = 9, nullable = false)
    private BigDecimal transaccionesProcesadas;

    @Column(name = "TRANSACCIONES_AUTORIZADAS", precision = 9, nullable = false)
    private BigDecimal transaccionesAutorizadas;

    @Column(name = "TRANSACCIONES_RECHAZADAS", precision = 9, nullable = false)
    private BigDecimal transaccionesRechazadas;

    @Column(name = "TRANSACCIONES_REVERSADAS", precision = 9, nullable = false)
    private BigDecimal transaccionesReversadas;

    @Column(name = "VALOR", precision = 20, scale = 4, nullable = false)
    private BigDecimal valor;

    @Column(name = "ESTADO", length = 3, nullable = false)
    private String estado;

    @Column(name = "CODIGO_FACTURACION", length = 20, nullable = false)
    private String codigoFacturacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_FACTURACION", nullable = false)
    private Date fechaFacturacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA_PAGO", nullable = false)
    private Date fechaPago;

    @ManyToOne
    @JoinColumn(name = "COD_COMERCIO", nullable = false)
    private GtwComercio comercio;

    @ManyToOne
    @JoinColumn(name = "COD_COMISION", nullable = false)
    private GtwComision comision;

    // Empty Constructor
    public GtwFacturacionComercio() {
    }

    // Constructor with COD_FACTURACION_COMERCIO
    public GtwFacturacionComercio(Integer codigo) {
        this.codigo = codigo;
    }

    // Getters and Setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getTransaccionesProcesadas() {
        return transaccionesProcesadas;
    }

    public void setTransaccionesProcesadas(BigDecimal transaccionesProcesadas) {
        this.transaccionesProcesadas = transaccionesProcesadas;
    }

    public BigDecimal getTransaccionesAutorizadas() {
        return transaccionesAutorizadas;
    }

    public void setTransaccionesAutorizadas(BigDecimal transaccionesAutorizadas) {
        this.transaccionesAutorizadas = transaccionesAutorizadas;
    }

    public BigDecimal getTransaccionesRechazadas() {
        return transaccionesRechazadas;
    }

    public void setTransaccionesRechazadas(BigDecimal transaccionesRechazadas) {
        this.transaccionesRechazadas = transaccionesRechazadas;
    }

    public BigDecimal getTransaccionesReversadas() {
        return transaccionesReversadas;
    }

    public void setTransaccionesReversadas(BigDecimal transaccionesReversadas) {
        this.transaccionesReversadas = transaccionesReversadas;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigoFacturacion() {
        return codigoFacturacion;
    }

    public void setCodigoFacturacion(String codigoFacturacion) {
        this.codigoFacturacion = codigoFacturacion;
    }

    public Date getFechaFacturacion() {
        return fechaFacturacion;
    }

    public void setFechaFacturacion(Date fechaFacturacion) {
        this.fechaFacturacion = fechaFacturacion;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public GtwComercio getComercio() {
        return comercio;
    }

    public void setComercio(GtwComercio comercio) {
        this.comercio = comercio;
    }

    public GtwComision getComision() {
        return comision;
    }

    public void setComision(GtwComision comision) {
        this.comision = comision;
    }

    // ToString

    @Override
    public String toString() {
        return "GtwFacturacionComercio [codigo=" + codigo + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin
                + ", transaccionesProcesadas=" + transaccionesProcesadas + ", transaccionesAutorizadas="
                + transaccionesAutorizadas + ", transaccionesRechazadas=" + transaccionesRechazadas
                + ", transaccionesReversadas=" + transaccionesReversadas + ", valor=" + valor + ", estado=" + estado
                + ", codigoFacturacion=" + codigoFacturacion + ", fechaFacturacion=" + fechaFacturacion + ", fechaPago="
                + fechaPago + ", comercio=" + comercio + ", comision=" + comision + "]";
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
        GtwFacturacionComercio other = (GtwFacturacionComercio) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

}
