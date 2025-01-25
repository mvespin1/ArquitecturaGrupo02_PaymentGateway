package ec.edu.espe.gateway.comercio.model;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PosComercioPK implements Serializable {
    
    @NotNull
    @Column(name = "MODELO", length = 10, nullable = false)
    private String modelo;
    
    @NotNull
    @Column(name = "CODIGO_POS", length = 10, nullable = false)
    private String codigo;
}
