package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.PosSeguridadMarca;
import ec.edu.espe.pos.repository.PosSeguridadMarcaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PosSeguridadMarcaService {

    private final PosSeguridadMarcaRepository posSeguridadMarcaRepository;

    public PosSeguridadMarcaService(PosSeguridadMarcaRepository posSeguridadMarcaRepository) {
        this.posSeguridadMarcaRepository = posSeguridadMarcaRepository;
    }

    public List<PosSeguridadMarca> obtenerTodas() {
        return this.posSeguridadMarcaRepository.findAll();
    }

    public Optional<PosSeguridadMarca> obtenerPorMarca(String marca) {
        return this.posSeguridadMarcaRepository.findById(marca);
    }

    public PosSeguridadMarca crear(PosSeguridadMarca posSeguridadMarca) {
        return this.posSeguridadMarcaRepository.save(posSeguridadMarca);
    }

    public PosSeguridadMarca actualizarClave(String marca, String clave) {
        Optional<PosSeguridadMarca> marcaOpt = this.posSeguridadMarcaRepository.findById(marca);
        if (marcaOpt.isPresent()) {
            PosSeguridadMarca seguridadMarca = marcaOpt.get();
            seguridadMarca.setClave(clave);
            seguridadMarca.setFechaActualizacion(LocalDate.now());
            return this.posSeguridadMarcaRepository.save(seguridadMarca);
        }
        throw new RuntimeException("No se encontró la marca con el ID proporcionado");
    }
} 