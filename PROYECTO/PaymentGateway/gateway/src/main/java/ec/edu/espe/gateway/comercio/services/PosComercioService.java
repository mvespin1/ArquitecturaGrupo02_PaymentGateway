package ec.edu.espe.gateway.comercio.services;

import org.springframework.stereotype.Service;
import ec.edu.espe.gateway.comercio.repository.PosComercioRepository;
import ec.edu.espe.gateway.comercio.repository.ComercioRepository;
import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.model.PosComercioPK;
import ec.edu.espe.gateway.comercio.model.Comercio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ec.edu.espe.gateway.comercio.client.PosConfiguracionClient;
import ec.edu.espe.gateway.comercio.dto.ConfiguracionDTO;

@Service
public class PosComercioService {

    private static final Pattern MAC_ADDRESS_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
    private static final Integer CODIGO_POS_LENGTH = 10;
    public static final String ESTADO_ACTIVO = "ACT";
    public static final String ESTADO_INACTIVO = "INA";

    private final PosComercioRepository posComercioRepository;
    private final ComercioRepository comercioRepository;
    private final PosConfiguracionClient posConfiguracionClient;

    public PosComercioService(PosComercioRepository posComercioRepository,
            ComercioRepository comercioRepository, PosConfiguracionClient posConfiguracionClient) {
        this.posComercioRepository = posComercioRepository;
        this.comercioRepository = comercioRepository;
        this.posConfiguracionClient = posConfiguracionClient;
        
    }

    public List<PosComercio> obtenerTodos() {
        return posComercioRepository.findAll();
    }

    public PosComercio obtenerPorId(PosComercioPK id) {
        return posComercioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe POS con el ID proporcionado"));
    }

    @Transactional
    public PosComercio crear(PosComercio posComercio) {
        // 1. Validar y obtener comercio completo
        validarNuevoPOS(posComercio);
        Comercio comercioCompleto = comercioRepository.findById(posComercio.getComercio().getCodigo())
            .orElseThrow(() -> new EntityNotFoundException("Comercio no encontrado"));
        
        // 2. Preparar y guardar POS localmente
        posComercio.setComercio(comercioCompleto);
        posComercio.setEstado(ESTADO_INACTIVO);
        posComercio.setFechaActivacion(null);
        PosComercio posGuardado = posComercioRepository.save(posComercio);

        try {
            // 3. Intentar sincronizar con el POS (solo los campos necesarios)
            ConfiguracionDTO configuracionParaSincronizar = new ConfiguracionDTO();
            configuracionParaSincronizar.setPk(posGuardado.getPk());
            configuracionParaSincronizar.setDireccionMac(posGuardado.getDireccionMac());
            configuracionParaSincronizar.setFechaActivacion(posGuardado.getFechaActivacion());
            configuracionParaSincronizar.setCodigoComercio(comercioCompleto.getCodigoInterno());

            posConfiguracionClient.enviarConfiguracion(configuracionParaSincronizar);
        } catch (Exception e) {
            System.err.println("Error en sincronización con POS: " + e.getMessage());
        }

        return posGuardado;
    }

    private void validarNuevoPOS(PosComercio posComercio) {
        // Validar código POS
        if (posComercio.getPk().getCodigoPos() == null ||
                posComercio.getPk().getCodigoPos().length() != CODIGO_POS_LENGTH ||
                !posComercio.getPk().getCodigoPos().matches("^[A-Za-z0-9]{10}$")) {
            throw new IllegalArgumentException("El código POS debe tener 10 caracteres alfanuméricos");
        }

        // Validar dirección MAC
        if (!MAC_ADDRESS_PATTERN.matcher(posComercio.getDireccionMac()).matches()) {
            throw new IllegalArgumentException("La dirección MAC no cumple con el formato válido");
        }

        // Validar comercio asociado
        Comercio comercio = comercioRepository.findById(posComercio.getComercio().getCodigo())
                .orElseThrow(() -> new EntityNotFoundException("El comercio asociado no existe"));

        if (!"ACT".equals(comercio.getEstado())) {
            throw new IllegalStateException("El comercio asociado debe estar activo");
        }

        // Validar duplicados
        if (posComercioRepository.existsByDireccionMac(posComercio.getDireccionMac())) {
            throw new IllegalArgumentException("Ya existe un POS con la dirección MAC proporcionada");
        }
    }

    public void activarPOS(PosComercioPK id) {
        try {
            PosComercio pos = obtenerPorId(id);
            Comercio comercio = pos.getComercio();
            LocalDateTime fechaActual = LocalDateTime.now();

            // Validar estado del comercio
            if (!"ACT".equals(comercio.getEstado())) {
                throw new IllegalStateException("No se puede activar el POS porque el comercio no está activo");
            }

            // Validar fechas del comercio
            if (comercio.getFechaActivacion() == null || 
                fechaActual.isBefore(comercio.getFechaActivacion())) {
                throw new IllegalStateException("La fecha de activación del POS no puede ser anterior a la del comercio");
            }

            // Validar que la fecha de activación no sea futura
            if (pos.getFechaActivacion() != null && pos.getFechaActivacion().isAfter(fechaActual)) {
                throw new IllegalStateException("La fecha de activación no puede ser posterior a la fecha actual");
            }

            // Validar configuración del POS antes de activar
            validarConfiguracionPOS(pos.getPk().getCodigoPos());

            pos.setEstado(ESTADO_ACTIVO);
            pos.setFechaActivacion(fechaActual);
            posComercioRepository.save(pos);
        } catch (Exception e) {
            throw new RuntimeException("Error al activar POS: " + e.getMessage());
        }
    }

    public void inactivarPOS(PosComercioPK id) {
        try {
            PosComercio pos = obtenerPorId(id);
            pos.setEstado(ESTADO_INACTIVO);
            pos.setFechaActivacion(null);
            posComercioRepository.save(pos);
        } catch (Exception e) {
            throw new RuntimeException("Error al inactivar POS: " + e.getMessage());
        }
    }

    public void actualizarUltimoUso(PosComercioPK id, LocalDateTime fechaUltimoUso) {
        try {
            PosComercio pos = obtenerPorId(id);
            
            // Validar estado del POS
            if (!ESTADO_ACTIVO.equals(pos.getEstado())) {
                throw new IllegalStateException("No se puede actualizar el último uso de un POS inactivo");
            }

            // Validar coherencia de fechas
            if (fechaUltimoUso.isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("La fecha de último uso no puede ser futura");
            }
            if (pos.getFechaActivacion() != null && fechaUltimoUso.isBefore(pos.getFechaActivacion())) {
                throw new IllegalStateException("La fecha de último uso no puede ser anterior a la fecha de activación");
            }

            pos.setUltimoUso(fechaUltimoUso);
            posComercioRepository.save(pos);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar último uso: " + e.getMessage());
        }
    }

    public void actualizarEstadoPorComercio(Integer codigoComercio, String estadoComercio) {
        try {
            List<PosComercio> dispositivosComercio = posComercioRepository.findByComercio_Codigo(codigoComercio);
            for (PosComercio pos : dispositivosComercio) {
                if ("INA".equals(estadoComercio) || "SUS".equals(estadoComercio)) {
                    pos.setEstado(ESTADO_INACTIVO);
                    pos.setFechaActivacion(null);
                    posComercioRepository.save(pos);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar estado por comercio: " + e.getMessage());
        }
    }

    public void validarConfiguracionPOS(String codigoPos) {
        // Aquí iría la validación contra la tabla POS_CONFIGURACION
        // Como no tenemos acceso a esa tabla, solo validamos el formato
        if (codigoPos == null || codigoPos.length() != CODIGO_POS_LENGTH ||
                !codigoPos.matches("^[A-Za-z0-9]{10}$")) {
            throw new IllegalArgumentException("Configuración de POS inválida");
        }
    }

    public void cambiarComercioAsociado(PosComercioPK id, Integer nuevoCodigoComercio) {
        try {
            PosComercio pos = obtenerPorId(id);
            
            // Validar que el POS esté inactivo
            if (!ESTADO_INACTIVO.equals(pos.getEstado())) {
                throw new IllegalStateException("El POS debe estar inactivo para cambiar el comercio asociado");
            }

            // Validar y obtener el nuevo comercio
            Comercio nuevoComercio = comercioRepository.findById(nuevoCodigoComercio)
                    .orElseThrow(() -> new EntityNotFoundException("El nuevo comercio no existe"));

            // Validar que el nuevo comercio esté activo
            if (!"ACT".equals(nuevoComercio.getEstado())) {
                throw new IllegalStateException("El nuevo comercio debe estar activo");
            }

            // Limpiar fechas al cambiar de comercio
            pos.setFechaActivacion(null);
            pos.setUltimoUso(null);
            pos.setComercio(nuevoComercio);
            
            posComercioRepository.save(pos);
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar comercio asociado: " + e.getMessage());
        }
    }

    public void procesarConfiguracion(PosComercio posComercio) {
        try {
            PosComercio posExistente = this.obtenerPorId(posComercio.getPk());
            posExistente.setDireccionMac(posComercio.getDireccionMac());
            posExistente.setFechaActivacion(posComercio.getFechaActivacion());
            posComercioRepository.save(posExistente);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("POS no encontrado para configuración: " + e.getMessage());
        }
    }

    /*private void validarFechasActivacion(PosComercio pos, Comercio comercio) {
        LocalDate fechaActual = LocalDate.now();
        
        // Validar que la fecha de activación no sea futura
        if (pos.getFechaActivacion() != null && pos.getFechaActivacion().isAfter(fechaActual)) {
            throw new IllegalStateException("La fecha de activación no puede ser posterior a la fecha actual");
        }

        // Validar que la fecha de activación sea posterior a la del comercio
        if (comercio.getFechaActivacion() != null && pos.getFechaActivacion() != null && 
            pos.getFechaActivacion().isBefore(comercio.getFechaActivacion())) {
            throw new IllegalStateException("La fecha de activación del POS debe ser posterior a la del comercio");
        }

        // Validar que la fecha de último uso sea coherente
        if (pos.getUltimoUso() != null) {
            if (pos.getUltimoUso().isAfter(fechaActual)) {
                throw new IllegalStateException("La fecha de último uso no puede ser futura");
            }
            if (pos.getFechaActivacion() != null && pos.getUltimoUso().isBefore(pos.getFechaActivacion())) {
                throw new IllegalStateException("La fecha de último uso no puede ser anterior a la fecha de activación");
            }
        }
    }*/
}
