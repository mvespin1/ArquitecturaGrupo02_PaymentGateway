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
import org.springframework.transaction.annotation.Transactional;
import ec.edu.espe.gateway.comercio.client.PosConfiguracionClient;
import ec.edu.espe.gateway.comercio.controller.dto.ConfiguracionDTO;
import ec.edu.espe.gateway.comercio.controller.mapper.ConfiguracionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ec.edu.espe.gateway.comercio.exception.NotFoundException;

@Service
public class PosComercioService {

    private static final Logger log = LoggerFactory.getLogger(PosComercioService.class);

    // Constantes de entidad
    public static final String ENTITY_NAME = "POS";
    public static final String ENTITY_COMERCIO = "Comercio";
    public static final String ENTITY_CONFIGURACION = "Configuración POS";

    // Constantes de estado
    public static final String ESTADO_ACTIVO = "ACT";
    public static final String ESTADO_INACTIVO = "INA";

    // Constantes de validación
    private static final Pattern MAC_ADDRESS_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");
    private static final Integer CODIGO_POS_LENGTH = 10;

    private final PosComercioRepository posComercioRepository;
    private final ComercioRepository comercioRepository;
    private final PosConfiguracionClient posConfiguracionClient;
    private final ConfiguracionMapper configuracionMapper;

    public PosComercioService(PosComercioRepository posComercioRepository,
            ComercioRepository comercioRepository,
            PosConfiguracionClient posConfiguracionClient,
            ConfiguracionMapper configuracionMapper) {
        this.posComercioRepository = posComercioRepository;
        this.comercioRepository = comercioRepository;
        this.posConfiguracionClient = posConfiguracionClient;
        this.configuracionMapper = configuracionMapper;
    }

    public List<PosComercio> obtenerTodos() {
        return posComercioRepository.findAll();
    }

    public PosComercio obtenerPorId(PosComercioPK id) {
        PosComercio pos = posComercioRepository.findById(id).orElse(null);
        if (pos == null) {
            throw new NotFoundException(id.toString(), ENTITY_NAME);
        }
        return pos;
    }

    @Transactional
    public PosComercio crear(PosComercio posComercio) {
        try {
            // 1. Validar y obtener comercio completo
            validarNuevoPOS(posComercio);
            Comercio comercioCompleto = comercioRepository.findById(posComercio.getComercio().getCodigo())
                    .orElseThrow(() -> new NotFoundException(
                            posComercio.getComercio().getCodigo().toString(),
                            ENTITY_COMERCIO));

            // 2. Preparar y guardar POS
            posComercio.setComercio(comercioCompleto);
            posComercio.setEstado(ESTADO_ACTIVO);
            posComercio.setFechaActivacion(LocalDateTime.now());
            PosComercio posGuardado = posComercioRepository.save(posComercio);

            try {
                // 3. Sincronizar con servicio de configuración
                ConfiguracionDTO configuracionParaSincronizar = configuracionMapper.toDTO(posGuardado);
                configuracionParaSincronizar.setCodigoComercio(comercioCompleto.getCodigo());
                posConfiguracionClient.enviarConfiguracion(configuracionParaSincronizar);
            } catch (Exception e) {
                log.error("Error en sincronización con POS: {}", e.getMessage());
            }

            return posGuardado;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear POS: " + e.getMessage());
        }
    }

    // este metodo se utilizara cuando llegue una trasaccion al gateway
    public void actualizarUltimoUso(String codigoPos, String modeloPos, LocalDateTime fechaUltimoUso) {
        try {
            PosComercioPK pk = new PosComercioPK(codigoPos, modeloPos);
            PosComercio pos = obtenerPorId(pk);

            if (!ESTADO_ACTIVO.equals(pos.getEstado())) {
                throw new IllegalStateException("El POS debe estar activo para actualizar su último uso");
            }

            pos.setUltimoUso(fechaUltimoUso);
            posComercioRepository.save(pos);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar último uso: " + e.getMessage());
        }
    }

    private void validarNuevoPOS(PosComercio posComercio) {
        if (posComercio.getPk().getCodigo() == null ||
                posComercio.getPk().getCodigo().length() != CODIGO_POS_LENGTH ||
                !posComercio.getPk().getCodigo().matches("^[A-Za-z0-9]{10}$")) {
            throw new IllegalArgumentException("El código POS debe tener 10 caracteres alfanuméricos");
        }

        if (!MAC_ADDRESS_PATTERN.matcher(posComercio.getDireccionMac()).matches()) {
            throw new IllegalArgumentException("La dirección MAC no cumple con el formato válido");
        }

        Comercio comercio = comercioRepository.findById(posComercio.getComercio().getCodigo())
                .orElseThrow(() -> new NotFoundException(
                        posComercio.getComercio().getCodigo().toString(),
                        ENTITY_COMERCIO));

        if (!ESTADO_ACTIVO.equals(comercio.getEstado())) {
            throw new IllegalStateException("El comercio asociado debe estar activo");
        }
    }

    public void activarPOS(PosComercioPK id) {
        try {
            PosComercio pos = obtenerPorId(id);
            Comercio comercio = pos.getComercio();
            LocalDateTime fechaActual = LocalDateTime.now();

            if (!"ACT".equals(comercio.getEstado())) {
                throw new IllegalStateException("No se puede activar el POS porque el comercio no está activo");
            }

            if (comercio.getFechaActivacion() == null ||
                    fechaActual.isBefore(comercio.getFechaActivacion())) {
                throw new IllegalStateException(
                        "La fecha de activación del POS no puede ser anterior a la del comercio");
            }

            if (pos.getFechaActivacion() != null && pos.getFechaActivacion().isAfter(fechaActual)) {
                throw new IllegalStateException("La fecha de activación no puede ser posterior a la fecha actual");
            }

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
}
