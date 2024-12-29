package ec.edu.espe.gateway.facturacion.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class FacturacionComercioService {

    private final FacturacionComercioRepository facturacionComercioRepository;

    public FacturacionComercioService(FacturacionComercioRepository facturacionComercioRepository) {
        this.facturacionComercioRepository = facturacionComercioRepository;
    }

    public List<FacturacionComercio> findAll() {
        return facturacionComercioRepository.findAll();
    }

    public Optional<FacturacionComercio> findById(Integer codigo) {
        return facturacionComercioRepository.findById(codigo);
    }

    public FacturacionComercio save(FacturacionComercio facturacionComercio) {
        return facturacionComercioRepository.save(facturacionComercio);
    }

    public FacturacionComercio update(Integer codigo, FacturacionComercio facturacionComercio) {
        return facturacionComercioRepository.findById(codigo)
                .map(facturacionComercioExistente -> {
                    // Actualización de los campos solicitados
                    facturacionComercioExistente.setComision(facturacionComercio.getComision());
                    facturacionComercioExistente.setValor(facturacionComercio.getValor());
                    facturacionComercioExistente.setEstado(facturacionComercio.getEstado()); // ACT, FAC, PAG
                    facturacionComercioExistente.setTransaccionesProcesadas(facturacionComercio.getTransaccionesProcesadas());
                    facturacionComercioExistente.setTransaccionesAutorizadas(facturacionComercio.getTransaccionesAutorizadas());
                    facturacionComercioExistente.setTransaccionesRechazadas(facturacionComercio.getTransaccionesRechazadas());
                    facturacionComercioExistente.setTransaccionesReversadas(facturacionComercio.getTransaccionesReversadas());
                    facturacionComercioExistente.setFechaFacturacion(facturacionComercio.getFechaFacturacion());
                    
                    return facturacionComercioRepository.save(facturacionComercioExistente);
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

