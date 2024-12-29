package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.Configuracion;
import ec.edu.espe.pos.model.ConfiguracionPK;
import ec.edu.espe.pos.repository.ConfiguracionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;

    public ConfiguracionService(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    public List<Configuracion> obtenerTodos() {
        return this.configuracionRepository.findAll();
    }

    public Optional<Configuracion> obtenerPorId(ConfiguracionPK id) {
        return this.configuracionRepository.findById(id);
    }

    public Configuracion crear(Configuracion configuracion) {
        return this.configuracionRepository.save(configuracion);
    }

    public Configuracion actualizar(ConfiguracionPK id, String codigoComercio, LocalDate fechaActivacion) {
        Optional<Configuracion> posConfiguracionOpt = this.configuracionRepository.findById(id);
        if (posConfiguracionOpt.isPresent()) {
            Configuracion configuracion = posConfiguracionOpt.get();
            configuracion.setCodigoComercio(codigoComercio);
            configuracion.setFechaActivacion(fechaActivacion);
            return this.configuracionRepository.save(configuracion);
        }
        throw new RuntimeException("No se encontró la configuración POS con el ID proporcionado");
    }
} 