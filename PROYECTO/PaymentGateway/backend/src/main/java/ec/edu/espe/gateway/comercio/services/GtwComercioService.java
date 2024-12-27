package ec.edu.espe.gateway.comercio.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.espe.gateway.comercio.repository.GtwComercioRepository;
import ec.edu.espe.gateway.comercio.model.GtwComercio;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;

@Service
public class GtwComercioService {

    private final GtwComercioRepository gtwComercioRepository;

    public GtwComercioService(GtwComercioRepository gtwComercioRepository) {
        this.gtwComercioRepository = gtwComercioRepository;
    }

    public List<GtwComercio> findAll() {
        return gtwComercioRepository.findAll();
    }

    public Optional<GtwComercio> findById(int id) {
        return gtwComercioRepository.findById(id);
    }

    @Transactional
    public GtwComercio save(GtwComercio gtwComercio) {
        validarPagosAceptados(gtwComercio.getPagosAceptados());
        validarEstado(gtwComercio.getEstado());
        
        // Establecer fecha de creación
        gtwComercio.setFechaCreacion(LocalDate.now());
        
        // Manejar fechas según el estado
        switch (gtwComercio.getEstado()) {
            case "ACT":
                gtwComercio.setFechaActivacion(LocalDate.now());
                gtwComercio.setFechaSuspension(null);
                break;
            case "SUS":
                gtwComercio.setFechaSuspension(LocalDate.now());
                break;
            case "PEN":
            case "INA":
                gtwComercio.setFechaActivacion(null);
                gtwComercio.setFechaSuspension(null);
                break;
        }
        
        return gtwComercioRepository.save(gtwComercio);
    }

    @Transactional
    public GtwComercio update(int codigo, GtwComercio gtwComercio) {
        return gtwComercioRepository.findById(codigo)
                .map(comercioExistente -> {
                    validarPagosAceptados(gtwComercio.getPagosAceptados());
                    validarEstado(gtwComercio.getEstado());
                    
                    comercioExistente.setComision(gtwComercio.getComision());
                    comercioExistente.setPagosAceptados(gtwComercio.getPagosAceptados());
                    
                    // Actualizar fechas según el estado
                    if (!comercioExistente.getEstado().equals(gtwComercio.getEstado())) {
                        switch (gtwComercio.getEstado()) {
                            case "ACT":
                                comercioExistente.setFechaActivacion(LocalDate.now());
                                comercioExistente.setFechaSuspension(null);
                                break;
                            case "SUS":
                                comercioExistente.setFechaSuspension(LocalDate.now());
                                break;
                            case "INA":
                                comercioExistente.setFechaActivacion(null);
                                comercioExistente.setFechaSuspension(LocalDate.now());
                                break;
                        }
                    }
                    
                    comercioExistente.setEstado(gtwComercio.getEstado());
                    
                    return gtwComercioRepository.save(comercioExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Comercio con código " + codigo + " no encontrado."));
    }

    @Transactional
    public void deleteById(int id) {
        GtwComercio comercio = gtwComercioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comercio con ID " + id + " no encontrado."));

        if (!"ACT".equals(comercio.getEstado())) {
            comercio.setEstado("INA");
            comercio.setFechaSuspension(LocalDate.now());
            gtwComercioRepository.save(comercio);
        } else {
            throw new IllegalStateException("No se puede eliminar un comercio en estado activo.");
        }
    }

    private void validarPagosAceptados(String pagosAceptados) {
        if (!"SIM".equals(pagosAceptados) && !"REC".equals(pagosAceptados) && !"DOS".equals(pagosAceptados)) {
            throw new IllegalArgumentException("PAGOS_ACEPTADOS debe ser SIM, REC o DOS.");
        }
    }

    private void validarEstado(String estado) {
        if (!"PEN".equals(estado) && !"ACT".equals(estado) && !"INA".equals(estado) && !"SUS".equals(estado)) {
            throw new IllegalArgumentException("ESTADO debe ser PEN, ACT, INA o SUS.");
        }
    }
    
    public List<GtwComercio> findByEstado(String estado) {
        validarEstado(estado);
        return gtwComercioRepository.findByEstado(estado);
    }
    
    public List<GtwComercio> findByPagosAceptados(String pagosAceptados) {
        validarPagosAceptados(pagosAceptados);
        return gtwComercioRepository.findByPagosAceptados(pagosAceptados);
    }
}