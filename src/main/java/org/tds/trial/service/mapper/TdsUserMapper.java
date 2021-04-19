package org.tds.trial.service.mapper;

import org.mapstruct.*;
import org.tds.trial.domain.*;
import org.tds.trial.service.dto.TdsUserDTO;

/**
 * Mapper for the entity {@link TdsUser} and its DTO {@link TdsUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TdsUserMapper extends EntityMapper<TdsUserDTO, TdsUser> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TdsUserDTO toDtoId(TdsUser tdsUser);
}
