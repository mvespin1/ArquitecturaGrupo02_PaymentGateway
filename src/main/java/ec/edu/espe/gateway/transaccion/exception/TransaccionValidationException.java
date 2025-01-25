package ec.edu.espe.gateway.transaccion.exception;

public class TransaccionValidationException extends RuntimeException {
    
    private final String campo;
    private final String mensaje;

    public TransaccionValidationException(String campo, String mensaje) {
        super();
        this.campo = campo;
        this.mensaje = mensaje;
    }

    @Override
    public String getMessage() {
        return "Error de validación en el campo " + this.campo + ": " + this.mensaje;
    }
} 