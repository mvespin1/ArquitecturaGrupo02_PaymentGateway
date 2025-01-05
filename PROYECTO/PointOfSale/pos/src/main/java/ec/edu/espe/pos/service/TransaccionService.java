package ec.edu.espe.pos.service;

import ec.edu.espe.pos.model.Transaccion;
import ec.edu.espe.pos.repository.ConfiguracionRepository;
import ec.edu.espe.pos.repository.TransaccionRepository;
import ec.edu.espe.pos.client.GatewayTransaccionClient;
import ec.edu.espe.pos.dto.Comercio;
import ec.edu.espe.pos.client.GatewayComercioClient;
import ec.edu.espe.pos.model.ConfiguracionPK;
import ec.edu.espe.pos.dto.GatewayComercioDTO;
import ec.edu.espe.pos.dto.GatewayTransaccionDTO;
import feign.FeignException;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TransaccionService {

    // Constantes para tipos de transacción
    public static final String TIPO_PAGO = "PAG";
    public static final String TIPO_REVERSO = "REV";

    // Constantes para modalidades
    public static final String MODALIDAD_SIMPLE = "SIM";
    public static final String MODALIDAD_RECURRENTE = "REC";

    // Constantes para estados
    public static final String ESTADO_ENVIADO = "ENV";
    public static final String ESTADO_AUTORIZADO = "AUT";
    public static final String ESTADO_RECHAZADO = "REC";

    // Constantes para estados de recibo
    public static final String ESTADO_RECIBO_IMPRESO = "IMP";
    public static final String ESTADO_RECIBO_PENDIENTE = "PEN";

    // Set de códigos de moneda válidos (ISO 4217)
    private static final Set<String> MONEDAS_VALIDAS = Set.of("USD", "EUR", "GBP");

    private final TransaccionRepository transaccionRepository;
    private final ConfiguracionRepository configuracionRepository;
    private final GatewayTransaccionClient gatewayClient;
    private final GatewayComercioClient comercioClient;

    public TransaccionService(TransaccionRepository transaccionRepository,
                            ConfiguracionRepository configuracionRepository,
                            GatewayTransaccionClient gatewayClient,
                            GatewayComercioClient comercioClient) {
        this.transaccionRepository = transaccionRepository;
        this.configuracionRepository = configuracionRepository;
        this.gatewayClient = gatewayClient;
        this.comercioClient = comercioClient;
    }

    @Transactional(value = TxType.NEVER)
    public List<Transaccion> obtenerTodas() {
        return this.transaccionRepository.findAll();
    }

    @Transactional(value = TxType.NEVER)
    public Transaccion obtenerPorId(Integer id) {
        Optional<Transaccion> transaccionOpt = this.transaccionRepository.findById(id);
        if (transaccionOpt.isPresent()) {
            return transaccionOpt.get();
        }
        throw new EntityNotFoundException("No existe la transacción con el ID: " + id);
    }

    private String obtenerCodigoComercio() {
        // Obtener la configuración del POS actual
        ConfiguracionPK pk = new ConfiguracionPK("POS0000001", "MODEL001");
        
        return configuracionRepository.findByPk(pk)
            .orElseThrow(() -> new RuntimeException(
                String.format("No se encontró la configuración para POS: codigo_pos: %s modelo: %s", 
                    pk.getCodigo(), pk.getModelo())))
            .getCodigoComercio();
    }

    public Transaccion crear(Transaccion transaccion) {
        try {
            // Establecer valores predeterminados
            transaccion.setTipo(TIPO_PAGO);
            transaccion.setModalidad(MODALIDAD_SIMPLE);
            transaccion.setMoneda("USD");
            transaccion.setFecha(LocalDateTime.now());
            transaccion.setEstado(ESTADO_ENVIADO);
            transaccion.setDetalle("Pago con tarjeta");
            transaccion.setMarca(convertirMarcaACodigo(transaccion.getMarca()));
            transaccion.setEstadoRecibo(ESTADO_RECIBO_PENDIENTE);

            System.out.println("1. Datos iniciales de la transacción:");
            System.out.println("   Tipo: " + transaccion.getTipo());
            System.out.println("   Modalidad: " + transaccion.getModalidad());
            System.out.println("   Monto: " + transaccion.getMonto());
            System.out.println("   Detalle: " + transaccion.getDetalle());
            System.out.println("   Comercio inicial: " + transaccion.getComercio());
            System.out.println("   Marca: " + transaccion.getMarca());

            String codigoComercio = obtenerCodigoComercio();
            System.out.println("2. Código de comercio obtenido: " + codigoComercio);
            
            Integer idComercio = comercioClient.obtenerIdPorCodigoInterno(codigoComercio);
            System.out.println("3. ID de comercio obtenido del gateway: " + idComercio);

            // Guardar una copia del comercio original
            GatewayComercioDTO comercioGateway = new GatewayComercioDTO(idComercio);
            Comercio comercioPos = new Comercio(idComercio);
            
            // Limpiar campos transientes para guardar localmente
            transaccion.setCodigoComercioPOS(null);
            transaccion.setTarjeta(null);
            transaccion.setComercio(comercioPos);
            transaccion.setFacturacionComercio(null);

            // Generar código único y guardar
            String codigoUnico = "TRX" + System.currentTimeMillis();
            transaccion.setCodigoUnicoTransaccion(codigoUnico);
            
            // Validar y guardar localmente
            validarTransaccion(transaccion);
            Transaccion transaccionGuardada = this.transaccionRepository.save(transaccion);
            System.out.println("5. Transacción guardada localmente: " + transaccionGuardada);

            // Preparar transacción para el gateway
            transaccionGuardada.setComercio(comercioPos);
            
            // Verificar que el comercio exista en el gateway antes de sincronizar
            try {
                if (idComercio == null) {
                    throw new EntityNotFoundException("El comercio con ID " + idComercio + " no existe en el gateway");
                }
            } catch (FeignException.NotFound e) {
                throw new EntityNotFoundException("El comercio con ID " + idComercio + " no existe en el gateway");
            } catch (FeignException e) {
                throw new RuntimeException("Error al verificar el comercio en el gateway: " + e.getMessage());
            }
            
            // Crear copia para el gateway
            GatewayTransaccionDTO transaccionGateway = GatewayTransaccionDTO.fromTransaccion(transaccionGuardada, idComercio);
            
            // Sincronizar con gateway con reintentos
            int maxIntentos = 3;
            int intento = 0;
            boolean sincronizacionExitosa = false;
            Exception ultimoError = null;

            while (intento < maxIntentos && !sincronizacionExitosa) {
                try {
                    System.out.println("6. Enviando al gateway... (intento " + (intento + 1) + " de " + maxIntentos + ")");
                    System.out.println("Datos de comercio enviados al gateway: " + comercioGateway);
                    gatewayClient.sincronizarTransaccion(transaccionGateway);
                    sincronizacionExitosa = true;
                    System.out.println("7. Sincronización completada exitosamente");
                } catch (FeignException.InternalServerError e) {
                    ultimoError = e;
                    intento++;
                    if (intento < maxIntentos) {
                        // Esperar un poco antes de reintentar
                        Thread.sleep(1000 * intento); // Espera progresiva
                    }
                }
            }

            if (!sincronizacionExitosa) {
                // Si después de todos los intentos no se logró sincronizar
                transaccionGuardada.setEstado(ESTADO_RECHAZADO);
                transaccionRepository.save(transaccionGuardada);
                throw new RuntimeException("No se pudo sincronizar la transacción después de " + maxIntentos + 
                    " intentos. Último error: " + (ultimoError != null ? ultimoError.getMessage() : "desconocido"));
            }

            return transaccionGuardada;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("La operación fue interrumpida", e);
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("No se pudo crear la transacción. Motivo: " + ex.getMessage());
        }
    }

    public Transaccion actualizarEstado(Integer id, String nuevoEstado) {
        try {
            Transaccion transaccion = obtenerPorId(id);
            validarCambioEstado(transaccion.getEstado(), nuevoEstado);
            transaccion.setEstado(nuevoEstado);

            if (ESTADO_AUTORIZADO.equals(nuevoEstado)) {
                transaccion.setEstadoRecibo(ESTADO_RECIBO_PENDIENTE);
            }
            
            Transaccion transaccionActualizada = this.transaccionRepository.save(transaccion);
            
            // Sincronizar con el gateway
            gatewayClient.actualizarEstadoTransaccion(id, nuevoEstado);
            
            return transaccionActualizada;
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo actualizar el estado. Motivo: " + ex.getMessage());
        }
    }

    public Transaccion actualizarEstadoRecibo(Integer id, String nuevoEstadoRecibo) {
        try {
            Transaccion transaccion = obtenerPorId(id);
            validarActualizacionEstadoRecibo(transaccion, nuevoEstadoRecibo);
            transaccion.setEstadoRecibo(nuevoEstadoRecibo);
            return this.transaccionRepository.save(transaccion);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo actualizar el estado del recibo. Motivo: " + ex.getMessage());
        }
    }

    private void validarTransaccion(Transaccion transaccion) {
        validarTipo(transaccion.getTipo());
        validarModalidad(transaccion.getModalidad());
        validarMonto(transaccion.getMonto());
        validarMoneda(transaccion.getMoneda());
        validarFecha(transaccion.getFecha());
    }

    private void validarTipo(String tipo) {
        if (!List.of(TIPO_PAGO, TIPO_REVERSO).contains(tipo)) {
            throw new IllegalArgumentException("Tipo de transacción no válido. Valores permitidos: PAG, REV");
        }
    }

    private void validarModalidad(String modalidad) {
        if (!List.of(MODALIDAD_SIMPLE, MODALIDAD_RECURRENTE).contains(modalidad)) {
            throw new IllegalArgumentException("Modalidad no válida. Valores permitidos: SIM, REC");
        }
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
    }

    private void validarMoneda(String moneda) {
        if (!MONEDAS_VALIDAS.contains(moneda)) {
            throw new IllegalArgumentException("Código de moneda no válido según ISO 4217");
        }
    }

    private void validarFecha(LocalDateTime fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (fecha.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se permiten fechas futuras");
        }
    }

    private void validarCambioEstado(String estadoActual, String nuevoEstado) {
        if (ESTADO_RECHAZADO.equals(estadoActual)) {
            throw new IllegalStateException("No se puede cambiar el estado de una transacción rechazada");
        }

        if (ESTADO_AUTORIZADO.equals(nuevoEstado) && !ESTADO_ENVIADO.equals(estadoActual)) {
            throw new IllegalStateException("Una transacción solo puede ser autorizada si está en estado enviado");
        }
    }

    private void validarActualizacionEstadoRecibo(Transaccion transaccion, String nuevoEstadoRecibo) {
        if (!List.of(ESTADO_RECIBO_IMPRESO, ESTADO_RECIBO_PENDIENTE).contains(nuevoEstadoRecibo)) {
            throw new IllegalArgumentException("Estado de recibo no válido. Valores permitidos: IMP, PEN");
        }

        if (ESTADO_RECIBO_IMPRESO.equals(nuevoEstadoRecibo) && !ESTADO_AUTORIZADO.equals(transaccion.getEstado())) {
            throw new IllegalStateException("Solo se puede imprimir el recibo de transacciones autorizadas");
        }
    }

    private void validarCodigoUnicoTransaccion(String codigoUnico) {
        if (transaccionRepository.existsByCodigoUnicoTransaccion(codigoUnico)) {
            throw new IllegalArgumentException("Ya existe una transacción con el código único proporcionado");
        }
    }

    private void validarReverso(Transaccion reverso) {
        Optional<Transaccion> transaccionOriginal = transaccionRepository
                .findByCodigoUnicoTransaccion(reverso.getCodigoUnicoTransaccion());

        if (transaccionOriginal.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la transacción original para el reverso");
        }

        if (reverso.getMonto().compareTo(transaccionOriginal.get().getMonto()) > 0) {
            throw new IllegalArgumentException("El monto del reverso no puede ser mayor al de la transacción original");
        }
    }

    @Transactional(value = TxType.NEVER)
    public List<Transaccion> obtenerPorEstado(String estado) {
        if (!List.of(ESTADO_ENVIADO, ESTADO_AUTORIZADO, ESTADO_RECHAZADO).contains(estado)) {
            throw new IllegalArgumentException("Estado no válido");
        }
        return transaccionRepository.findByEstado(estado);
    }

    @Transactional(value = TxType.NEVER)
    public List<Transaccion> obtenerPorTipoYEstado(String tipo, String estado) {
        validarTipo(tipo);
        if (!List.of(ESTADO_ENVIADO, ESTADO_AUTORIZADO, ESTADO_RECHAZADO).contains(estado)) {
            throw new IllegalArgumentException("Estado no válido");
        }
        return transaccionRepository.findByTipoAndEstado(tipo, estado);
    }

    public String convertirMarcaACodigo(String marca) {
        if (marca == null)
            return "OTHR";

        switch (marca.toUpperCase()) {
            case "MASTERCARD":
                return "MSCD";
            case "VISA":
                return "VISA";
            case "AMERICAN EXPRESS":
                return "AMEX";
            case "DINERS CLUB":
                return "DINE";
            default:
                return "OTHR";
        }
    }
}