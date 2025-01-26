package ec.edu.espe.gateway.exception;

public class ValidationException extends RuntimeException {

    private final String tipo;
    private final String mensaje;

    public ValidationException(String tipo, String mensaje) {
        super();
        this.tipo = tipo;
        this.mensaje = mensaje;
    }

    @Override
    public String getMessage() {
        return "Tipo: " + tipo + " - " + mensaje;
    }
} 