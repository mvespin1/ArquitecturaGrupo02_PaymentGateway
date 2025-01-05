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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

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

        for (FacturacionComercio factura : facturasActivas) {
            if (factura.getFechaFin().isBefore(LocalDate.now())) {
                procesarFactura(factura);
            }
        }
    }

    @Transactional
    public void procesarFactura(FacturacionComercio factura) {
        // Calcular comisiones y actualizar estado a FACTURADO
        BigDecimal totalComisiones = calcularComisiones(factura.getComercio(), factura);
        factura.setValor(totalComisiones);
        factura.setEstado("FAC"); // FACTURADO
        facturacionComercioRepository.save(factura);
        
        // Crear nueva factura inmediatamente después
        crearNuevaFactura(factura.getComercio(), factura.getFechaFin());
    }

    private void crearNuevaFactura(Comercio comercio, LocalDate fechaInicio) {
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
