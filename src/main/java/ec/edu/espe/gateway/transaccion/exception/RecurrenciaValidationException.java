package ec.edu.espe.gateway.transaccion.exception;

public class RecurrenciaValidationException extends RuntimeException {
    
    private final String campo;
    private final String mensaje;

    public RecurrenciaValidationException(String campo, String mensaje) {
        super();
        this.campo = campo;
        this.mensaje = mensaje;
    }

    @Override
    public String getMessage() {
        return "Error de validación en recurrencia - campo " + this.campo + ": " + this.mensaje;
    }
} 