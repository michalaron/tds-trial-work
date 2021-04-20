package org.tds.trial.web.rest;

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
import org.tds.trial.repository.EsimRepository;
import org.tds.trial.service.EsimService;
import org.tds.trial.service.dto.EsimDTO;
import org.tds.trial.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.tds.trial.domain.Esim}.
 */
@RestController
@RequestMapping("/api")
public class EsimResource {

    private final Logger log = LoggerFactory.getLogger(EsimResource.class);

    private static final String ENTITY_NAME = "esim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EsimService esimService;

    private final EsimRepository esimRepository;

    public EsimResource(EsimService esimService, EsimRepository esimRepository) {
        this.esimService = esimService;
        this.esimRepository = esimRepository;
    }

    /**
     * {@code POST  /esims} : Create a new esim.
     *
     * @param esimDTO the esimDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new esimDTO, or with status {@code 400 (Bad Request)} if the esim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/esims")
    public ResponseEntity<EsimDTO> createEsim(@Valid @RequestBody EsimDTO esimDTO) throws URISyntaxException {
        log.debug("REST request to save Esim : {}", esimDTO);
        if (esimDTO.getId() != null) {
            throw new BadRequestAlertException("A new esim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EsimDTO result = esimService.save(esimDTO);
        return ResponseEntity
            .created(new URI("/api/esims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /esims/:id} : Updates an existing esim.
     *
     * @param id the id of the esimDTO to save.
     * @param esimDTO the esimDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated esimDTO,
     * or with status {@code 400 (Bad Request)} if the esimDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the esimDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/esims/{id}")
    public ResponseEntity<EsimDTO> updateEsim(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EsimDTO esimDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Esim : {}, {}", id, esimDTO);
        if (esimDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, esimDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!esimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EsimDTO result = esimService.save(esimDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, esimDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /esims/:id} : Partial updates given fields of an existing esim, field will ignore if it is null
     *
     * @param id the id of the esimDTO to save.
     * @param esimDTO the esimDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated esimDTO,
     * or with status {@code 400 (Bad Request)} if the esimDTO is not valid,
     * or with status {@code 404 (Not Found)} if the esimDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the esimDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/esims/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EsimDTO> partialUpdateEsim(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EsimDTO esimDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Esim partially : {}, {}", id, esimDTO);
        if (esimDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, esimDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!esimRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EsimDTO> result = esimService.partialUpdate(esimDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, esimDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /esims} : get all the esims.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of esims in body.
     */
    @GetMapping("/esims")
    public List<EsimDTO> getAllEsims() {
        log.debug("REST request to get all Esims");
        return esimService.findAll();
    }

    /**
     * {@code GET  /esims/:id} : get the "id" esim.
     *
     * @param id the id of the esimDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the esimDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/esims/{id}")
    public ResponseEntity<EsimDTO> getEsim(@PathVariable Long id) {
        log.debug("REST request to get Esim : {}", id);
        Optional<EsimDTO> esimDTO = esimService.findOne(id);
        return ResponseUtil.wrapOrNotFound(esimDTO);
    }

    /**
     * {@code DELETE  /esims/:id} : delete the "id" esim.
     *
     * @param id the id of the esimDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/esims/{id}")
    public ResponseEntity<Void> deleteEsim(@PathVariable Long id) {
        log.debug("REST request to delete Esim : {}", id);
        esimService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
