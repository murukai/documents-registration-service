package com.afrikatek.documentsregistrationservice.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.domain.AppointmentSlot;
import com.afrikatek.documentsregistrationservice.repository.AppointmentSlotRepository;
import com.afrikatek.documentsregistrationservice.repository.search.AppointmentSlotSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentDTO;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentSlotDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.AppointmentMapper;
import com.afrikatek.documentsregistrationservice.service.mapper.AppointmentSlotMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AppointmentSlot}.
 */
@Service
@Transactional
public class AppointmentSlotService {

    private final Logger log = LoggerFactory.getLogger(AppointmentSlotService.class);

    private final AppointmentSlotRepository appointmentSlotRepository;

    private final AppointmentSlotMapper appointmentSlotMapper;

    private final AppointmentMapper appointmentMapper;

    private final AppointmentSlotSearchRepository appointmentSlotSearchRepository;

    public AppointmentSlotService(
        AppointmentSlotRepository appointmentSlotRepository,
        AppointmentSlotMapper appointmentSlotMapper,
        AppointmentMapper applicantMapper,
        AppointmentSlotSearchRepository appointmentSlotSearchRepository
    ) {
        this.appointmentSlotRepository = appointmentSlotRepository;
        this.appointmentSlotMapper = appointmentSlotMapper;
        this.appointmentMapper = applicantMapper;
        this.appointmentSlotSearchRepository = appointmentSlotSearchRepository;
    }

    /**
     * Save a appointmentSlot.
     *
     * @param appointmentSlotDTO the entity to save.
     * @return the persisted entity.
     */
    public AppointmentSlotDTO save(AppointmentSlotDTO appointmentSlotDTO) {
        log.debug("Request to save AppointmentSlot : {}", appointmentSlotDTO);
        AppointmentSlot appointmentSlot = appointmentSlotMapper.toEntity(appointmentSlotDTO);
        appointmentSlot = appointmentSlotRepository.save(appointmentSlot);
        AppointmentSlotDTO result = appointmentSlotMapper.toDto(appointmentSlot);
        appointmentSlotSearchRepository.save(appointmentSlot);
        return result;
    }

    /**
     * Partially update a appointmentSlot.
     *
     * @param appointmentSlotDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppointmentSlotDTO> partialUpdate(AppointmentSlotDTO appointmentSlotDTO) {
        log.debug("Request to partially update AppointmentSlot : {}", appointmentSlotDTO);

        return appointmentSlotRepository
            .findById(appointmentSlotDTO.getId())
            .map(
                existingAppointmentSlot -> {
                    appointmentSlotMapper.partialUpdate(existingAppointmentSlot, appointmentSlotDTO);
                    return existingAppointmentSlot;
                }
            )
            .map(appointmentSlotRepository::save)
            .map(
                savedAppointmentSlot -> {
                    appointmentSlotSearchRepository.save(savedAppointmentSlot);

                    return savedAppointmentSlot;
                }
            )
            .map(appointmentSlotMapper::toDto);
    }

    /**
     * Get all the appointmentSlots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointmentSlotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppointmentSlots");
        return appointmentSlotRepository.findAll(pageable).map(appointmentSlotMapper::toDto);
    }

    /**
     * Get one appointmentSlot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppointmentSlotDTO> findOne(Long id) {
        log.debug("Request to get AppointmentSlot : {}", id);
        return appointmentSlotRepository.findById(id).map(appointmentSlotMapper::toDto);
    }

    /**
     *
     * @param appointmentDTO
     * @return list of all appointment slots in the that appointmnet date
     */
    public Optional<List<AppointmentSlotDTO>> findByAppointment(AppointmentDTO appointmentDTO) {
        log.debug("Request to get AppointmentSlot for appointment : {}", appointmentDTO);
        return appointmentSlotRepository.findByAppointment(appointmentMapper.toEntity(appointmentDTO)).map(appointmentSlotMapper::toDto);
    }

    /**
     * Delete the appointmentSlot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppointmentSlot : {}", id);
        appointmentSlotRepository.deleteById(id);
        appointmentSlotSearchRepository.deleteById(id);
    }

    /**
     * Search for the appointmentSlot corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointmentSlotDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AppointmentSlots for query {}", query);
        return appointmentSlotSearchRepository.search(queryStringQuery(query), pageable).map(appointmentSlotMapper::toDto);
    }
}
