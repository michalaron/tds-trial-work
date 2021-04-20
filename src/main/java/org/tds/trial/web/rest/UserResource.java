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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.tds.trial.repository.TdsUserRepository;
import org.tds.trial.service.TdsUserService;
import org.tds.trial.service.dto.TdsUserDTO;
import org.tds.trial.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.tds.trial.domain.TdsUser}.
 */
@Api(tags = "User resource")
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "User";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TdsUserService tdsUserService;

    private final TdsUserRepository tdsUserRepository;

    public UserResource(TdsUserService tdsUserService, TdsUserRepository tdsUserRepository) {
        this.tdsUserService = tdsUserService;
        this.tdsUserRepository = tdsUserRepository;
    }

    /**
     * {@code POST  /tds-users} : Create a new tdsUser.
     *
     * @param tdsUserDTO the tdsUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tdsUserDTO, or with status {@code 400 (Bad Request)} if the tdsUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/users")
    @Operation(summary = "Create a user")
    public ResponseEntity<TdsUserDTO> createTdsUser(@Valid @RequestBody TdsUserDTO tdsUserDTO) throws URISyntaxException {
        log.debug("REST request to save TdsUser : {}", tdsUserDTO);
        if (tdsUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new tdsUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TdsUserDTO result = tdsUserService.save(tdsUserDTO);
        return ResponseEntity
            .created(new URI("/api/tds-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /users/:id} : Updates an existing tdsUser.
     *
     * @param id the id of the tdsUserDTO to save.
     * @param tdsUserDTO the tdsUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tdsUserDTO,
     * or with status {@code 400 (Bad Request)} if the tdsUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tdsUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/users/{id}")
    @Operation(summary = "Update a user")
    public ResponseEntity<TdsUserDTO> updateTdsUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TdsUserDTO tdsUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TdsUser : {}, {}", id, tdsUserDTO);
        if (tdsUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tdsUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tdsUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TdsUserDTO result = tdsUserService.save(tdsUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tdsUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /users/:id} : Partial updates given fields of an existing tdsUser, field will ignore if it is null
     *
     * @param id the id of the tdsUserDTO to save.
     * @param tdsUserDTO the tdsUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tdsUserDTO,
     * or with status {@code 400 (Bad Request)} if the tdsUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tdsUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tdsUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/users/{id}", consumes = "application/merge-patch+json")
    @Operation(summary = "Partially update a user")
    public ResponseEntity<TdsUserDTO> partialUpdateTdsUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TdsUserDTO tdsUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TdsUser partially : {}, {}", id, tdsUserDTO);
        if (tdsUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tdsUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tdsUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TdsUserDTO> result = tdsUserService.partialUpdate(tdsUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tdsUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /users} : get all the tdsUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tdsUsers in body.
     */
    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<TdsUserDTO>> getAllTdsUsers(Pageable pageable) {
        log.debug("REST request to get a page of TdsUsers");
        Page<TdsUserDTO> page = tdsUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /users/:id} : get the "id" tdsUser.
     *
     * @param id the id of the tdsUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tdsUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "Get a user")
    public ResponseEntity<TdsUserDTO> getTdsUser(@PathVariable Long id) {
        log.debug("REST request to get TdsUser : {}", id);
        Optional<TdsUserDTO> tdsUserDTO = tdsUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tdsUserDTO);
    }

    /**
     * {@code DELETE  /users/:id} : delete the "id" tdsUser.
     *
     * @param id the id of the tdsUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<Void> deleteTdsUser(@PathVariable Long id) {
        log.debug("REST request to delete TdsUser : {}", id);
        tdsUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
