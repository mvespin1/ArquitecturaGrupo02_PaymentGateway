package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.SeguridadMarca;
import ec.edu.espe.pos.repository.SeguridadMarcaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SeguridadMarcaService {

    private final SeguridadMarcaRepository seguridadMarcaRepository;

    public SeguridadMarcaService(SeguridadMarcaRepository seguridadMarcaRepository) {
        this.seguridadMarcaRepository = seguridadMarcaRepository;
    }

    public List<SeguridadMarca> obtenerTodas() {
        return this.seguridadMarcaRepository.findAll();
    }

    public Optional<SeguridadMarca> obtenerPorMarca(String marca) {
        return this.seguridadMarcaRepository.findById(marca);
    }

    public SeguridadMarca crear(SeguridadMarca seguridadMarca) {
        return this.seguridadMarcaRepository.save(seguridadMarca);
    }

    public SeguridadMarca actualizarClave(String marca, String clave) {
        Optional<SeguridadMarca> marcaOpt = this.seguridadMarcaRepository.findById(marca);
        if (marcaOpt.isPresent()) {
            SeguridadMarca seguridadMarca = marcaOpt.get();
            seguridadMarca.setClave(clave);
            seguridadMarca.setFechaActualizacion(LocalDate.now());
            return this.seguridadMarcaRepository.save(seguridadMarca);
        }
        throw new RuntimeException("No se encontró la marca con el ID proporcionado");
    }
} 