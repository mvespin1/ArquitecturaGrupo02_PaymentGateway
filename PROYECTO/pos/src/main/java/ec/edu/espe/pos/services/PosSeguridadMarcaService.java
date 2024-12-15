package ec.edu.espe.pos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.espe.pos.model.PosSeguridadMarca;
import ec.edu.espe.pos.repository.PosSeguridadMarcaRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PosSeguridadMarcaService {

    @Autowired
    private PosSeguridadMarcaRepository posSeguridadMarcaRepository;

    public Optional<PosSeguridadMarca> obtenerSeguridadPorMarca(String marca) {
        return posSeguridadMarcaRepository.findById(marca);
    }

    public PosSeguridadMarca configurarSeguridadMarca(String marca, String clave) {
        Optional<PosSeguridadMarca> seguridadMarca = posSeguridadMarcaRepository.findById(marca);

        if (seguridadMarca.isPresent()) {
            PosSeguridadMarca marcaExistente = seguridadMarca.get();
            marcaExistente.setClave(clave);
            marcaExistente.setFechaActualizacion(LocalDate.now());
            return posSeguridadMarcaRepository.save(marcaExistente);
        }

        PosSeguridadMarca nuevaMarca = new PosSeguridadMarca(marca);
        nuevaMarca.setClave(clave);
        nuevaMarca.setFechaActualizacion(LocalDate.now());
        return posSeguridadMarcaRepository.save(nuevaMarca);
    }

    public void eliminarSeguridadMarca(String marca) {
        posSeguridadMarcaRepository.deleteById(marca);
    }
}
