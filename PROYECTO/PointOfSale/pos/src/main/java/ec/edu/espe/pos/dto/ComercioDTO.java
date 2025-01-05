package ec.edu.espe.pos.dto;

public class ComercioDTO {
    private Integer codigo;

    public ComercioDTO() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "ComercioDTO{" +
                "codigo=" + codigo +
                '}';
    }
}