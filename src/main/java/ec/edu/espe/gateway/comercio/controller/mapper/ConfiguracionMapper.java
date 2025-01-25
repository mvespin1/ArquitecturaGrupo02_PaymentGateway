package ec.edu.espe.gateway.comercio.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import ec.edu.espe.gateway.comercio.model.PosComercio;
import ec.edu.espe.gateway.comercio.controller.dto.ConfiguracionDTO;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)

public interface ConfiguracionMapper {
    
    ConfiguracionDTO toDTO(PosComercio model);

    PosComercio toModel(ConfiguracionDTO dto);
}
