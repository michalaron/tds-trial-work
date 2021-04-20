package org.tds.trial.service.mapper;

import org.mapstruct.*;
import org.tds.trial.domain.*;
import org.tds.trial.service.dto.EsimDTO;

/**
 * Mapper for the entity {@link Esim} and its DTO {@link EsimDTO}.
 */
@Mapper(componentModel = "spring", uses = { EsimSubscriptionMapper.class, DeviceMapper.class })
public interface EsimMapper extends EntityMapper<EsimDTO, Esim> {
    @Mapping(target = "device", source = "device", qualifiedByName = "id")
    EsimDTO toDto(Esim s);
}
