package com.afrikatek.documentsregistrationservice.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.domain.Appointment;
import com.afrikatek.documentsregistrationservice.repository.AppointmentRepository;
import com.afrikatek.documentsregistrationservice.repository.search.AppointmentSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.AppointmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Appointment}.
 */
@Service
@Transactional
public class AppointmentService {

    private final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    private final AppointmentSearchRepository appointmentSearchRepository;

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        AppointmentMapper appointmentMapper,
        AppointmentSearchRepository appointmentSearchRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentMapper = appointmentMapper;
        this.appointmentSearchRepository = appointmentSearchRepository;
    }

    /**
     * Save a appointment.
     *
     * @param appointmentDTO the entity to save.
     * @return the persisted entity.
     */
    public AppointmentDTO save(AppointmentDTO appointmentDTO) {
        log.debug("Request to save Appointment : {}", appointmentDTO);
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment = appointmentRepository.save(appointment);
        AppointmentDTO result = appointmentMapper.toDto(appointment);
        appointmentSearchRepository.save(appointment);
        return result;
    }

    /**
     * Partially update a appointment.
     *
     * @param appointmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppointmentDTO> partialUpdate(AppointmentDTO appointmentDTO) {
        log.debug("Request to partially update Appointment : {}", appointmentDTO);

        return appointmentRepository
            .findById(appointmentDTO.getId())
            .map(
                existingAppointment -> {
                    appointmentMapper.partialUpdate(existingAppointment, appointmentDTO);
                    return existingAppointment;
                }
            )
            .map(appointmentRepository::save)
            .map(
                savedAppointment -> {
                    appointmentSearchRepository.save(savedAppointment);

                    return savedAppointment;
                }
            )
            .map(appointmentMapper::toDto);
    }

    /**
     * Get all the appointments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appointments");
        return appointmentRepository.findAll(pageable).map(appointmentMapper::toDto);
    }

    /**
     * Get one appointment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppointmentDTO> findOne(Long id) {
        log.debug("Request to get Appointment : {}", id);
        return appointmentRepository.findById(id).map(appointmentMapper::toDto);
    }

    /**
     * Delete the appointment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Appointment : {}", id);
        appointmentRepository.deleteById(id);
        appointmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the appointment corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Appointments for query {}", query);
        return appointmentSearchRepository.search(queryStringQuery(query), pageable).map(appointmentMapper::toDto);
    }
}
