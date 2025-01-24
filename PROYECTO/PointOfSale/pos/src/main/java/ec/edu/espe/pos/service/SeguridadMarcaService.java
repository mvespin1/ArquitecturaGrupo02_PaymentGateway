package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.SeguridadMarca;
import ec.edu.espe.pos.repository.SeguridadMarcaRepository;
import ec.edu.espe.pos.exception.NotFoundException;
import ec.edu.espe.pos.exception.SecurityException;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
@RequiredArgsConstructor
public class SeguridadMarcaService {

    private static final Logger log = LoggerFactory.getLogger(SeguridadMarcaService.class);
    private static final String ENTITY_NAME = "Seguridad Marca";
    private static final int LONGITUD_MARCA = 4;
    private static final int LONGITUD_CLAVE = 128;
    private static final Pattern PATRON_MARCA = Pattern.compile("^[A-Za-z0-9]{4}$");
    private static final Pattern PATRON_CLAVE = Pattern.compile("^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{128}$");

    private final SeguridadMarcaRepository seguridadMarcaRepository;

    @Transactional(value = TxType.NEVER)
    public SeguridadMarca obtenerPorMarca(String marca) {
        log.info("Buscando seguridad marca con ID: {}", marca);
        return seguridadMarcaRepository.findById(marca)
                .orElseThrow(() -> new NotFoundException(marca, ENTITY_NAME));
    }

    public SeguridadMarca procesarActualizacionAutomatica(SeguridadMarca seguridadMarca) {
        try {
            log.info("Procesando actualización automática para marca: {}", seguridadMarca.getMarca());
            validarSeguridadMarca(seguridadMarca);
            seguridadMarca.setFechaActualizacion(LocalDateTime.now());

            Optional<SeguridadMarca> marcaExistente = seguridadMarcaRepository.findById(seguridadMarca.getMarca());
            if (marcaExistente.isPresent()) {
                return actualizarMarcaExistente(marcaExistente.get(), seguridadMarca);
            } else {
                log.info("Creando nueva seguridad marca");
                return seguridadMarcaRepository.save(seguridadMarca);
            }
        } catch (SecurityException | NotFoundException e) {
            throw e;
        } catch (Exception ex) {
            log.error("Error al procesar actualización automática: {}", ex.getMessage());
            throw new SecurityException(
                seguridadMarca.getMarca(), 
                "Actualización automática de seguridad marca");
        }
    }

    private SeguridadMarca actualizarMarcaExistente(SeguridadMarca existente, SeguridadMarca nueva) {
        log.info("Actualizando seguridad marca existente");
        existente.setClave(nueva.getClave());
        existente.setFechaActualizacion(LocalDateTime.now());
        return seguridadMarcaRepository.save(existente);
    }

    private void validarSeguridadMarca(SeguridadMarca seguridadMarca) {
        log.debug("Validando seguridad marca");
        validarMarca(seguridadMarca.getMarca());
        validarClave(seguridadMarca.getClave());
    }

    private void validarMarca(String marca) {
        if (marca == null || marca.length() != LONGITUD_MARCA) {
            log.error("Error de validación: marca con longitud incorrecta");
            throw new SecurityException(
                marca != null ? marca : "null", 
                "Longitud de marca");
        }
        if (!PATRON_MARCA.matcher(marca).matches()) {
            log.error("Error de validación: marca con formato incorrecto");
            throw new SecurityException(marca, "Formato de marca");
        }
    }

    private void validarClave(String clave) {
        if (clave == null || clave.length() != LONGITUD_CLAVE) {
            log.error("Error de validación: clave con longitud incorrecta");
            throw new SecurityException(
                clave != null ? String.valueOf(clave.length()) : "null", 
                "Longitud de clave");
        }
        if (!PATRON_CLAVE.matcher(clave).matches()) {
            log.error("Error de validación: clave con formato incorrecto");
            throw new SecurityException(clave, "Formato de clave");
        }
    }
}