package org.tds.trial.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tds.trial.domain.EsimSubscription;
import org.tds.trial.repository.EsimSubscriptionRepository;
import org.tds.trial.service.dto.EsimSubscriptionDTO;
import org.tds.trial.service.mapper.EsimSubscriptionMapper;

/**
 * Service Implementation for managing {@link EsimSubscription}.
 */
@Service
@Transactional
public class EsimSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(EsimSubscriptionService.class);

    private final EsimSubscriptionRepository esimSubscriptionRepository;

    private final EsimSubscriptionMapper esimSubscriptionMapper;

    public EsimSubscriptionService(EsimSubscriptionRepository esimSubscriptionRepository, EsimSubscriptionMapper esimSubscriptionMapper) {
        this.esimSubscriptionRepository = esimSubscriptionRepository;
        this.esimSubscriptionMapper = esimSubscriptionMapper;
    }

    /**
     * Save a esimSubscription.
     *
     * @param esimSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public EsimSubscriptionDTO save(EsimSubscriptionDTO esimSubscriptionDTO) {
        log.debug("Request to save EsimSubscription : {}", esimSubscriptionDTO);
        EsimSubscription esimSubscription = esimSubscriptionMapper.toEntity(esimSubscriptionDTO);
        esimSubscription = esimSubscriptionRepository.save(esimSubscription);
        return esimSubscriptionMapper.toDto(esimSubscription);
    }

    /**
     * Partially update a esimSubscription.
     *
     * @param esimSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EsimSubscriptionDTO> partialUpdate(EsimSubscriptionDTO esimSubscriptionDTO) {
        log.debug("Request to partially update EsimSubscription : {}", esimSubscriptionDTO);

        return esimSubscriptionRepository
            .findById(esimSubscriptionDTO.getId())
            .map(
                existingEsimSubscription -> {
                    esimSubscriptionMapper.partialUpdate(existingEsimSubscription, esimSubscriptionDTO);
                    return existingEsimSubscription;
                }
            )
            .map(esimSubscriptionRepository::save)
            .map(esimSubscriptionMapper::toDto);
    }

    /**
     * Get all the esimSubscriptions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EsimSubscriptionDTO> findAll() {
        log.debug("Request to get all EsimSubscriptions");
        return esimSubscriptionRepository
            .findAll()
            .stream()
            .map(esimSubscriptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one esimSubscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EsimSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get EsimSubscription : {}", id);
        return esimSubscriptionRepository.findById(id).map(esimSubscriptionMapper::toDto);
    }

    /**
     * Delete the esimSubscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EsimSubscription : {}", id);
        esimSubscriptionRepository.deleteById(id);
    }
}
