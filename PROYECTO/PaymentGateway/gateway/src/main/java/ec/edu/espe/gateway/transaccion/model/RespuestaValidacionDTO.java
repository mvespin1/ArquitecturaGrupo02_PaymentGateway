package ec.edu.espe.gateway.transaccion.model;

public class RespuestaValidacionDTO {
    private String mensaje;

    public RespuestaValidacionDTO() {
    }

    public RespuestaValidacionDTO(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
} 