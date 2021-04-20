package org.tds.trial.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import org.tds.trial.domain.*;
import org.tds.trial.service.dto.DeviceDTO;
import org.tds.trial.service.dto.EsimDTO;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = { TdsUserMapper.class })
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    DeviceDTO toDto(Device s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DeviceDTO toDtoId(Device device);
}
