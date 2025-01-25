package ec.edu.espe.gateway.transaccion.exception;

public class TransaccionNotFoundException extends RuntimeException {
    
    private final String codigoTransaccion;

    public TransaccionNotFoundException(String codigoTransaccion) {
        super();
        this.codigoTransaccion = codigoTransaccion;
    }

    @Override
    public String getMessage() {
        return "No se encontró la transacción con código: " + this.codigoTransaccion;
    }
} 