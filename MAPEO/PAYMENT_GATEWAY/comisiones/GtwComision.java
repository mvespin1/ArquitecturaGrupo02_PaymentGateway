package ec.edu.espe.comisiones.model;

import jakarta.persistence.*;
import scala.math.BigDecimal;

import java.io.Serializable;

@Entity
@Table(name = "GTW_COMISION")
public class GtwComision implements Serializable {

    @Id
    @Column(name = "COD_COMISION", nullable = false)
    private Integer codigo;

    @Column(name = "TIPO", length = 3, nullable = false)
    private String tipo;

    @Column(name = "MONTO_BASE", precision = 20, scale = 4, nullable = false)
    private BigDecimal montoBase;

    @Column(name = "TRANSACCIONES_BASE", precision = 9, nullable = false)
    private BigDecimal transaccionesBase;

    @Column(name = "MANEJA_SEGMENTOS", nullable = false)
    private Boolean manejaSegmentos;

    // Empty constructor
    public GtwComision() {
    }

    // Constructor with COD_COMISION
    public GtwComision(Integer codigo) {
        this.codigo = codigo;
    }

    // Getters and Setters
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

    public BigDecimal getMontoBase() {
        return montoBase;
    }

    public void setMontoBase(BigDecimal montoBase) {
        this.montoBase = montoBase;
    }

    public BigDecimal getTransaccionesBase() {
        return transaccionesBase;
    }

    public void setTransaccionesBase(BigDecimal transaccionesBase) {
        this.transaccionesBase = transaccionesBase;
    }

    public Boolean getManejaSegmentos() {
        return manejaSegmentos;
    }

    public void setManejaSegmentos(Boolean manejaSegmentos) {
        this.manejaSegmentos = manejaSegmentos;
    }

    // ToString
    @Override
    public String toString() {
        return "GtwComision{" +
                "codigo=" + codigo +
                ", tipo='" + tipo + '\'' +
                ", montoBase=" + montoBase +
                ", transaccionesBase=" + transaccionesBase +
                ", manejaSegmentos=" + manejaSegmentos +
                '}';
    }

    // Equals and HashCode

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
        GtwComision other = (GtwComision) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

}