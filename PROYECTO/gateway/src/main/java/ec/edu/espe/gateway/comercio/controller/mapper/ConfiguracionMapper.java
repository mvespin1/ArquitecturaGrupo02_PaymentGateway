package ec.edu.espe.gateway.comercio.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.controller.dto.Configuracion;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ConfiguracionMapper {
    
    Configuracion toDTO(PosComercio model);

    PosComercio toModel(Configuracion dto);
}
