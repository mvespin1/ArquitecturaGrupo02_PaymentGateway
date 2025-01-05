package ec.edu.espe.pos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import ec.edu.espe.pos.model.Transaccion;

public class GatewayTransaccionDTO {
    private Integer codigo;
    private String tipo;
    private String marca;
    private String modalidad;
    private String detalle;
    private BigDecimal monto;
    private String codigoUnicoTransaccion;
    private LocalDateTime fecha;
    private String estado;
    private String moneda;
    private GatewayComercioPosDTO comercio;

    public GatewayTransaccionDTO() {
    }

    public void setCodigo(Integer codigo) { this.codigo = codigo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public void setCodigoUnicoTransaccion(String codigoUnicoTransaccion) { this.codigoUnicoTransaccion = codigoUnicoTransaccion; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public void setComercio(GatewayComercioPosDTO comercio) { this.comercio = comercio; }

    public static GatewayTransaccionDTO fromTransaccion(Transaccion transaccion, Integer idComercio) {
        GatewayTransaccionDTO dto = new GatewayTransaccionDTO();
        dto.setCodigo(transaccion.getCodigo());
        dto.setTipo(transaccion.getModalidad());
        dto.setMarca(transaccion.getMarca());
        dto.setModalidad(transaccion.getModalidad());
        dto.setDetalle(transaccion.getDetalle());
        dto.setMonto(transaccion.getMonto());
        dto.setCodigoUnicoTransaccion(transaccion.getCodigoUnicoTransaccion());
        dto.setFecha(transaccion.getFecha());
        dto.setEstado(transaccion.getEstado());
        dto.setMoneda(transaccion.getMoneda());
        dto.setComercio(new GatewayComercioPosDTO(idComercio));
        return dto;
    }

    public Integer getCodigo() { return codigo; }
    public String getTipo() { return tipo; }
    public String getMarca() { return marca; }
    public String getModalidad() { return modalidad; }
    public String getDetalle() { return detalle; }
    public BigDecimal getMonto() { return monto; }
    public String getCodigoUnicoTransaccion() { return codigoUnicoTransaccion; }
    public LocalDateTime getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public String getMoneda() { return moneda; }
    public GatewayComercioPosDTO getComercio() { return comercio; }
}

class GatewayComercioPosDTO {
    private Integer codigo;
    private String codigoInterno;
    private String estado = "ACT"; // Valor por defecto

    public GatewayComercioPosDTO(Integer codigo) {
        this.codigo = codigo;
    }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}