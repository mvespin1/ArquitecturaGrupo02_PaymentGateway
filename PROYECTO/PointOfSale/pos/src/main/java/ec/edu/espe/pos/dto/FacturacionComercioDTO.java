package ec.edu.espe.pos.dto;

public class FacturacionComercioDTO {
    private Integer codigo;

    public FacturacionComercioDTO() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "FacturacionComercioDTO{" +
                "codigo=" + codigo +
                '}';
    }
}
