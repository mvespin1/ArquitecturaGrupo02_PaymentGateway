package ec.edu.espe.gateway.facturacion.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.facturacion.model.GtwFacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.GtwFacturacionComercioRepository;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GtwFacturacionComercioService {

    private final GtwFacturacionComercioRepository gtwFacturacionComercioRepository;

    public GtwFacturacionComercioService(GtwFacturacionComercioRepository gtwFacturacionComercioRepository) {
        this.gtwFacturacionComercioRepository = gtwFacturacionComercioRepository;
    }

    public List<GtwFacturacionComercio> findAll() {
        return gtwFacturacionComercioRepository.findAll();
    }

    public Optional<GtwFacturacionComercio> findById(Integer codigo) {
        return gtwFacturacionComercioRepository.findById(codigo);
    }

    public GtwFacturacionComercio save(GtwFacturacionComercio gtwFacturacionComercio) {
        return gtwFacturacionComercioRepository.save(gtwFacturacionComercio);
    }

    public GtwFacturacionComercio update(Integer codigo, GtwFacturacionComercio gtwFacturacionComercio) {
        return gtwFacturacionComercioRepository.findById(codigo)
                .map(facturacionComercioExistente -> {
                    // Actualización de los campos solicitados
                    facturacionComercioExistente.setComision(gtwFacturacionComercio.getComision());
                    facturacionComercioExistente.setValor(gtwFacturacionComercio.getValor());
                    facturacionComercioExistente.setEstado(gtwFacturacionComercio.getEstado()); // ACT, FAC, PAG
                    facturacionComercioExistente.setTransaccionesProcesadas(gtwFacturacionComercio.getTransaccionesProcesadas());
                    facturacionComercioExistente.setTransaccionesAutorizadas(gtwFacturacionComercio.getTransaccionesAutorizadas());
                    facturacionComercioExistente.setTransaccionesRechazadas(gtwFacturacionComercio.getTransaccionesRechazadas());
                    facturacionComercioExistente.setTransaccionesReversadas(gtwFacturacionComercio.getTransaccionesReversadas());
                    facturacionComercioExistente.setFechaFacturacion(gtwFacturacionComercio.getFechaFacturacion());
                    
                    return gtwFacturacionComercioRepository.save(facturacionComercioExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Facturación de Comercio con código " + codigo + " no encontrada."));
    }

    /* public void deleteById(Integer codigo) {
        GtwFacturacionComercio facturacionComercio = gtwFacturacionComercioRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Facturación de Comercio con código " + codigo + " no encontrada."));

        if ("INA".equals(facturacionComercio.getEstado())) {
            gtwFacturacionComercioRepository.deleteById(codigo);
        } else {
            throw new RuntimeException("No se puede eliminar una Facturación de Comercio con estado diferente a 'INA'.");
        }
    } */
}

