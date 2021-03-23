package com.afrikatek.documentsregistrationservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsregistrationservice.IntegrationTest;
import com.afrikatek.documentsregistrationservice.domain.Appointment;
import com.afrikatek.documentsregistrationservice.domain.enumeration.AppointmentType;
import com.afrikatek.documentsregistrationservice.repository.AppointmentRepository;
import com.afrikatek.documentsregistrationservice.repository.search.AppointmentSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.AppointmentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppointmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppointmentResourceIT {

    private static final Instant DEFAULT_DATE_OF_APPOINTMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_APPOINTMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    private static final AppointmentType DEFAULT_APPOINTMENT_TYPE = AppointmentType.ORDINARY;
    private static final AppointmentType UPDATED_APPOINTMENT_TYPE = AppointmentType.EMERGENCY;

    private static final Integer DEFAULT_MAX_APPOINTMENTS = 0;
    private static final Integer UPDATED_MAX_APPOINTMENTS = 1;

    private static final String ENTITY_API_URL = "/api/appointments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/appointments";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    /**
     * This repository is mocked in the com.afrikatek.documentsregistrationservice.repository.search test package.
     *
     * @see com.afrikatek.documentsregistrationservice.repository.search.AppointmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private AppointmentSearchRepository mockAppointmentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppointmentMockMvc;

    private Appointment appointment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointment createEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .dateOfAppointment(DEFAULT_DATE_OF_APPOINTMENT)
            .available(DEFAULT_AVAILABLE)
            .appointmentType(DEFAULT_APPOINTMENT_TYPE)
            .maxAppointments(DEFAULT_MAX_APPOINTMENTS);
        return appointment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Appointment createUpdatedEntity(EntityManager em) {
        Appointment appointment = new Appointment()
            .dateOfAppointment(UPDATED_DATE_OF_APPOINTMENT)
            .available(UPDATED_AVAILABLE)
            .appointmentType(UPDATED_APPOINTMENT_TYPE)
            .maxAppointments(UPDATED_MAX_APPOINTMENTS);
        return appointment;
    }

    @BeforeEach
    public void initTest() {
        appointment = createEntity(em);
    }

    @Test
    @Transactional
    void createAppointment() throws Exception {
        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();
        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);
        restAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate + 1);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getDateOfAppointment()).isEqualTo(DEFAULT_DATE_OF_APPOINTMENT);
        assertThat(testAppointment.getAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testAppointment.getAppointmentType()).isEqualTo(DEFAULT_APPOINTMENT_TYPE);
        assertThat(testAppointment.getMaxAppointments()).isEqualTo(DEFAULT_MAX_APPOINTMENTS);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(1)).save(testAppointment);
    }

    @Test
    @Transactional
    void createAppointmentWithExistingId() throws Exception {
        // Create the Appointment with an existing ID
        appointment.setId(1L);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        int databaseSizeBeforeCreate = appointmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(0)).save(appointment);
    }

    @Test
    @Transactional
    void checkDateOfAppointmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setDateOfAppointment(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setAvailable(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxAppointmentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentRepository.findAll().size();
        // set the field null
        appointment.setMaxAppointments(null);

        // Create the Appointment, which fails.
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        restAppointmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppointments() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get all the appointmentList
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfAppointment").value(hasItem(DEFAULT_DATE_OF_APPOINTMENT.toString())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].appointmentType").value(hasItem(DEFAULT_APPOINTMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].maxAppointments").value(hasItem(DEFAULT_MAX_APPOINTMENTS)));
    }

    @Test
    @Transactional
    void getAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        // Get the appointment
        restAppointmentMockMvc
            .perform(get(ENTITY_API_URL_ID, appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointment.getId().intValue()))
            .andExpect(jsonPath("$.dateOfAppointment").value(DEFAULT_DATE_OF_APPOINTMENT.toString()))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.appointmentType").value(DEFAULT_APPOINTMENT_TYPE.toString()))
            .andExpect(jsonPath("$.maxAppointments").value(DEFAULT_MAX_APPOINTMENTS));
    }

    @Test
    @Transactional
    void getNonExistingAppointment() throws Exception {
        // Get the appointment
        restAppointmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment
        Appointment updatedAppointment = appointmentRepository.findById(appointment.getId()).get();
        // Disconnect from session so that the updates on updatedAppointment are not directly saved in db
        em.detach(updatedAppointment);
        updatedAppointment
            .dateOfAppointment(UPDATED_DATE_OF_APPOINTMENT)
            .available(UPDATED_AVAILABLE)
            .appointmentType(UPDATED_APPOINTMENT_TYPE)
            .maxAppointments(UPDATED_MAX_APPOINTMENTS);
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(updatedAppointment);

        restAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getDateOfAppointment()).isEqualTo(UPDATED_DATE_OF_APPOINTMENT);
        assertThat(testAppointment.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testAppointment.getAppointmentType()).isEqualTo(UPDATED_APPOINTMENT_TYPE);
        assertThat(testAppointment.getMaxAppointments()).isEqualTo(UPDATED_MAX_APPOINTMENTS);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository).save(testAppointment);
    }

    @Test
    @Transactional
    void putNonExistingAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(0)).save(appointment);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(0)).save(appointment);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(0)).save(appointment);
    }

    @Test
    @Transactional
    void partialUpdateAppointmentWithPatch() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment using partial update
        Appointment partialUpdatedAppointment = new Appointment();
        partialUpdatedAppointment.setId(appointment.getId());

        partialUpdatedAppointment.dateOfAppointment(UPDATED_DATE_OF_APPOINTMENT).appointmentType(UPDATED_APPOINTMENT_TYPE);

        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointment))
            )
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getDateOfAppointment()).isEqualTo(UPDATED_DATE_OF_APPOINTMENT);
        assertThat(testAppointment.getAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testAppointment.getAppointmentType()).isEqualTo(UPDATED_APPOINTMENT_TYPE);
        assertThat(testAppointment.getMaxAppointments()).isEqualTo(DEFAULT_MAX_APPOINTMENTS);
    }

    @Test
    @Transactional
    void fullUpdateAppointmentWithPatch() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();

        // Update the appointment using partial update
        Appointment partialUpdatedAppointment = new Appointment();
        partialUpdatedAppointment.setId(appointment.getId());

        partialUpdatedAppointment
            .dateOfAppointment(UPDATED_DATE_OF_APPOINTMENT)
            .available(UPDATED_AVAILABLE)
            .appointmentType(UPDATED_APPOINTMENT_TYPE)
            .maxAppointments(UPDATED_MAX_APPOINTMENTS);

        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointment))
            )
            .andExpect(status().isOk());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);
        Appointment testAppointment = appointmentList.get(appointmentList.size() - 1);
        assertThat(testAppointment.getDateOfAppointment()).isEqualTo(UPDATED_DATE_OF_APPOINTMENT);
        assertThat(testAppointment.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testAppointment.getAppointmentType()).isEqualTo(UPDATED_APPOINTMENT_TYPE);
        assertThat(testAppointment.getMaxAppointments()).isEqualTo(UPDATED_MAX_APPOINTMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(0)).save(appointment);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(0)).save(appointment);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppointment() throws Exception {
        int databaseSizeBeforeUpdate = appointmentRepository.findAll().size();
        appointment.setId(count.incrementAndGet());

        // Create the Appointment
        AppointmentDTO appointmentDTO = appointmentMapper.toDto(appointment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appointmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Appointment in the database
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(0)).save(appointment);
    }

    @Test
    @Transactional
    void deleteAppointment() throws Exception {
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);

        int databaseSizeBeforeDelete = appointmentRepository.findAll().size();

        // Delete the appointment
        restAppointmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Appointment> appointmentList = appointmentRepository.findAll();
        assertThat(appointmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Appointment in Elasticsearch
        verify(mockAppointmentSearchRepository, times(1)).deleteById(appointment.getId());
    }

    @Test
    @Transactional
    void searchAppointment() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        appointmentRepository.saveAndFlush(appointment);
        when(mockAppointmentSearchRepository.search(queryStringQuery("id:" + appointment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(appointment), PageRequest.of(0, 1), 1));

        // Search the appointment
        restAppointmentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + appointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointment.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfAppointment").value(hasItem(DEFAULT_DATE_OF_APPOINTMENT.toString())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].appointmentType").value(hasItem(DEFAULT_APPOINTMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].maxAppointments").value(hasItem(DEFAULT_MAX_APPOINTMENTS)));
    }
}
