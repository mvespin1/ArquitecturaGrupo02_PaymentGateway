package ec.edu.espe.gateway.transaccion.exception;

public class RecurrenciaNotFoundException extends RuntimeException {
    
    private final String codigoRecurrencia;

    public RecurrenciaNotFoundException(String codigoRecurrencia) {
        super();
        this.codigoRecurrencia = codigoRecurrencia;
    }

    @Override
    public String getMessage() {
        return "No se encontró la recurrencia con código: " + this.codigoRecurrencia;
    }
} 