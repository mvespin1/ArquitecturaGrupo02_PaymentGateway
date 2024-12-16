package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.POSConfiguracion;
import ec.edu.espe.pos.model.PosConfiguracionPK;
import ec.edu.espe.pos.repository.POSConfiguracionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class POSConfiguracionService {

    private final POSConfiguracionRepository posConfiguracionRepository;

    public POSConfiguracionService(POSConfiguracionRepository posConfiguracionRepository) {
        this.posConfiguracionRepository = posConfiguracionRepository;
    }

    public List<POSConfiguracion> obtenerTodos() {
        return this.posConfiguracionRepository.findAll();
    }

    public Optional<POSConfiguracion> obtenerPorId(PosConfiguracionPK id) {
        return this.posConfiguracionRepository.findById(id);
    }

    public POSConfiguracion crear(POSConfiguracion posConfiguracion) {
        return this.posConfiguracionRepository.save(posConfiguracion);
    }

    public POSConfiguracion actualizar(PosConfiguracionPK id, String codigoComercio, LocalDate fechaActivacion) {
        Optional<POSConfiguracion> posConfiguracionOpt = this.posConfiguracionRepository.findById(id);
        if (posConfiguracionOpt.isPresent()) {
            POSConfiguracion posConfiguracion = posConfiguracionOpt.get();
            posConfiguracion.setCodigoComercio(codigoComercio);
            posConfiguracion.setFechaActivacion(fechaActivacion);
            return this.posConfiguracionRepository.save(posConfiguracion);
        }
        throw new RuntimeException("No se encontró la configuración POS con el ID proporcionado");
    }
} 