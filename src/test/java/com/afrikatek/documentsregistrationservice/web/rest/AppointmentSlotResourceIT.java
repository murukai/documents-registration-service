package com.afrikatek.documentsregistrationservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsregistrationservice.IntegrationTest;
import com.afrikatek.documentsregistrationservice.domain.AppointmentSlot;
import com.afrikatek.documentsregistrationservice.repository.AppointmentSlotRepository;
import com.afrikatek.documentsregistrationservice.repository.search.AppointmentSlotSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentSlotDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.AppointmentSlotMapper;
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
 * Integration tests for the {@link AppointmentSlotResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppointmentSlotResourceIT {

    private static final Instant DEFAULT_TIME_OF_APPOINTMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_OF_APPOINTMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    private static final Integer DEFAULT_MAX_APPOINTMENTS = 1;
    private static final Integer UPDATED_MAX_APPOINTMENTS = 2;

    private static final String ENTITY_API_URL = "/api/appointment-slots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/appointment-slots";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;

    @Autowired
    private AppointmentSlotMapper appointmentSlotMapper;

    /**
     * This repository is mocked in the com.afrikatek.documentsregistrationservice.repository.search test package.
     *
     * @see com.afrikatek.documentsregistrationservice.repository.search.AppointmentSlotSearchRepositoryMockConfiguration
     */
    @Autowired
    private AppointmentSlotSearchRepository mockAppointmentSlotSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppointmentSlotMockMvc;

    private AppointmentSlot appointmentSlot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentSlot createEntity(EntityManager em) {
        AppointmentSlot appointmentSlot = new AppointmentSlot()
            .timeOfAppointment(DEFAULT_TIME_OF_APPOINTMENT)
            .available(DEFAULT_AVAILABLE)
            .maxAppointments(DEFAULT_MAX_APPOINTMENTS);
        return appointmentSlot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentSlot createUpdatedEntity(EntityManager em) {
        AppointmentSlot appointmentSlot = new AppointmentSlot()
            .timeOfAppointment(UPDATED_TIME_OF_APPOINTMENT)
            .available(UPDATED_AVAILABLE)
            .maxAppointments(UPDATED_MAX_APPOINTMENTS);
        return appointmentSlot;
    }

    @BeforeEach
    public void initTest() {
        appointmentSlot = createEntity(em);
    }

    @Test
    @Transactional
    void createAppointmentSlot() throws Exception {
        int databaseSizeBeforeCreate = appointmentSlotRepository.findAll().size();
        // Create the AppointmentSlot
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);
        restAppointmentSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeCreate + 1);
        AppointmentSlot testAppointmentSlot = appointmentSlotList.get(appointmentSlotList.size() - 1);
        assertThat(testAppointmentSlot.getTimeOfAppointment()).isEqualTo(DEFAULT_TIME_OF_APPOINTMENT);
        assertThat(testAppointmentSlot.getAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testAppointmentSlot.getMaxAppointments()).isEqualTo(DEFAULT_MAX_APPOINTMENTS);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository, times(1)).save(testAppointmentSlot);
    }

    @Test
    @Transactional
    void createAppointmentSlotWithExistingId() throws Exception {
        // Create the AppointmentSlot with an existing ID
        appointmentSlot.setId(1L);
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        int databaseSizeBeforeCreate = appointmentSlotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeCreate);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository, times(0)).save(appointmentSlot);
    }

    @Test
    @Transactional
    void checkTimeOfAppointmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentSlotRepository.findAll().size();
        // set the field null
        appointmentSlot.setTimeOfAppointment(null);

        // Create the AppointmentSlot, which fails.
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        restAppointmentSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentSlotRepository.findAll().size();
        // set the field null
        appointmentSlot.setAvailable(null);

        // Create the AppointmentSlot, which fails.
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        restAppointmentSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxAppointmentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentSlotRepository.findAll().size();
        // set the field null
        appointmentSlot.setMaxAppointments(null);

        // Create the AppointmentSlot, which fails.
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        restAppointmentSlotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppointmentSlots() throws Exception {
        // Initialize the database
        appointmentSlotRepository.saveAndFlush(appointmentSlot);

        // Get all the appointmentSlotList
        restAppointmentSlotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointmentSlot.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfAppointment").value(hasItem(DEFAULT_TIME_OF_APPOINTMENT.toString())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].maxAppointments").value(hasItem(DEFAULT_MAX_APPOINTMENTS)));
    }

    @Test
    @Transactional
    void getAppointmentSlot() throws Exception {
        // Initialize the database
        appointmentSlotRepository.saveAndFlush(appointmentSlot);

        // Get the appointmentSlot
        restAppointmentSlotMockMvc
            .perform(get(ENTITY_API_URL_ID, appointmentSlot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointmentSlot.getId().intValue()))
            .andExpect(jsonPath("$.timeOfAppointment").value(DEFAULT_TIME_OF_APPOINTMENT.toString()))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.maxAppointments").value(DEFAULT_MAX_APPOINTMENTS));
    }

    @Test
    @Transactional
    void getNonExistingAppointmentSlot() throws Exception {
        // Get the appointmentSlot
        restAppointmentSlotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppointmentSlot() throws Exception {
        // Initialize the database
        appointmentSlotRepository.saveAndFlush(appointmentSlot);

        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();

        // Update the appointmentSlot
        AppointmentSlot updatedAppointmentSlot = appointmentSlotRepository.findById(appointmentSlot.getId()).get();
        // Disconnect from session so that the updates on updatedAppointmentSlot are not directly saved in db
        em.detach(updatedAppointmentSlot);
        updatedAppointmentSlot
            .timeOfAppointment(UPDATED_TIME_OF_APPOINTMENT)
            .available(UPDATED_AVAILABLE)
            .maxAppointments(UPDATED_MAX_APPOINTMENTS);
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(updatedAppointmentSlot);

        restAppointmentSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentSlotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
        AppointmentSlot testAppointmentSlot = appointmentSlotList.get(appointmentSlotList.size() - 1);
        assertThat(testAppointmentSlot.getTimeOfAppointment()).isEqualTo(UPDATED_TIME_OF_APPOINTMENT);
        assertThat(testAppointmentSlot.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testAppointmentSlot.getMaxAppointments()).isEqualTo(UPDATED_MAX_APPOINTMENTS);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository).save(testAppointmentSlot);
    }

    @Test
    @Transactional
    void putNonExistingAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(count.incrementAndGet());

        // Create the AppointmentSlot
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentSlotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository, times(0)).save(appointmentSlot);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(count.incrementAndGet());

        // Create the AppointmentSlot
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository, times(0)).save(appointmentSlot);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(count.incrementAndGet());

        // Create the AppointmentSlot
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository, times(0)).save(appointmentSlot);
    }

    @Test
    @Transactional
    void partialUpdateAppointmentSlotWithPatch() throws Exception {
        // Initialize the database
        appointmentSlotRepository.saveAndFlush(appointmentSlot);

        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();

        // Update the appointmentSlot using partial update
        AppointmentSlot partialUpdatedAppointmentSlot = new AppointmentSlot();
        partialUpdatedAppointmentSlot.setId(appointmentSlot.getId());

        partialUpdatedAppointmentSlot.timeOfAppointment(UPDATED_TIME_OF_APPOINTMENT);

        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentSlot))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
        AppointmentSlot testAppointmentSlot = appointmentSlotList.get(appointmentSlotList.size() - 1);
        assertThat(testAppointmentSlot.getTimeOfAppointment()).isEqualTo(UPDATED_TIME_OF_APPOINTMENT);
        assertThat(testAppointmentSlot.getAvailable()).isEqualTo(DEFAULT_AVAILABLE);
        assertThat(testAppointmentSlot.getMaxAppointments()).isEqualTo(DEFAULT_MAX_APPOINTMENTS);
    }

    @Test
    @Transactional
    void fullUpdateAppointmentSlotWithPatch() throws Exception {
        // Initialize the database
        appointmentSlotRepository.saveAndFlush(appointmentSlot);

        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();

        // Update the appointmentSlot using partial update
        AppointmentSlot partialUpdatedAppointmentSlot = new AppointmentSlot();
        partialUpdatedAppointmentSlot.setId(appointmentSlot.getId());

        partialUpdatedAppointmentSlot
            .timeOfAppointment(UPDATED_TIME_OF_APPOINTMENT)
            .available(UPDATED_AVAILABLE)
            .maxAppointments(UPDATED_MAX_APPOINTMENTS);

        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentSlot))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);
        AppointmentSlot testAppointmentSlot = appointmentSlotList.get(appointmentSlotList.size() - 1);
        assertThat(testAppointmentSlot.getTimeOfAppointment()).isEqualTo(UPDATED_TIME_OF_APPOINTMENT);
        assertThat(testAppointmentSlot.getAvailable()).isEqualTo(UPDATED_AVAILABLE);
        assertThat(testAppointmentSlot.getMaxAppointments()).isEqualTo(UPDATED_MAX_APPOINTMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(count.incrementAndGet());

        // Create the AppointmentSlot
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentSlotDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository, times(0)).save(appointmentSlot);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(count.incrementAndGet());

        // Create the AppointmentSlot
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository, times(0)).save(appointmentSlot);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppointmentSlot() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSlotRepository.findAll().size();
        appointmentSlot.setId(count.incrementAndGet());

        // Create the AppointmentSlot
        AppointmentSlotDTO appointmentSlotDTO = appointmentSlotMapper.toDto(appointmentSlot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSlotMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSlotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentSlot in the database
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository, times(0)).save(appointmentSlot);
    }

    @Test
    @Transactional
    void deleteAppointmentSlot() throws Exception {
        // Initialize the database
        appointmentSlotRepository.saveAndFlush(appointmentSlot);

        int databaseSizeBeforeDelete = appointmentSlotRepository.findAll().size();

        // Delete the appointmentSlot
        restAppointmentSlotMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointmentSlot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppointmentSlot> appointmentSlotList = appointmentSlotRepository.findAll();
        assertThat(appointmentSlotList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AppointmentSlot in Elasticsearch
        verify(mockAppointmentSlotSearchRepository, times(1)).deleteById(appointmentSlot.getId());
    }

    @Test
    @Transactional
    void searchAppointmentSlot() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        appointmentSlotRepository.saveAndFlush(appointmentSlot);
        when(mockAppointmentSlotSearchRepository.search(queryStringQuery("id:" + appointmentSlot.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(appointmentSlot), PageRequest.of(0, 1), 1));

        // Search the appointmentSlot
        restAppointmentSlotMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + appointmentSlot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointmentSlot.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfAppointment").value(hasItem(DEFAULT_TIME_OF_APPOINTMENT.toString())))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].maxAppointments").value(hasItem(DEFAULT_MAX_APPOINTMENTS)));
    }
}
