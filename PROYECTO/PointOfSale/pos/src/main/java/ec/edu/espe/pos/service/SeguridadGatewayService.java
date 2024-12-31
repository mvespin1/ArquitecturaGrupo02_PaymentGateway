package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.SeguridadGateway;
import ec.edu.espe.pos.repository.SeguridadGatewayRepository;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class SeguridadGatewayService {

    private final SeguridadGatewayRepository seguridadGatewayRepository;

    private static final int LONGITUD_CLAVE = 128;
    private static final Pattern PATRON_CLAVE = Pattern
            .compile("^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{128}$");

    public static final String ESTADO_ACTIVO = "ACT";
    public static final String ESTADO_INACTIVO = "INA";
    public static final String ESTADO_PENDIENTE = "PEN";

    public SeguridadGatewayService(SeguridadGatewayRepository seguridadGatewayRepository) {
        this.seguridadGatewayRepository = seguridadGatewayRepository;
    }

    /*@Transactional(value = TxType.NEVER)
    public List<SeguridadGateway> obtenerTodos() {
        return this.seguridadGatewayRepository.findAll();
    }*/

    @Transactional(value = TxType.NEVER)
    public SeguridadGateway obtenerPorId(Integer id) {
        Optional<SeguridadGateway> gatewayOpt = this.seguridadGatewayRepository.findById(id);
        if (gatewayOpt.isPresent()) {
            return gatewayOpt.get();
        }
        throw new EntityNotFoundException("No existe el gateway de seguridad con el ID: " + id);
    }

    public SeguridadGateway procesarActualizacionAutomatica(SeguridadGateway seguridadGateway) {
        try {
            validarSeguridadGateway(seguridadGateway);
            seguridadGateway.setFechaActualizacion(LocalDate.now());

            if (seguridadGateway.getCodigo() != null) {
                SeguridadGateway existente = obtenerPorId(seguridadGateway.getCodigo());
                existente.setClave(seguridadGateway.getClave());
                existente.setEstado(seguridadGateway.getEstado());
                existente.setFechaActualizacion(LocalDate.now());
                return this.seguridadGatewayRepository.save(existente);
            } else {
                seguridadGateway.setFechaActivacion(LocalDate.now());
                seguridadGateway.setEstado(ESTADO_ACTIVO);
                return this.seguridadGatewayRepository.save(seguridadGateway);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error al procesar la actualización automática. Motivo: " + ex.getMessage());
        }
    }

    private void validarSeguridadGateway(SeguridadGateway seguridadGateway) {
        validarClave(seguridadGateway.getClave());
        validarEstado(seguridadGateway.getEstado());
        validarFechaActivacion(seguridadGateway.getFechaActivacion());
    }

    private void validarClave(String clave) {
        if (clave == null || clave.length() != LONGITUD_CLAVE) {
            throw new IllegalArgumentException("La clave debe tener exactamente 128 caracteres");
        }
        if (!PATRON_CLAVE.matcher(clave).matches()) {
            throw new IllegalArgumentException("La clave contiene caracteres no permitidos");
        }
    }

    private void validarEstado(String estado) {
        if (estado != null && !List.of(ESTADO_ACTIVO, ESTADO_INACTIVO, ESTADO_PENDIENTE).contains(estado)) {
            throw new IllegalArgumentException(
                    "El estado proporcionado no es válido. Estados permitidos: ACT, INA, PEN");
        }
    }

    private void validarFechaActivacion(LocalDate fechaActivacion) {
        if (fechaActivacion != null && fechaActivacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de activación no puede ser una fecha futura");
        }
    }

    @Transactional(value = TxType.NEVER)
    public List<SeguridadGateway> obtenerPorEstado(String estado) {
        validarEstado(estado);
        return this.seguridadGatewayRepository.findByEstado(estado);
    }
}