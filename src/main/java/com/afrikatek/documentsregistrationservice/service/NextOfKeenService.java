package com.afrikatek.documentsregistrationservice.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.domain.NextOfKeen;
import com.afrikatek.documentsregistrationservice.repository.NextOfKeenRepository;
import com.afrikatek.documentsregistrationservice.repository.search.NextOfKeenSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsregistrationservice.service.dto.NextOfKeenDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.ApplicantMapper;
import com.afrikatek.documentsregistrationservice.service.mapper.NextOfKeenMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NextOfKeen}.
 */
@Service
@Transactional
public class NextOfKeenService {

    private final Logger log = LoggerFactory.getLogger(NextOfKeenService.class);

    private final NextOfKeenRepository nextOfKeenRepository;

    private final NextOfKeenMapper nextOfKeenMapper;

    private final ApplicantMapper applicantMapper;

    private final NextOfKeenSearchRepository nextOfKeenSearchRepository;

    public NextOfKeenService(
        NextOfKeenRepository nextOfKeenRepository,
        NextOfKeenMapper nextOfKeenMapper,
        ApplicantMapper applicantMapper,
        NextOfKeenSearchRepository nextOfKeenSearchRepository
    ) {
        this.nextOfKeenRepository = nextOfKeenRepository;
        this.nextOfKeenMapper = nextOfKeenMapper;
        this.applicantMapper = applicantMapper;
        this.nextOfKeenSearchRepository = nextOfKeenSearchRepository;
    }

    /**
     * Save a nextOfKeen.
     *
     * @param nextOfKeenDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOfKeenDTO save(NextOfKeenDTO nextOfKeenDTO) {
        log.debug("Request to save NextOfKeen : {}", nextOfKeenDTO);
        NextOfKeen nextOfKeen = nextOfKeenMapper.toEntity(nextOfKeenDTO);
        nextOfKeen = nextOfKeenRepository.save(nextOfKeen);
        NextOfKeenDTO result = nextOfKeenMapper.toDto(nextOfKeen);
        nextOfKeenSearchRepository.save(nextOfKeen);
        return result;
    }

    /**
     * Partially update a nextOfKeen.
     *
     * @param nextOfKeenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOfKeenDTO> partialUpdate(NextOfKeenDTO nextOfKeenDTO) {
        log.debug("Request to partially update NextOfKeen : {}", nextOfKeenDTO);

        return nextOfKeenRepository
            .findById(nextOfKeenDTO.getId())
            .map(
                existingNextOfKeen -> {
                    nextOfKeenMapper.partialUpdate(existingNextOfKeen, nextOfKeenDTO);
                    return existingNextOfKeen;
                }
            )
            .map(nextOfKeenRepository::save)
            .map(
                savedNextOfKeen -> {
                    nextOfKeenSearchRepository.save(savedNextOfKeen);

                    return savedNextOfKeen;
                }
            )
            .map(nextOfKeenMapper::toDto);
    }

    /**
     * Get all the nextOfKeens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOfKeenDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NextOfKeens");
        return nextOfKeenRepository.findAll(pageable).map(nextOfKeenMapper::toDto);
    }

    /**
     * Get one nextOfKeen by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOfKeenDTO> findOne(Long id) {
        log.debug("Request to get NextOfKeen : {}", id);
        return nextOfKeenRepository.findById(id).map(nextOfKeenMapper::toDto);
    }

    /**
     *
     * @param applicantDTO
     * @return list of next of keen for an applicant
     */
    @Transactional(readOnly = true)
    public Optional<NextOfKeenDTO> findByApplicant(ApplicantDTO applicantDTO) {
        log.debug("Request to get NextOfKeen for applicant : {}", applicantDTO);
        return nextOfKeenRepository.findByApplicant(applicantMapper.toEntity(applicantDTO)).map(nextOfKeenMapper::toDto);
    }

    /**
     * Delete the nextOfKeen by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NextOfKeen : {}", id);
        nextOfKeenRepository.deleteById(id);
        nextOfKeenSearchRepository.deleteById(id);
    }

    /**
     * Search for the nextOfKeen corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOfKeenDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NextOfKeens for query {}", query);
        return nextOfKeenSearchRepository.search(queryStringQuery(query), pageable).map(nextOfKeenMapper::toDto);
    }
}
