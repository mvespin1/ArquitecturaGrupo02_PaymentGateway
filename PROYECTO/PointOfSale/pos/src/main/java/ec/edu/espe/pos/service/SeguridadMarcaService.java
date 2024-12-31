package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.SeguridadMarca;
import ec.edu.espe.pos.repository.SeguridadMarcaRepository;
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
public class SeguridadMarcaService {

    private final SeguridadMarcaRepository seguridadMarcaRepository;
    private static final int LONGITUD_MARCA = 4;
    private static final int LONGITUD_CLAVE = 128;
    private static final Pattern PATRON_MARCA = Pattern.compile("^[A-Za-z0-9]{4}$");
    private static final Pattern PATRON_CLAVE = Pattern
            .compile("^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{128}$");

    public SeguridadMarcaService(SeguridadMarcaRepository seguridadMarcaRepository) {
        this.seguridadMarcaRepository = seguridadMarcaRepository;
    }

    @Transactional(value = TxType.NEVER)
    public List<SeguridadMarca> obtenerTodas() {
        return this.seguridadMarcaRepository.findAll();
    }

    @Transactional(value = TxType.NEVER)
    public SeguridadMarca obtenerPorMarca(String marca) {
        Optional<SeguridadMarca> marcaOpt = this.seguridadMarcaRepository.findById(marca);
        if (marcaOpt.isPresent()) {
            return marcaOpt.get();
        }
        throw new EntityNotFoundException("No existe la marca de seguridad con el ID: " + marca);
    }

    public SeguridadMarca procesarActualizacionAutomatica(SeguridadMarca seguridadMarca) {
        try {
            validarSeguridadMarca(seguridadMarca);
            seguridadMarca.setFechaActualizacion(LocalDate.now());

            Optional<SeguridadMarca> marcaExistente = seguridadMarcaRepository.findById(seguridadMarca.getMarca());
            if (marcaExistente.isPresent()) {
                SeguridadMarca existente = marcaExistente.get();
                existente.setClave(seguridadMarca.getClave());
                existente.setFechaActualizacion(LocalDate.now());
                return this.seguridadMarcaRepository.save(existente);
            } else {
                return this.seguridadMarcaRepository.save(seguridadMarca);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error al procesar la actualización automática. Motivo: " + ex.getMessage());
        }
    }

    private void validarSeguridadMarca(SeguridadMarca seguridadMarca) {
        validarMarca(seguridadMarca.getMarca());
        validarClave(seguridadMarca.getClave());
    }

    private void validarMarca(String marca) {
        if (marca == null || marca.length() != LONGITUD_MARCA) {
            throw new IllegalArgumentException("La marca debe tener exactamente 4 caracteres");
        }
        if (!PATRON_MARCA.matcher(marca).matches()) {
            throw new IllegalArgumentException("La marca solo puede contener caracteres alfanuméricos");
        }
    }

    private void validarClave(String clave) {
        if (clave == null || clave.length() != LONGITUD_CLAVE) {
            throw new IllegalArgumentException("La clave debe tener exactamente 128 caracteres");
        }
        if (!PATRON_CLAVE.matcher(clave).matches()) {
            throw new IllegalArgumentException("La clave contiene caracteres no permitidos");
        }
    }
}