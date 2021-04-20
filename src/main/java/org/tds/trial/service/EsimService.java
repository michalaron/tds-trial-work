package org.tds.trial.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tds.trial.domain.Esim;
import org.tds.trial.repository.EsimRepository;
import org.tds.trial.service.dto.EsimDTO;
import org.tds.trial.service.mapper.EsimMapper;

/**
 * Service Implementation for managing {@link Esim}.
 */
@Service
@Transactional
public class EsimService {

    private final Logger log = LoggerFactory.getLogger(EsimService.class);

    private final EsimRepository esimRepository;

    private final EsimMapper esimMapper;

    public EsimService(EsimRepository esimRepository, EsimMapper esimMapper) {
        this.esimRepository = esimRepository;
        this.esimMapper = esimMapper;
    }

    /**
     * Save a esim.
     *
     * @param esimDTO the entity to save.
     * @return the persisted entity.
     */
    public EsimDTO save(EsimDTO esimDTO) {
        log.debug("Request to save Esim : {}", esimDTO);
        Esim esim = esimMapper.toEntity(esimDTO);
        esim = esimRepository.save(esim);
        return esimMapper.toDto(esim);
    }

    /**
     * Partially update a esim.
     *
     * @param esimDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EsimDTO> partialUpdate(EsimDTO esimDTO) {
        log.debug("Request to partially update Esim : {}", esimDTO);

        return esimRepository
            .findById(esimDTO.getId())
            .map(
                existingEsim -> {
                    esimMapper.partialUpdate(existingEsim, esimDTO);
                    return existingEsim;
                }
            )
            .map(esimRepository::save)
            .map(esimMapper::toDto);
    }

    /**
     * Get all the esims.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EsimDTO> findAll() {
        log.debug("Request to get all Esims");
        return esimRepository.findAll().stream().map(esimMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one esim by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EsimDTO> findOne(Long id) {
        log.debug("Request to get Esim : {}", id);
        return esimRepository.findById(id).map(esimMapper::toDto);
    }

    /**
     * Delete the esim by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Esim : {}", id);
        esimRepository.deleteById(id);
    }
}
