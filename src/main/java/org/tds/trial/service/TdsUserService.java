package org.tds.trial.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tds.trial.domain.TdsUser;
import org.tds.trial.repository.TdsUserRepository;
import org.tds.trial.service.dto.TdsUserDTO;
import org.tds.trial.service.mapper.TdsUserMapper;

/**
 * Service Implementation for managing {@link TdsUser}.
 */
@Service
@Transactional
public class TdsUserService {

    private final Logger log = LoggerFactory.getLogger(TdsUserService.class);

    private final TdsUserRepository tdsUserRepository;

    private final TdsUserMapper tdsUserMapper;

    public TdsUserService(TdsUserRepository tdsUserRepository, TdsUserMapper tdsUserMapper) {
        this.tdsUserRepository = tdsUserRepository;
        this.tdsUserMapper = tdsUserMapper;
    }

    /**
     * Save a tdsUser.
     *
     * @param tdsUserDTO the entity to save.
     * @return the persisted entity.
     */
    public TdsUserDTO save(TdsUserDTO tdsUserDTO) {
        log.debug("Request to save TdsUser : {}", tdsUserDTO);
        TdsUser tdsUser = tdsUserMapper.toEntity(tdsUserDTO);
        tdsUser = tdsUserRepository.save(tdsUser);
        return tdsUserMapper.toDto(tdsUser);
    }

    /**
     * Partially update a tdsUser.
     *
     * @param tdsUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TdsUserDTO> partialUpdate(TdsUserDTO tdsUserDTO) {
        log.debug("Request to partially update TdsUser : {}", tdsUserDTO);

        return tdsUserRepository
            .findById(tdsUserDTO.getId())
            .map(
                existingTdsUser -> {
                    tdsUserMapper.partialUpdate(existingTdsUser, tdsUserDTO);
                    return existingTdsUser;
                }
            )
            .map(tdsUserRepository::save)
            .map(tdsUserMapper::toDto);
    }

    /**
     * Get all the tdsUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TdsUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TdsUsers");
        return tdsUserRepository.findAll(pageable).map(tdsUserMapper::toDto);
    }

    /**
     * Get one tdsUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TdsUserDTO> findOne(String id) {
        log.debug("Request to get TdsUser : {}", id);
        return tdsUserRepository.findById(id).map(tdsUserMapper::toDto);
    }

    /**
     * Delete the tdsUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete TdsUser : {}", id);
        tdsUserRepository.deleteById(id);
    }
}
