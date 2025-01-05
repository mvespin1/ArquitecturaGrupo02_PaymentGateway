package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.Configuracion;
import ec.edu.espe.pos.model.ConfiguracionPK;
import ec.edu.espe.pos.repository.ConfiguracionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;
    private static final Pattern MAC_ADDRESS_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
    private static final int CODIGO_POS_LENGTH = 10;
    private static final int MODELO_LENGTH = 10;

    public ConfiguracionService(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    @Transactional(readOnly = true)
    public List<Configuracion> obtenerTodos() {
        return this.configuracionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Configuracion obtenerPorId(ConfiguracionPK id) {
        Optional<Configuracion> configuracionOpt = this.configuracionRepository.findById(id);
        if (configuracionOpt.isPresent()) {
            return configuracionOpt.get();
        }
        throw new EntityNotFoundException("No existe configuración POS con el ID proporcionado");
    }

    public Configuracion crear(Configuracion configuracion) {
        try {
            validarConfiguracion(configuracion);
            
            // Asegurar que la PK esté correctamente asignada
            ConfiguracionPK pk = new ConfiguracionPK(
                configuracion.getPk().getCodigo(),
                configuracion.getPk().getModelo()
            );
            configuracion.setPk(pk);
            configuracion.setCodigoComercio(configuracion.getCodigoComercio());
            configuracion.setDireccionMac(configuracion.getDireccionMac());
            configuracion.setFechaActivacion(configuracion.getFechaActivacion());
            
            return configuracionRepository.save(configuracion);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear configuración: " + e.getMessage());
        }
    }

    public Configuracion actualizarFechaActivacion(ConfiguracionPK id, LocalDateTime nuevaFechaActivacion) {
        try {
            Configuracion configuracion = obtenerPorId(id);
            validarFechaActivacion(nuevaFechaActivacion);
            configuracion.setFechaActivacion(nuevaFechaActivacion);

            Configuracion configuracionActualizada = this.configuracionRepository.save(configuracion);

            return configuracionActualizada;
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo actualizar la fecha de activación. Motivo: " + ex.getMessage());
        }
    }

    private void validarConfiguracion(Configuracion configuracion) {
        validarCodigoPOS(configuracion.getPk().getCodigo());
        validarModelo(configuracion.getPk().getModelo());
        validarDireccionMAC(configuracion.getDireccionMac());
        validarFechaActivacion(configuracion.getFechaActivacion());
        validarCodigoComercio(configuracion.getCodigoComercio());
        validarDuplicados(configuracion);
    }

    private void validarCodigoPOS(String codigoPos) {
        if (codigoPos == null || codigoPos.length() != CODIGO_POS_LENGTH) {
            throw new IllegalArgumentException("El código POS debe tener exactamente 10 caracteres");
        }
        if (!codigoPos.matches("^[A-Za-z0-9]{10}$")) {
            throw new IllegalArgumentException("El código POS solo puede contener letras y números");
        }
    }

    private void validarModelo(String modelo) {
        if (modelo == null || modelo.length() > MODELO_LENGTH) {
            throw new IllegalArgumentException("El modelo debe tener un máximo de 10 caracteres");
        }
        if (!modelo.matches("^[A-Za-z0-9]{1,10}$")) {
            throw new IllegalArgumentException("El modelo solo puede contener letras y números");
        }
    }

    private void validarDireccionMAC(String direccionMac) {
        if (direccionMac == null || !MAC_ADDRESS_PATTERN.matcher(direccionMac).matches()) {
            throw new IllegalArgumentException("La dirección MAC no cumple con el formato válido (XX:XX:XX:XX:XX:XX)");
        }
    }

    private void validarFechaActivacion(LocalDateTime fechaActivacion) {
        if (fechaActivacion != null && fechaActivacion.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de activación no puede ser posterior a la fecha actual");
        }
    }

    private void validarCodigoComercio(Integer codigoComercio) {
        if (codigoComercio == null || codigoComercio <= 0) {
            throw new IllegalArgumentException("El código de comercio debe ser un número positivo");
        }
    }

    private void validarDuplicados(Configuracion configuracion) {
        // Validar MAC duplicada
        configuracionRepository.findAll().stream()
                .filter(config -> !config.getPk().equals(configuracion.getPk()))
                .forEach(config -> {
                    if (config.getDireccionMac().equals(configuracion.getDireccionMac())) {
                        throw new IllegalArgumentException(
                                "Ya existe una configuración con la dirección MAC proporcionada");
                    }
                });
    }

    @Transactional(readOnly = true)
    public Configuracion obtenerConfiguracionActual() {
        List<Configuracion> configuraciones = configuracionRepository.findAll();
        if (configuraciones.isEmpty()) {
            throw new EntityNotFoundException("No existe configuración para este POS");
        }
        if (configuraciones.size() > 1) {
            throw new IllegalStateException("Existe más de una configuración para este POS");
        }
        return configuraciones.get(0);
    }
}