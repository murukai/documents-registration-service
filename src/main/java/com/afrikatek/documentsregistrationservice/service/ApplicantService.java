package com.afrikatek.documentsregistrationservice.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.domain.Applicant;
import com.afrikatek.documentsregistrationservice.repository.ApplicantRepository;
import com.afrikatek.documentsregistrationservice.repository.search.ApplicantSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentSlotDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.ApplicantMapper;
import com.afrikatek.documentsregistrationservice.service.mapper.AppointmentSlotMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Applicant}.
 */
@Service
@Transactional
public class ApplicantService {

    private final Logger log = LoggerFactory.getLogger(ApplicantService.class);

    private final ApplicantRepository applicantRepository;

    private final ApplicantMapper applicantMapper;

    private final AppointmentSlotMapper appointmentSlotMapper;

    private final ApplicantSearchRepository applicantSearchRepository;

    public ApplicantService(
        ApplicantRepository applicantRepository,
        ApplicantMapper applicantMapper,
        AppointmentSlotMapper appointmentSlotMapper,
        ApplicantSearchRepository applicantSearchRepository
    ) {
        this.applicantRepository = applicantRepository;
        this.applicantMapper = applicantMapper;
        this.appointmentSlotMapper = appointmentSlotMapper;
        this.applicantSearchRepository = applicantSearchRepository;
    }

    /**
     * Save a applicant.
     *
     * @param applicantDTO the entity to save.
     * @return the persisted entity.
     */
    public ApplicantDTO save(ApplicantDTO applicantDTO) {
        log.debug("Request to save Applicant : {}", applicantDTO);
        Applicant applicant = applicantMapper.toEntity(applicantDTO);
        applicant = applicantRepository.save(applicant);
        ApplicantDTO result = applicantMapper.toDto(applicant);
        applicantSearchRepository.save(applicant);
        return result;
    }

    /**
     * Partially update a applicant.
     *
     * @param applicantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApplicantDTO> partialUpdate(ApplicantDTO applicantDTO) {
        log.debug("Request to partially update Applicant : {}", applicantDTO);

        return applicantRepository
            .findById(applicantDTO.getId())
            .map(
                existingApplicant -> {
                    applicantMapper.partialUpdate(existingApplicant, applicantDTO);
                    return existingApplicant;
                }
            )
            .map(applicantRepository::save)
            .map(
                savedApplicant -> {
                    applicantSearchRepository.save(savedApplicant);

                    return savedApplicant;
                }
            )
            .map(applicantMapper::toDto);
    }

    /**
     * Get all the applicants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Applicants");
        return applicantRepository.findAll(pageable).map(applicantMapper::toDto);
    }

    /**
     *  Get all the applicants where MarriageDetails is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicantDTO> findAllWhereMarriageDetailsIsNull() {
        log.debug("Request to get all applicants where MarriageDetails is null");
        return StreamSupport
            .stream(applicantRepository.findAll().spliterator(), false)
            .filter(applicant -> applicant.getMarriageDetails() == null)
            .map(applicantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the applicants where NextOfKeen is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ApplicantDTO> findAllWhereNextOfKeenIsNull() {
        log.debug("Request to get all applicants where NextOfKeen is null");
        return StreamSupport
            .stream(applicantRepository.findAll().spliterator(), false)
            .filter(applicant -> applicant.getNextOfKeen() == null)
            .map(applicantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one applicant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicantDTO> findOne(Long id) {
        log.debug("Request to get Applicant : {}", id);
        return applicantRepository.findById(id).map(applicantMapper::toDto);
    }

    /**
     *
     * @param appointmentSlotDTO
     * @return list of Applicants for a specific appointment slot
     */
    @Transactional(readOnly = true)
    public Optional<List<ApplicantDTO>> findAllByAppointmentSlot(AppointmentSlotDTO appointmentSlotDTO) {
        log.debug("Request to get Applicant for appointment slot : {}", appointmentSlotDTO);
        return applicantRepository.findByAppointmentSlot(appointmentSlotMapper.toEntity(appointmentSlotDTO)).map(applicantMapper::toDto);
    }

    /**
     * Delete the applicant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Applicant : {}", id);
        applicantRepository.deleteById(id);
        applicantSearchRepository.deleteById(id);
    }

    /**
     * Search for the applicant corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicantDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Applicants for query {}", query);
        return applicantSearchRepository.search(queryStringQuery(query), pageable).map(applicantMapper::toDto);
    }
}
