package org.tds.trial.web.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tds.trial.repository.DeviceRepository;
import org.tds.trial.service.DeviceService;
import org.tds.trial.service.dto.DeviceDTO;
import org.tds.trial.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.tds.trial.domain.Device}.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "Device resource")
public class DeviceResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

    private static final String ENTITY_NAME = "device";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceService deviceService;

    private final DeviceRepository deviceRepository;

    public DeviceResource(DeviceService deviceService, DeviceRepository deviceRepository) {
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
    }

    /**
     * {@code POST  /devices} : Create a new device.
     *
     * @param deviceDTO the deviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceDTO, or with status {@code 400 (Bad Request)} if the
     * device has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devices")
    @Operation(summary = "Create a new device")
    public ResponseEntity<DeviceDTO> createDevice(@Valid @RequestBody DeviceDTO deviceDTO) throws URISyntaxException {
        log.debug("REST request to save Device : {}", deviceDTO);
        if (deviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceDTO result = deviceService.save(deviceDTO);
        return ResponseEntity
            .created(new URI("/api/devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /devices/:id} : Updates an existing device.
     *
     * @param id the id of the deviceDTO to save.
     * @param deviceDTO the deviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceDTO, or with status {@code 400 (Bad Request)} if the
     * deviceDTO is not valid, or with status {@code 500 (Internal Server Error)} if the deviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/devices/{id}")
    @Operation(summary = "Update an existing device")
    public ResponseEntity<DeviceDTO> updateDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeviceDTO deviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Device : {}, {}", id, deviceDTO);
        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!deviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        deviceDTO.setId(id);
        DeviceDTO result = deviceService.save(deviceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /devices/:id} : Partial updates given fields of an existing device, field will ignore if it is null
     *
     * @param id the id of the deviceDTO to save.
     * @param deviceDTO the deviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceDTO, or with status {@code 400 (Bad Request)} if the
     * deviceDTO is not valid, or with status {@code 404 (Not Found)} if the deviceDTO is not found, or with status {@code 500 (Internal Server Error)} if the
     * deviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/devices/{id}", consumes = "application/merge-patch+json")
    @Operation(summary = "Partial update given fields of an existing device")
    public ResponseEntity<DeviceDTO> partialUpdateDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeviceDTO deviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Device partially : {}, {}", id, deviceDTO);
        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!deviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        deviceDTO.setId(id);
        Optional<DeviceDTO> result = deviceService.partialUpdate(deviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deviceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /devices} : get all the devices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of devices in body.
     */
    @GetMapping("/devices")
    @Operation(summary = "Get all the devices")
    public List<DeviceDTO> getAllDevices() {
        log.debug("REST request to get all Devices");
        return deviceService.findAll();
    }

    /**
     * {@code GET  /devices/:id} : get the "id" device.
     *
     * @param id the id of the deviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devices/{id}")
    @Operation(summary = "Get a device")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable Long id) {
        log.debug("REST request to get Device : {}", id);
        Optional<DeviceDTO> deviceDTO = deviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceDTO);
    }

    /**
     * {@code DELETE  /devices/:id} : delete the "id" device.
     *
     * @param id the id of the deviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devices/{id}")
    @Operation(summary = "Delete a device")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        log.debug("REST request to delete Device : {}", id);
        deviceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
