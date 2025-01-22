package ec.edu.espe.gateway.seguridad.exception;

public class NotFoundException extends RuntimeException {

    private final String data;
    private final String entity;

    public NotFoundException(String data, String entity) {
        super();
        this.data = data;
        this.entity = entity;
    }

    @Override
    public String getMessage() {
        return "No se encontro ninguna coincidencia para: "+this.entity+", con el dato:"+data;
    }
}
