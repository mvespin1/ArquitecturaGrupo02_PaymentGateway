package ec.edu.espe.pos.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "POS_TRANSACCION")
public class Transaccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TRANSACCION", nullable = false)
    private Integer codigo;
    @NotNull(message = "El tipo de transacción no puede ser nulo")
    @Column(name = "TIPO", length = 3, nullable = false)
    private String tipo;
    @NotNull(message = "La marca no puede ser nula")
    @Column(name = "MARCA", length = 4, nullable = false)
    private String marca;
    @NotNull(message = "La modalidad no puede ser nula")
    @Column(name = "MODALIDAD", length = 3, nullable = false)
    private String modalidad;
    @NotNull(message = "El detalle no puede ser nulo")
    @Column(name = "DETALLE", length = 50, nullable = false)
    private String detalle;
    @NotNull(message = "El monto no puede ser nulo")
    @Column(name = "MONTO", precision = 20, scale = 2, nullable = false)
    private BigDecimal monto;
    @NotNull(message = "El código único de transacción no puede ser nulo")
    @Column(name = "CODIGO_UNICO_TRANSACCION", length = 64, nullable = false)
    private String codigoUnicoTransaccion;
    @NotNull(message = "La fecha no puede ser nula")
    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;
    @Column(name = "ESTADO", length = 3)
    private String estado;
    @Column(name = "ESTADO_RECIBO", length = 3)
    private String estadoRecibo;
    @NotNull(message = "La moneda no puede ser nula")
    @Column(name = "MONEDA", length = 3, nullable = false)
    private String moneda;

    @Transient
    private String codigoComercioPOS;
    @Transient
    private String tarjeta;

    public Transaccion() {
    }

    public Transaccion(Integer codigo) {
        this.codigo = codigo;
    }

    // Getters y Setters

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
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

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoRecibo() {
        return estadoRecibo;
    }

    public void setEstadoRecibo(String estadoRecibo) {
        this.estadoRecibo = estadoRecibo;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCodigoComercioPOS() {
        return codigoComercioPOS;
    }

    public void setCodigoComercioPOS(String codigoComercioPOS) {
        this.codigoComercioPOS = codigoComercioPOS;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
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
        Transaccion other = (Transaccion) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PosTransaccion{" +
                "codigo=" + codigo +
                ", tipo='" + tipo + '\'' +
                ", marca='" + marca + '\'' +
                ", modalidad='" + modalidad + '\'' +
                ", detalle='" + detalle + '\'' +
                ", monto=" + monto +
                ", codigoUnicoTransaccion='" + codigoUnicoTransaccion + '\'' +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", estadoRecibo='" + estadoRecibo + '\'' +
                ", moneda='" + moneda + '\'' +
                ", codigoComercioPOS='" + codigoComercioPOS + '\'' +
                ", tarjeta='" + tarjeta + '\'' +
                '}';
    }

    public String getEncryptedData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEncryptedData'");
    }
}