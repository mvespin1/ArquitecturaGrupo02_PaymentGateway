package ec.edu.espe.gateway.exception;

public class StateException extends RuntimeException {

    private final String estado;
    private final String mensaje;

    public StateException(String estado, String mensaje) {
        super();
        this.estado = estado;
        this.mensaje = mensaje;
    }

    @Override
    public String getMessage() {
        return "Estado: " + estado + " - " + mensaje;
    }
} 