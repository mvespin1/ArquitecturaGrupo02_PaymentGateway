package ec.edu.espe.gateway.transaccion.exception;

public class RecurrenciaStateException extends RuntimeException {
    
    private final String estadoActual;
    private final String operacion;

    public RecurrenciaStateException(String estadoActual, String operacion) {
        super();
        this.estadoActual = estadoActual;
        this.operacion = operacion;
    }

    @Override
    public String getMessage() {
        return "Operación " + this.operacion + " no permitida para recurrencia en estado: " + this.estadoActual;
    }
} 