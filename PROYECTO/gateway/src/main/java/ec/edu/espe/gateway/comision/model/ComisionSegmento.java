package ec.edu.espe.gateway.comision.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GTW_COMISION_SEGMENTO")
public class ComisionSegmento implements Serializable {

    @EmbeddedId
    private ComisionSegmentoPK pk;

    @NotNull
    @Column(name = "TRANSACCIONES_HASTA", nullable = false)
    private Integer transaccionesHasta;

    @NotNull
    @Column(name = "MONTO", precision = 20, scale = 4, nullable = false)
    private BigDecimal monto;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "COD_COMISION", referencedColumnName = "COD_COMISION", nullable = false, insertable = false, updatable = false)
    private Comision comision;

    public ComisionSegmento(ComisionSegmentoPK pk) {
        this.pk = pk;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
        ComisionSegmento other = (ComisionSegmento) obj;
        if (pk == null) {
            if (other.pk != null)
                return false;
        } else if (!pk.equals(other.pk))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GtwComisionSegmento [pk=" + pk + ", transaccionesHasta=" + transaccionesHasta + ", monto=" + monto
                + ", comision=" + comision + "]";
    }

}
