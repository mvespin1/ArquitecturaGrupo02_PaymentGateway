package ec.edu.espe.gateway.comercio.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.comercio.model.Comercio;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;

@Service
public class ComercioService {

    private final ComercioRepository comercioRepository;

    public ComercioService(ComercioRepository comercioRepository) {
        this.comercioRepository = comercioRepository;
    }

    public List<Comercio> findAll() {
        return comercioRepository.findAll();
    }

    public Optional<Comercio> findById(int id) {
        return comercioRepository.findById(id);
    }

    @Transactional
    public Comercio save(Comercio comercio) {
        validarPagosAceptados(comercio.getPagosAceptados());
        validarEstado(comercio.getEstado());
        
        // Establecer fecha de creación
        comercio.setFechaCreacion(LocalDate.now());
        
        // Manejar fechas según el estado
        switch (comercio.getEstado()) {
            case "ACT":
                comercio.setFechaActivacion(LocalDate.now());
                comercio.setFechaSuspension(null);
                break;
            case "SUS":
                comercio.setFechaSuspension(LocalDate.now());
                break;
            case "PEN":
            case "INA":
                comercio.setFechaActivacion(null);
                comercio.setFechaSuspension(null);
                break;
        }
        
        return comercioRepository.save(comercio);
    }

    @Transactional
    public Comercio update(int codigo, Comercio comercio) {
        return comercioRepository.findById(codigo)
                .map(comercioExistente -> {
                    validarPagosAceptados(comercio.getPagosAceptados());
                    validarEstado(comercio.getEstado());
                    
                    comercioExistente.setComision(comercio.getComision());
                    comercioExistente.setPagosAceptados(comercio.getPagosAceptados());
                    
                    // Actualizar fechas según el estado
                    if (!comercioExistente.getEstado().equals(comercio.getEstado())) {
                        switch (comercio.getEstado()) {
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
                    
                    comercioExistente.setEstado(comercio.getEstado());
                    
                    return comercioRepository.save(comercioExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Comercio con código " + codigo + " no encontrado."));
    }

    @Transactional
    public void deleteById(int id) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comercio con ID " + id + " no encontrado."));

        if (!"ACT".equals(comercio.getEstado())) {
            comercio.setEstado("INA");
            comercio.setFechaSuspension(LocalDate.now());
            comercioRepository.save(comercio);
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
    
    public List<Comercio> findByEstado(String estado) {
        validarEstado(estado);
        return comercioRepository.findByEstado(estado);
    }
    
    public List<Comercio> findByPagosAceptados(String pagosAceptados) {
        validarPagosAceptados(pagosAceptados);
        return comercioRepository.findByPagosAceptados(pagosAceptados);
    }
}