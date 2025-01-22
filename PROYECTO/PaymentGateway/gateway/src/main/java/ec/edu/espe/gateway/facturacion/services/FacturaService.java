package ec.edu.espe.gateway.facturacion.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.espe.gateway.comercio.model.Comercio;
import ec.edu.espe.gateway.transaccion.model.Transaccion;
import ec.edu.espe.gateway.transaccion.services.TransaccionService;
import ec.edu.espe.gateway.comision.model.Comision;
import ec.edu.espe.gateway.comision.services.ComisionService;
import ec.edu.espe.gateway.facturacion.model.FacturacionComercio;
import ec.edu.espe.gateway.facturacion.repository.FacturacionComercioRepository;
import ec.edu.espe.gateway.facturacion.exception.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    public static final String ENTITY_NAME = "Factura";
    public static final String ENTITY_COMERCIO = "Comercio";

    private final TransaccionService transaccionService;
    private final ComisionService comisionService;
    private final FacturacionComercioRepository facturacionComercioRepository;

    public FacturaService(TransaccionService transaccionService,
            ComisionService comisionService, FacturacionComercioRepository facturacionComercioRepository) {
        this.transaccionService = transaccionService;
        this.comisionService = comisionService;
        this.facturacionComercioRepository = facturacionComercioRepository;
    }

    @Transactional
    public void procesarFacturacionAutomatica() {
        List<FacturacionComercio> facturasActivas = facturacionComercioRepository.findByEstado("ACT");
        if (facturasActivas.isEmpty()) {
            throw new NotFoundException("ACT", ENTITY_NAME);
        }

        for (FacturacionComercio factura : facturasActivas) {
            if (factura.getFechaFin().isBefore(LocalDate.now())) {
                procesarFactura(factura);
            }
        }
    }

    @Transactional
    public void procesarFactura(FacturacionComercio factura) {
        try {
            // Calcular comisiones y actualizar estado a FACTURADO
            BigDecimal totalComisiones = calcularComisiones(factura.getComercio(), factura);
            factura.setValor(totalComisiones);
            factura.setEstado("FAC"); // FACTURADO
            facturacionComercioRepository.save(factura);
            
            // Crear nueva factura inmediatamente después
            crearNuevaFactura(factura.getComercio(), factura.getFechaFin());
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar factura: " + e.getMessage());
        }
    }

    private void crearNuevaFactura(Comercio comercio, LocalDate fechaInicio) {
        try {
            LocalDate fechaFin = fechaInicio.plusMonths(1);

            FacturacionComercio nuevaFactura = new FacturacionComercio();
            nuevaFactura.setFechaInicio(fechaInicio);
            nuevaFactura.setFechaFin(fechaFin);
            nuevaFactura.setEstado("ACT"); // ACTIVO
            nuevaFactura.setCodigoFacturacion(
                    "FACT-" + comercio.getCodigo() + "-" + fechaFin.format(DateTimeFormatter.ofPattern("yyyyMM")));
            nuevaFactura.setComercio(comercio);
            nuevaFactura.setComision(comercio.getComision());
            nuevaFactura.setTransaccionesAutorizadas(0);
            nuevaFactura.setTransaccionesProcesadas(0);
            nuevaFactura.setTransaccionesRechazadas(0);
            nuevaFactura.setTransaccionesReversadas(0);
            nuevaFactura.setValor(BigDecimal.ZERO);

            facturacionComercioRepository.save(nuevaFactura);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear nueva factura: " + e.getMessage());
        }
    }

    private BigDecimal calcularComisiones(Comercio comercio, FacturacionComercio factura) {
        List<Transaccion> transacciones = transaccionService.obtenerPorComercioYFecha(
                comercio.getCodigo(), factura.getFechaInicio(), factura.getFechaFin());
        if (transacciones.isEmpty()) {
            throw new NotFoundException(
                comercio.getCodigo().toString(), 
                "Transacciones del comercio"
            );
        }

        Optional<Comision> comisionOpt = comisionService.findById(comercio.getComision().getCodigo());
        if (!comisionOpt.isPresent()) {
            throw new NotFoundException(
                comercio.getComision().getCodigo().toString(), 
                "Comisión"
            );
        }

        Comision comision = comisionOpt.get();
        Integer totalTransacciones = transacciones.size();
        Integer transaccionesBase = comision.getTransaccionesBase();

        if (transaccionesBase <= 0) {
            throw new IllegalStateException(
                    "El valor de transacciones base debe ser mayor a cero para aplicar la comisión.");
        }

        Integer bloques = (int) Math.ceil((double) totalTransacciones / transaccionesBase);

        BigDecimal totalComisiones = BigDecimal.ZERO;

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

        return totalComisiones;
    }
}
