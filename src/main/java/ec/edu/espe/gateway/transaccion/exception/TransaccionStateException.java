package ec.edu.espe.gateway.transaccion.exception;

public class TransaccionStateException extends RuntimeException {
    
    private final String estadoActual;
    private final String operacion;

    public TransaccionStateException(String estadoActual, String operacion) {
        super();
        this.estadoActual = estadoActual;
        this.operacion = operacion;
    }

    @Override
    public String getMessage() {
        return "Operación " + this.operacion + " no permitida para transacción en estado: " + this.estadoActual;
    }
} 