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
import org.tds.trial.repository.EsimSubscriptionRepository;
import org.tds.trial.service.EsimSubscriptionService;
import org.tds.trial.service.dto.EsimSubscriptionDTO;
import org.tds.trial.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.tds.trial.domain.EsimSubscription}.
 */
@RestController
@RequestMapping("/api")
@Api(tags = "Esim subscription resource")
public class EsimSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(EsimSubscriptionResource.class);

    private static final String ENTITY_NAME = "esimSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EsimSubscriptionService esimSubscriptionService;

    private final EsimSubscriptionRepository esimSubscriptionRepository;

    public EsimSubscriptionResource(
        EsimSubscriptionService esimSubscriptionService,
        EsimSubscriptionRepository esimSubscriptionRepository
    ) {
        this.esimSubscriptionService = esimSubscriptionService;
        this.esimSubscriptionRepository = esimSubscriptionRepository;
    }

    /**
     * {@code POST  /esim-subscriptions} : Create a new esimSubscription.
     *
     * @param esimSubscriptionDTO the esimSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new esimSubscriptionDTO, or with status {@code 400 (Bad
     * Request)} if
     * the esimSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/esim-subscriptions")
    @Operation(summary = "Create a new EsimSubscription")
    public ResponseEntity<EsimSubscriptionDTO> createEsimSubscription(@Valid @RequestBody EsimSubscriptionDTO esimSubscriptionDTO)
        throws URISyntaxException {
        log.debug("REST request to save EsimSubscription : {}", esimSubscriptionDTO);
        if (esimSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new esimSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EsimSubscriptionDTO result = esimSubscriptionService.save(esimSubscriptionDTO);
        return ResponseEntity
            .created(new URI("/api/esim-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /esim-subscriptions/:id} : Updates an existing esimSubscription.
     *
     * @param id the id of the esimSubscriptionDTO to save.
     * @param esimSubscriptionDTO the esimSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated esimSubscriptionDTO, or with status {@code 400 (Bad
     * Request)} if
     * the esimSubscriptionDTO is not valid, or with status {@code 500 (Internal Server Error)} if the esimSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/esim-subscriptions/{id}")
    @Operation(summary = "Update an existing EsimSubscription")
    public ResponseEntity<EsimSubscriptionDTO> updateEsimSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EsimSubscriptionDTO esimSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EsimSubscription : {}, {}", id, esimSubscriptionDTO);
        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!esimSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        esimSubscriptionDTO.setId(id);
        EsimSubscriptionDTO result = esimSubscriptionService.save(esimSubscriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, esimSubscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /esim-subscriptions/:id} : Partial updates given fields of an existing esimSubscription, field will ignore if it is null
     *
     * @param id the id of the esimSubscriptionDTO to save.
     * @param esimSubscriptionDTO the esimSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated esimSubscriptionDTO, or with status {@code 400 (Bad
     * Request)} if
     * the esimSubscriptionDTO is not valid, or with status {@code 404 (Not Found)} if the esimSubscriptionDTO is not found, or with status {@code 500 (Internal
     * Server Error)} if the esimSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/esim-subscriptions/{id}", consumes = "application/merge-patch+json")
    @Operation(summary = "Partial update given fields of an existing EsimSubscription")
    public ResponseEntity<EsimSubscriptionDTO> partialUpdateEsimSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EsimSubscriptionDTO esimSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EsimSubscription partially : {}, {}", id, esimSubscriptionDTO);
        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!esimSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        esimSubscriptionDTO.setId(id);
        Optional<EsimSubscriptionDTO> result = esimSubscriptionService.partialUpdate(esimSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, esimSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /esim-subscriptions} : get all the esimSubscriptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of esimSubscriptions in body.
     */
    @GetMapping("/esim-subscriptions")
    @Operation(summary = "Get all the EsimSubscriptions")
    public List<EsimSubscriptionDTO> getAllEsimSubscriptions() {
        log.debug("REST request to get all EsimSubscriptions");
        return esimSubscriptionService.findAll();
    }

    /**
     * {@code GET  /esim-subscriptions/:id} : get the "id" esimSubscription.
     *
     * @param id the id of the esimSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the esimSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/esim-subscriptions/{id}")
    @Operation(summary = "Get an EsimSubscription")
    public ResponseEntity<EsimSubscriptionDTO> getEsimSubscription(@PathVariable Long id) {
        log.debug("REST request to get EsimSubscription : {}", id);
        Optional<EsimSubscriptionDTO> esimSubscriptionDTO = esimSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(esimSubscriptionDTO);
    }

    /**
     * {@code DELETE  /esim-subscriptions/:id} : delete the "id" esimSubscription.
     *
     * @param id the id of the esimSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/esim-subscriptions/{id}")
    @Operation(summary = "Delete an EsimSubscription")
    public ResponseEntity<Void> deleteEsimSubscription(@PathVariable Long id) {
        log.debug("REST request to delete EsimSubscription : {}", id);
        esimSubscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
