package ec.edu.espe.gateway.comision.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity
@Table(name = "GTW_COMISION")
public class Comision implements Serializable {

    @Id
    @Column(name = "COD_COMISION", nullable = false)
    private Integer codigo;
    @NotNull
    @Column(name = "TIPO", length = 3, nullable = false)
    private String tipo;
    @NotNull
    @Column(name = "MONTO_BASE", precision = 20, scale = 4, nullable = false)
    private BigDecimal montoBase;
    @NotNull
    @Column(name = "TRANSACCIONES_BASE", precision = 9, nullable = false)
    private BigDecimal transaccionesBase;
    @NotNull
    @Column(name = "MANEJA_SEGMENTOS", nullable = false)
    private Boolean manejaSegmentos;

    // Empty constructor
    public Comision() {
    }

    // Constructor with COD_COMISION
    public Comision(Integer codigo) {
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
        if (!"POR".equals(tipo) && !"FIJ".equals(tipo)) {
            throw new IllegalArgumentException("El tipo de comisión debe ser 'POR' o 'FIJ'.");
        }
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
        Comision other = (Comision) obj;
        if (codigo == null) {
            if (other.codigo != null)
                return false;
        } else if (!codigo.equals(other.codigo))
            return false;
        return true;
    }

}