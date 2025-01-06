package ec.edu.espe.gateway.transaccion.model;

public class RespuestaTransaccionDTO {
    private String mensaje;

    public RespuestaTransaccionDTO() {
    }

    public RespuestaTransaccionDTO(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
} 