package ec.edu.espe.gateway.facturacion.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.comercio.services.ComercioService;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.services.TransaccionService;
import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.services.ComisionService;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    private final ComercioService comercioService;
    private final TransaccionService transaccionService;
    private final ComisionService comisionService;
    private final FacturacionComercioRepository facturacionComercioRepository;

    public FacturaService(ComercioService comercioService, TransaccionService transaccionService,
            ComisionService comisionService, FacturacionComercioRepository facturacionComercioRepository) {
        this.comercioService = comercioService;
        this.transaccionService = transaccionService;
        this.comisionService = comisionService;
        this.facturacionComercioRepository = facturacionComercioRepository;
    }

    @Transactional
    public void procesarFacturacionAutomatica() {
        List<Comercio> comerciosActivos = comercioService.listarComerciosPorEstado("ACT");

        for (Comercio comercio : comerciosActivos) {
            // Procesar facturas activas que hayan llegado a su fecha de fin
            FacturacionComercio facturaActiva = facturacionComercioRepository
                    .findFacturaActivaPorComercio(comercio.getCodigo());

            if (facturaActiva != null) {
                if (facturaActiva.getFechaFin().isBefore(LocalDate.now())) {
                    // Calcular comisiones y actualizar estado a FACTURADO
                    BigDecimal totalComisiones = calcularComisiones(comercio, facturaActiva);
                    facturaActiva.setValor(totalComisiones);
                    facturaActiva.setEstado(FacturacionComercioService.ESTADO_FACTURADO);
                    facturacionComercioRepository.save(facturaActiva);
                }
                // No generar nuevas facturas si hay una activa pendiente
                continue;
            }

            // Si no hay facturas activas, crear una nueva
            FacturacionComercio ultimaFactura = facturacionComercioRepository
                    .findUltimaFacturaPorComercio(comercio.getCodigo());

            LocalDate fechaInicio;
            LocalDate fechaFin;

            if (ultimaFactura != null) {
                // La nueva factura comienza en la fecha de fin de la última factura
                fechaInicio = ultimaFactura.getFechaFin();
            } else {
                // Primera factura, usar la fecha de activación del comercio
                fechaInicio = comercio.getFechaActivacion();
            }
            fechaFin = fechaInicio.plusMonths(1);

            // Crear una nueva factura con estado ACTIVO
            FacturacionComercio nuevaFactura = new FacturacionComercio();
            nuevaFactura.setFechaInicio(fechaInicio);
            nuevaFactura.setFechaFin(fechaFin);
            nuevaFactura.setEstado(FacturacionComercioService.ESTADO_ACTIVO); // Estado inicial
            nuevaFactura.setCodigoFacturacion(
                    "FACT-" + comercio.getCodigo() + "-" + fechaFin.format(DateTimeFormatter.ofPattern("yyyyMM")));
            nuevaFactura.setComercio(comercio);
            nuevaFactura.setComision(comercio.getComision());

            // Guardar la nueva factura en la base de datos
            facturacionComercioRepository.save(nuevaFactura);
        }
    }

    private BigDecimal calcularComisiones(Comercio comercio, FacturacionComercio factura) {
        List<Transaccion> transacciones = transaccionService.obtenerPorComercioYFecha(
                comercio.getCodigo(), factura.getFechaInicio(), factura.getFechaFin());

        BigDecimal totalComisiones = BigDecimal.ZERO;
        Optional<Comision> comisionOpt = comisionService.findById(comercio.getComision().getCodigo());

        if (comisionOpt.isPresent()) {
            Comision comision = comisionOpt.get();
            Integer totalTransacciones = transacciones.size();
            Integer transaccionesBase = comision.getTransaccionesBase();

            if (transaccionesBase <= 0) {
                throw new IllegalStateException(
                        "El valor de transacciones base debe ser mayor a cero para aplicar la comisión.");
            }

            Integer bloques = (int) Math.ceil((double) totalTransacciones / transaccionesBase);

            for (int i = 0; i < bloques; i++) {
                if ("POR".equals(comision.getTipo())) {
                    // Comisión porcentual
                    BigDecimal montoBloque = transacciones.stream()
                            .skip((long) i * transaccionesBase)
                            .limit(transaccionesBase)
                            .map(Transaccion::getMonto)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    totalComisiones = totalComisiones.add(montoBloque.multiply(comision.getMontoBase()));
                } else if ("FIJ".equals(comision.getTipo())) {
                    // Comisión fija
                    totalComisiones = totalComisiones.add(comision.getMontoBase());
                }
            }
        } else {
            throw new IllegalStateException("No se encontró la comisión asociada al comercio " + comercio.getCodigo());
        }

        return totalComisiones;
    }

}
