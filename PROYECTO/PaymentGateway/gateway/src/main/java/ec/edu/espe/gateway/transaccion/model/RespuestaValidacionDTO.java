package ec.edu.espe.gateway.transaccion.model;

public class RespuestaValidacionDTO {
    private String mensaje;
    private Integer codigoRespuesta;

    public RespuestaValidacionDTO() {
    }

    public RespuestaValidacionDTO(String mensaje, Integer codigoRespuesta) {
        this.mensaje = mensaje;
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(Integer codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }
} 