package ec.edu.espe.gateway.transaccion.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import ec.edu.espe.gateway.transaccion.controller.dto.ValidacionTransaccionDTO;
import ec.edu.espe.gateway.transaccion.model.Transaccion;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TransaccionMapper {
    
    ValidacionTransaccionDTO toDTO(Transaccion model);
    
    Transaccion toModel(ValidacionTransaccionDTO dto);
} 