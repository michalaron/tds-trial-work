package org.tds.trial.service.mapper;

import org.mapstruct.*;
import org.tds.trial.domain.*;
import org.tds.trial.service.dto.EsimSubscriptionDTO;

/**
 * Mapper for the entity {@link EsimSubscription} and its DTO {@link EsimSubscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EsimSubscriptionMapper extends EntityMapper<EsimSubscriptionDTO, EsimSubscription> {}
