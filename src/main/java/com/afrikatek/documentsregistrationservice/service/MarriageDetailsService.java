package com.afrikatek.documentsregistrationservice.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.domain.MarriageDetails;
import com.afrikatek.documentsregistrationservice.repository.MarriageDetailsRepository;
import com.afrikatek.documentsregistrationservice.repository.search.MarriageDetailsSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsregistrationservice.service.dto.MarriageDetailsDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.ApplicantMapper;
import com.afrikatek.documentsregistrationservice.service.mapper.MarriageDetailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MarriageDetails}.
 */
@Service
@Transactional
public class MarriageDetailsService {

    private final Logger log = LoggerFactory.getLogger(MarriageDetailsService.class);

    private final MarriageDetailsRepository marriageDetailsRepository;

    private final MarriageDetailsMapper marriageDetailsMapper;

    private final ApplicantMapper applicantMapper;

    private final MarriageDetailsSearchRepository marriageDetailsSearchRepository;

    public MarriageDetailsService(
        MarriageDetailsRepository marriageDetailsRepository,
        MarriageDetailsMapper marriageDetailsMapper,
        ApplicantMapper applicantMapper,
        MarriageDetailsSearchRepository marriageDetailsSearchRepository
    ) {
        this.marriageDetailsRepository = marriageDetailsRepository;
        this.marriageDetailsMapper = marriageDetailsMapper;
        this.applicantMapper = applicantMapper;
        this.marriageDetailsSearchRepository = marriageDetailsSearchRepository;
    }

    /**
     * Save a marriageDetails.
     *
     * @param marriageDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public MarriageDetailsDTO save(MarriageDetailsDTO marriageDetailsDTO) {
        log.debug("Request to save MarriageDetails : {}", marriageDetailsDTO);
        MarriageDetails marriageDetails = marriageDetailsMapper.toEntity(marriageDetailsDTO);
        marriageDetails = marriageDetailsRepository.save(marriageDetails);
        MarriageDetailsDTO result = marriageDetailsMapper.toDto(marriageDetails);
        marriageDetailsSearchRepository.save(marriageDetails);
        return result;
    }

    /**
     * Partially update a marriageDetails.
     *
     * @param marriageDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MarriageDetailsDTO> partialUpdate(MarriageDetailsDTO marriageDetailsDTO) {
        log.debug("Request to partially update MarriageDetails : {}", marriageDetailsDTO);

        return marriageDetailsRepository
            .findById(marriageDetailsDTO.getId())
            .map(
                existingMarriageDetails -> {
                    marriageDetailsMapper.partialUpdate(existingMarriageDetails, marriageDetailsDTO);
                    return existingMarriageDetails;
                }
            )
            .map(marriageDetailsRepository::save)
            .map(
                savedMarriageDetails -> {
                    marriageDetailsSearchRepository.save(savedMarriageDetails);

                    return savedMarriageDetails;
                }
            )
            .map(marriageDetailsMapper::toDto);
    }

    /**
     * Get all the marriageDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MarriageDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MarriageDetails");
        return marriageDetailsRepository.findAll(pageable).map(marriageDetailsMapper::toDto);
    }

    /**
     * Get one marriageDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MarriageDetailsDTO> findOne(Long id) {
        log.debug("Request to get MarriageDetails : {}", id);
        return marriageDetailsRepository.findById(id).map(marriageDetailsMapper::toDto);
    }

    /**
     *
     * @param applicantDTO
     * @return marriage details for a given applicant
     */
    @Transactional(readOnly = true)
    public Optional<MarriageDetailsDTO> findByApplicant(ApplicantDTO applicantDTO) {
        log.debug("Request to get MarriageDetails for applicant : {}", applicantDTO);
        return marriageDetailsRepository.findByApplicant(applicantMapper.toEntity(applicantDTO)).map(marriageDetailsMapper::toDto);
    }

    /**
     * Delete the marriageDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MarriageDetails : {}", id);
        marriageDetailsRepository.deleteById(id);
        marriageDetailsSearchRepository.deleteById(id);
    }

    /**
     * Search for the marriageDetails corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MarriageDetailsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MarriageDetails for query {}", query);
        return marriageDetailsSearchRepository.search(queryStringQuery(query), pageable).map(marriageDetailsMapper::toDto);
    }
}
