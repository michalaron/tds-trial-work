package org.tds.trial.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tds.trial.domain.Device;
import org.tds.trial.repository.DeviceRepository;
import org.tds.trial.service.dto.DeviceDTO;
import org.tds.trial.service.mapper.DeviceMapper;

/**
 * Service Implementation for managing {@link Device}.
 */
@Service
@Transactional
public class DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    /**
     * Save a device.
     *
     * @param deviceDTO the entity to save.
     * @return the persisted entity.
     */
    public DeviceDTO save(DeviceDTO deviceDTO) {
        log.debug("Request to save Device : {}", deviceDTO);
        Device device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    /**
     * Partially update a device.
     *
     * @param deviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeviceDTO> partialUpdate(DeviceDTO deviceDTO) {
        log.debug("Request to partially update Device : {}", deviceDTO);

        return deviceRepository
            .findById(deviceDTO.getId())
            .map(
                existingDevice -> {
                    deviceMapper.partialUpdate(existingDevice, deviceDTO);
                    return existingDevice;
                }
            )
            .map(deviceRepository::save)
            .map(deviceMapper::toDto);
    }

    /**
     * Get all the devices.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DeviceDTO> findAll() {
        log.debug("Request to get all Devices");
        return deviceRepository.findAll().stream().map(deviceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one device by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeviceDTO> findOne(Long id) {
        log.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id).map(deviceMapper::toDto);
    }

    /**
     * Delete the device by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }
}
