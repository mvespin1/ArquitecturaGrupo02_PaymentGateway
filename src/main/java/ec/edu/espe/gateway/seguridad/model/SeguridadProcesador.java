package ec.edu.espe.gateway.seguridad.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GTW_SEGURIDAD_PROCESADOR")
public class SeguridadProcesador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_SEGURIDAD_PROCESADOR", nullable = false)
    private Integer codigo;

    @NotNull
    @Column(name = "CLAVE", length = 128, nullable = false)
    private String clave;

    @NotNull
    @Column(name = "FECHA_ACTUALIZACION", nullable = false)
    private LocalDateTime fechaActualizacion;

    @NotNull
    @Column(name = "FECHA_ACTIVACION", nullable = false)
    private LocalDate fechaActivacion;

    @NotNull
    @Column(name = "ESTADO", length = 3, nullable = false)
    private String estado;

    public SeguridadProcesador(Integer codigo) {
        this.codigo = codigo;
    }
}
