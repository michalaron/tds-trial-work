package org.tds.trial.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import org.tds.trial.domain.*;
import org.tds.trial.service.dto.DeviceDTO;
import org.tds.trial.service.dto.EsimDTO;
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

    @Named(value = "deviceToDeviceDTOWithoutUser")
    @Mapping(target = "user", ignore = true)
    DeviceDTO deviceToDeviceDTO(Device device);

    @IterableMapping(qualifiedByName = "deviceToDeviceDTOWithoutUser")
    Set<DeviceDTO> deviceSetToDeviceDTOSet(Set<Device> devices);

    @Named(value = "esimToEsimDTOWithoutDevice")
    @Mapping(target = "device", ignore = true)
    EsimDTO esimToEsimDTO(Esim device);

    @IterableMapping(qualifiedByName = "esimToEsimDTOWithoutDevice")
    Set<EsimDTO> esimSetToEsimDTOSet(Set<Esim> devices);
}
