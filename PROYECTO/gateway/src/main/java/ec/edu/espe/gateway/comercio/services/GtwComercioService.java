package ec.edu.espe.gateway.comercio.services;

import org.springframework.stereotype.Service;

import ec.edu.espe.gateway.comercio.repository.GtwComercioRepository;
import ec.edu.espe.gateway.comercio.model.GtwComercio;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GtwComercioService {

    private final GtwComercioRepository gtwComercioRepository;

    public GtwComercioService(GtwComercioRepository gtwComercioRepository) {
        this.gtwComercioRepository = gtwComercioRepository;
    }

    public List<GtwComercio> findAll() {
        return gtwComercioRepository.findAll();
    }

    public Optional<GtwComercio> obterComercioPorId(int id) {
        return gtwComercioRepository.findById(id);
    }

    public GtwComercio save(GtwComercio gtwComercio) {
        return gtwComercioRepository.save(gtwComercio);
    }

    public GtwComercio update(int codigo, GtwComercio gtwComercio) {
        return gtwComercioRepository.findById(codigo)
                .map(comercioExistente -> {
                    comercioExistente.setComision(gtwComercio.getComision());
                    comercioExistente.setPagosAceptados(gtwComercio.getPagosAceptados());
                    comercioExistente.setEstado(gtwComercio.getEstado());
                    comercioExistente.setFechaSuspension(gtwComercio.getFechaSuspension());
                    return gtwComercioRepository.save(comercioExistente);
                })
                .orElseThrow(() -> new EntityNotFoundException("Comercio con código " + codigo + " no encontrado."));
    }

    public void deleteById(int id) {
        GtwComercio comercio = gtwComercioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comercio con ID " + id + " no encontrado."));

        comercio.setEstado("INA"); // Cambio de estado a inactivo
        gtwComercioRepository.save(comercio);
    }
}