package ec.edu.espe.gateway.facturacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.math.BigDecimal;

import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comision.model.Comision;

@Entity
@Table(name = "GTW_FACTURACION_COMERCIO")
public class FacturacionComercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_FACTURACION_COMERCIO", nullable = false)
    private Integer codigo;
    @NotNull
    @Column(name = "FECHA_INICIO", nullable = false)
    private LocalDate fechaInicio;
    @NotNull
    @Column(name = "FECHA_FIN", nullable = false)
    private LocalDate fechaFin;
    @NotNull
    @Column(name = "TRANSACCIONES_PROCESADAS", precision = 9, nullable = false)
    private Integer transaccionesProcesadas;
    @NotNull
    @Column(name = "TRANSACCIONES_AUTORIZADAS", precision = 9, nullable = false)
    private Integer transaccionesAutorizadas;
    @NotNull
    @Column(name = "TRANSACCIONES_RECHAZADAS", precision = 9, nullable = false)
    private Integer transaccionesRechazadas;
    @NotNull
    @Column(name = "TRANSACCIONES_REVERSADAS", precision = 9, nullable = false)
    private Integer transaccionesReversadas;
    @NotNull
    @Column(name = "VALOR", precision = 20, scale = 4, nullable = false)
    private BigDecimal valor;
    @NotNull
    @Column(name = "ESTADO", length = 3, nullable = false)
    private String estado;
    @NotNull
    @Column(name = "CODIGO_FACTURACION", length = 20, nullable = false)
    private String codigoFacturacion;
    @NotNull
    @Column(name = "FECHA_FACTURACION", nullable = false)
    private LocalDate fechaFacturacion;
    @NotNull
    @Column(name = "FECHA_PAGO", nullable = false)
    private LocalDate fechaPago;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_COMERCIO", nullable = false)
    private Comercio comercio;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_COMISION", nullable = false)
    private Comision comision;

    // Empty Constructor
    public FacturacionComercio() {
    }

    // Constructor with COD_FACTURACION_COMERCIO
    public FacturacionComercio(Integer codigo) {
        this.codigo = codigo;
    }

    // Getters and Setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getTransaccionesProcesadas() {
        return transaccionesProcesadas;
    }

    public void setTransaccionesProcesadas(Integer transaccionesProcesadas) {
        this.transaccionesProcesadas = transaccionesProcesadas;
    }

    public Integer getTransaccionesAutorizadas() {
        return transaccionesAutorizadas;
    }

    public void setTransaccionesAutorizadas(Integer transaccionesAutorizadas) {
        this.transaccionesAutorizadas = transaccionesAutorizadas;
    }

    public Integer getTransaccionesRechazadas() {
        return transaccionesRechazadas;
    }

    public void setTransaccionesRechazadas(Integer transaccionesRechazadas) {
        this.transaccionesRechazadas = transaccionesRechazadas;
    }

    public Integer getTransaccionesReversadas() {
        return transaccionesReversadas;
    }

    public void setTransaccionesReversadas(Integer transaccionesReversadas) {
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

    public LocalDate getFechaFacturacion() {
        return fechaFacturacion;
    }

    public void setFechaFacturacion(LocalDate fechaFacturacion) {
        this.fechaFacturacion = fechaFacturacion;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public Comision getComision() {
        return comision;
    }

    public void setComision(Comision comision) {
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
        FacturacionComercio other = (FacturacionComercio) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

}
