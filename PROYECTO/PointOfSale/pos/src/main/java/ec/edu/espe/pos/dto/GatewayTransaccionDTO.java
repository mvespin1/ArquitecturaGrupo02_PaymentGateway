package ec.edu.espe.pos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class GatewayTransaccionDTO {
    private ComercioDTO comercio;
    private FacturacionComercioDTO facturacionComercio;
    private String tipo;
    private String marca;
    private String detalle;
    private BigDecimal monto;
    private String codigoUnicoTransaccion;
    private LocalDateTime fecha;
    private String estado;
    private String moneda;
    private String pais;
    private String tarjeta;
    private String codigoPos;
    private String modeloPos;
    private Boolean interesDiferido;
    private Integer cuotas;

    // Getters y Setters
    public ComercioDTO getComercio() {
        return comercio;
    }

    public void setComercio(ComercioDTO comercio) {
        this.comercio = comercio;
    }

    public FacturacionComercioDTO getFacturacionComercio() {
        return facturacionComercio;
    }

    public void setFacturacionComercio(FacturacionComercioDTO facturacionComercio) {
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

    public String getCodigoPos() {
        return codigoPos;
    }

    public void setCodigoPos(String codigoPos) {
        this.codigoPos = codigoPos;
    }

    public String getModeloPos() {
        return modeloPos;
    }

    public void setModeloPos(String modeloPos) {
        this.modeloPos = modeloPos;
    }

    public Boolean getInteresDiferido() {
        return interesDiferido;
    }

    public void setInteresDiferido(Boolean interesDiferido) {
        this.interesDiferido = interesDiferido;
    }

    public Integer getCuotas() {
        return cuotas;
    }

    public void setCuotas(Integer cuotas) {
        this.cuotas = cuotas;
    }
}
