package com.afrikatek.documentsregistrationservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsregistrationservice.IntegrationTest;
import com.afrikatek.documentsregistrationservice.domain.AppointmentSettings;
import com.afrikatek.documentsregistrationservice.repository.AppointmentSettingsRepository;
import com.afrikatek.documentsregistrationservice.repository.search.AppointmentSettingsSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentSettingsDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.AppointmentSettingsMapper;
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
 * Integration tests for the {@link AppointmentSettingsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppointmentSettingsResourceIT {

    private static final Integer DEFAULT_MAX_ORDINARY_APPOINTMENTS = 0;
    private static final Integer UPDATED_MAX_ORDINARY_APPOINTMENTS = 1;

    private static final Integer DEFAULT_MAX_EMERGENCY_APPOINTMENTS = 0;
    private static final Integer UPDATED_MAX_EMERGENCY_APPOINTMENTS = 1;

    private static final Integer DEFAULT_STARTING_WORK_TIME = 0;
    private static final Integer UPDATED_STARTING_WORK_TIME = 1;

    private static final Integer DEFAULT_ENDING_WORK_TIME = 0;
    private static final Integer UPDATED_ENDING_WORK_TIME = 1;

    private static final String ENTITY_API_URL = "/api/appointment-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/appointment-settings";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppointmentSettingsRepository appointmentSettingsRepository;

    @Autowired
    private AppointmentSettingsMapper appointmentSettingsMapper;

    /**
     * This repository is mocked in the com.afrikatek.documentsregistrationservice.repository.search test package.
     *
     * @see com.afrikatek.documentsregistrationservice.repository.search.AppointmentSettingsSearchRepositoryMockConfiguration
     */
    @Autowired
    private AppointmentSettingsSearchRepository mockAppointmentSettingsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppointmentSettingsMockMvc;

    private AppointmentSettings appointmentSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentSettings createEntity(EntityManager em) {
        AppointmentSettings appointmentSettings = new AppointmentSettings()
            .maxOrdinaryAppointments(DEFAULT_MAX_ORDINARY_APPOINTMENTS)
            .maxEmergencyAppointments(DEFAULT_MAX_EMERGENCY_APPOINTMENTS)
            .startingWorkTime(DEFAULT_STARTING_WORK_TIME)
            .endingWorkTime(DEFAULT_ENDING_WORK_TIME);
        return appointmentSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppointmentSettings createUpdatedEntity(EntityManager em) {
        AppointmentSettings appointmentSettings = new AppointmentSettings()
            .maxOrdinaryAppointments(UPDATED_MAX_ORDINARY_APPOINTMENTS)
            .maxEmergencyAppointments(UPDATED_MAX_EMERGENCY_APPOINTMENTS)
            .startingWorkTime(UPDATED_STARTING_WORK_TIME)
            .endingWorkTime(UPDATED_ENDING_WORK_TIME);
        return appointmentSettings;
    }

    @BeforeEach
    public void initTest() {
        appointmentSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createAppointmentSettings() throws Exception {
        int databaseSizeBeforeCreate = appointmentSettingsRepository.findAll().size();
        // Create the AppointmentSettings
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);
        restAppointmentSettingsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        AppointmentSettings testAppointmentSettings = appointmentSettingsList.get(appointmentSettingsList.size() - 1);
        assertThat(testAppointmentSettings.getMaxOrdinaryAppointments()).isEqualTo(DEFAULT_MAX_ORDINARY_APPOINTMENTS);
        assertThat(testAppointmentSettings.getMaxEmergencyAppointments()).isEqualTo(DEFAULT_MAX_EMERGENCY_APPOINTMENTS);
        assertThat(testAppointmentSettings.getStartingWorkTime()).isEqualTo(DEFAULT_STARTING_WORK_TIME);
        assertThat(testAppointmentSettings.getEndingWorkTime()).isEqualTo(DEFAULT_ENDING_WORK_TIME);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository, times(1)).save(testAppointmentSettings);
    }

    @Test
    @Transactional
    void createAppointmentSettingsWithExistingId() throws Exception {
        // Create the AppointmentSettings with an existing ID
        appointmentSettings.setId(1L);
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        int databaseSizeBeforeCreate = appointmentSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointmentSettingsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeCreate);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository, times(0)).save(appointmentSettings);
    }

    @Test
    @Transactional
    void checkMaxOrdinaryAppointmentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentSettingsRepository.findAll().size();
        // set the field null
        appointmentSettings.setMaxOrdinaryAppointments(null);

        // Create the AppointmentSettings, which fails.
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        restAppointmentSettingsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxEmergencyAppointmentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentSettingsRepository.findAll().size();
        // set the field null
        appointmentSettings.setMaxEmergencyAppointments(null);

        // Create the AppointmentSettings, which fails.
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        restAppointmentSettingsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartingWorkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentSettingsRepository.findAll().size();
        // set the field null
        appointmentSettings.setStartingWorkTime(null);

        // Create the AppointmentSettings, which fails.
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        restAppointmentSettingsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndingWorkTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = appointmentSettingsRepository.findAll().size();
        // set the field null
        appointmentSettings.setEndingWorkTime(null);

        // Create the AppointmentSettings, which fails.
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        restAppointmentSettingsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppointmentSettings() throws Exception {
        // Initialize the database
        appointmentSettingsRepository.saveAndFlush(appointmentSettings);

        // Get all the appointmentSettingsList
        restAppointmentSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointmentSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxOrdinaryAppointments").value(hasItem(DEFAULT_MAX_ORDINARY_APPOINTMENTS)))
            .andExpect(jsonPath("$.[*].maxEmergencyAppointments").value(hasItem(DEFAULT_MAX_EMERGENCY_APPOINTMENTS)))
            .andExpect(jsonPath("$.[*].startingWorkTime").value(hasItem(DEFAULT_STARTING_WORK_TIME)))
            .andExpect(jsonPath("$.[*].endingWorkTime").value(hasItem(DEFAULT_ENDING_WORK_TIME)));
    }

    @Test
    @Transactional
    void getAppointmentSettings() throws Exception {
        // Initialize the database
        appointmentSettingsRepository.saveAndFlush(appointmentSettings);

        // Get the appointmentSettings
        restAppointmentSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, appointmentSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appointmentSettings.getId().intValue()))
            .andExpect(jsonPath("$.maxOrdinaryAppointments").value(DEFAULT_MAX_ORDINARY_APPOINTMENTS))
            .andExpect(jsonPath("$.maxEmergencyAppointments").value(DEFAULT_MAX_EMERGENCY_APPOINTMENTS))
            .andExpect(jsonPath("$.startingWorkTime").value(DEFAULT_STARTING_WORK_TIME))
            .andExpect(jsonPath("$.endingWorkTime").value(DEFAULT_ENDING_WORK_TIME));
    }

    @Test
    @Transactional
    void getNonExistingAppointmentSettings() throws Exception {
        // Get the appointmentSettings
        restAppointmentSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAppointmentSettings() throws Exception {
        // Initialize the database
        appointmentSettingsRepository.saveAndFlush(appointmentSettings);

        int databaseSizeBeforeUpdate = appointmentSettingsRepository.findAll().size();

        // Update the appointmentSettings
        AppointmentSettings updatedAppointmentSettings = appointmentSettingsRepository.findById(appointmentSettings.getId()).get();
        // Disconnect from session so that the updates on updatedAppointmentSettings are not directly saved in db
        em.detach(updatedAppointmentSettings);
        updatedAppointmentSettings
            .maxOrdinaryAppointments(UPDATED_MAX_ORDINARY_APPOINTMENTS)
            .maxEmergencyAppointments(UPDATED_MAX_EMERGENCY_APPOINTMENTS)
            .startingWorkTime(UPDATED_STARTING_WORK_TIME)
            .endingWorkTime(UPDATED_ENDING_WORK_TIME);
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(updatedAppointmentSettings);

        restAppointmentSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeUpdate);
        AppointmentSettings testAppointmentSettings = appointmentSettingsList.get(appointmentSettingsList.size() - 1);
        assertThat(testAppointmentSettings.getMaxOrdinaryAppointments()).isEqualTo(UPDATED_MAX_ORDINARY_APPOINTMENTS);
        assertThat(testAppointmentSettings.getMaxEmergencyAppointments()).isEqualTo(UPDATED_MAX_EMERGENCY_APPOINTMENTS);
        assertThat(testAppointmentSettings.getStartingWorkTime()).isEqualTo(UPDATED_STARTING_WORK_TIME);
        assertThat(testAppointmentSettings.getEndingWorkTime()).isEqualTo(UPDATED_ENDING_WORK_TIME);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository).save(testAppointmentSettings);
    }

    @Test
    @Transactional
    void putNonExistingAppointmentSettings() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSettingsRepository.findAll().size();
        appointmentSettings.setId(count.incrementAndGet());

        // Create the AppointmentSettings
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appointmentSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository, times(0)).save(appointmentSettings);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppointmentSettings() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSettingsRepository.findAll().size();
        appointmentSettings.setId(count.incrementAndGet());

        // Create the AppointmentSettings
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository, times(0)).save(appointmentSettings);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppointmentSettings() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSettingsRepository.findAll().size();
        appointmentSettings.setId(count.incrementAndGet());

        // Create the AppointmentSettings
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSettingsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository, times(0)).save(appointmentSettings);
    }

    @Test
    @Transactional
    void partialUpdateAppointmentSettingsWithPatch() throws Exception {
        // Initialize the database
        appointmentSettingsRepository.saveAndFlush(appointmentSettings);

        int databaseSizeBeforeUpdate = appointmentSettingsRepository.findAll().size();

        // Update the appointmentSettings using partial update
        AppointmentSettings partialUpdatedAppointmentSettings = new AppointmentSettings();
        partialUpdatedAppointmentSettings.setId(appointmentSettings.getId());

        partialUpdatedAppointmentSettings.startingWorkTime(UPDATED_STARTING_WORK_TIME).endingWorkTime(UPDATED_ENDING_WORK_TIME);

        restAppointmentSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentSettings))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeUpdate);
        AppointmentSettings testAppointmentSettings = appointmentSettingsList.get(appointmentSettingsList.size() - 1);
        assertThat(testAppointmentSettings.getMaxOrdinaryAppointments()).isEqualTo(DEFAULT_MAX_ORDINARY_APPOINTMENTS);
        assertThat(testAppointmentSettings.getMaxEmergencyAppointments()).isEqualTo(DEFAULT_MAX_EMERGENCY_APPOINTMENTS);
        assertThat(testAppointmentSettings.getStartingWorkTime()).isEqualTo(UPDATED_STARTING_WORK_TIME);
        assertThat(testAppointmentSettings.getEndingWorkTime()).isEqualTo(UPDATED_ENDING_WORK_TIME);
    }

    @Test
    @Transactional
    void fullUpdateAppointmentSettingsWithPatch() throws Exception {
        // Initialize the database
        appointmentSettingsRepository.saveAndFlush(appointmentSettings);

        int databaseSizeBeforeUpdate = appointmentSettingsRepository.findAll().size();

        // Update the appointmentSettings using partial update
        AppointmentSettings partialUpdatedAppointmentSettings = new AppointmentSettings();
        partialUpdatedAppointmentSettings.setId(appointmentSettings.getId());

        partialUpdatedAppointmentSettings
            .maxOrdinaryAppointments(UPDATED_MAX_ORDINARY_APPOINTMENTS)
            .maxEmergencyAppointments(UPDATED_MAX_EMERGENCY_APPOINTMENTS)
            .startingWorkTime(UPDATED_STARTING_WORK_TIME)
            .endingWorkTime(UPDATED_ENDING_WORK_TIME);

        restAppointmentSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppointmentSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppointmentSettings))
            )
            .andExpect(status().isOk());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeUpdate);
        AppointmentSettings testAppointmentSettings = appointmentSettingsList.get(appointmentSettingsList.size() - 1);
        assertThat(testAppointmentSettings.getMaxOrdinaryAppointments()).isEqualTo(UPDATED_MAX_ORDINARY_APPOINTMENTS);
        assertThat(testAppointmentSettings.getMaxEmergencyAppointments()).isEqualTo(UPDATED_MAX_EMERGENCY_APPOINTMENTS);
        assertThat(testAppointmentSettings.getStartingWorkTime()).isEqualTo(UPDATED_STARTING_WORK_TIME);
        assertThat(testAppointmentSettings.getEndingWorkTime()).isEqualTo(UPDATED_ENDING_WORK_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingAppointmentSettings() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSettingsRepository.findAll().size();
        appointmentSettings.setId(count.incrementAndGet());

        // Create the AppointmentSettings
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppointmentSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appointmentSettingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository, times(0)).save(appointmentSettings);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppointmentSettings() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSettingsRepository.findAll().size();
        appointmentSettings.setId(count.incrementAndGet());

        // Create the AppointmentSettings
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository, times(0)).save(appointmentSettings);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppointmentSettings() throws Exception {
        int databaseSizeBeforeUpdate = appointmentSettingsRepository.findAll().size();
        appointmentSettings.setId(count.incrementAndGet());

        // Create the AppointmentSettings
        AppointmentSettingsDTO appointmentSettingsDTO = appointmentSettingsMapper.toDto(appointmentSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppointmentSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appointmentSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppointmentSettings in the database
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository, times(0)).save(appointmentSettings);
    }

    @Test
    @Transactional
    void deleteAppointmentSettings() throws Exception {
        // Initialize the database
        appointmentSettingsRepository.saveAndFlush(appointmentSettings);

        int databaseSizeBeforeDelete = appointmentSettingsRepository.findAll().size();

        // Delete the appointmentSettings
        restAppointmentSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, appointmentSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppointmentSettings> appointmentSettingsList = appointmentSettingsRepository.findAll();
        assertThat(appointmentSettingsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AppointmentSettings in Elasticsearch
        verify(mockAppointmentSettingsSearchRepository, times(1)).deleteById(appointmentSettings.getId());
    }

    @Test
    @Transactional
    void searchAppointmentSettings() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        appointmentSettingsRepository.saveAndFlush(appointmentSettings);
        when(mockAppointmentSettingsSearchRepository.search(queryStringQuery("id:" + appointmentSettings.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(appointmentSettings), PageRequest.of(0, 1), 1));

        // Search the appointmentSettings
        restAppointmentSettingsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + appointmentSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointmentSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].maxOrdinaryAppointments").value(hasItem(DEFAULT_MAX_ORDINARY_APPOINTMENTS)))
            .andExpect(jsonPath("$.[*].maxEmergencyAppointments").value(hasItem(DEFAULT_MAX_EMERGENCY_APPOINTMENTS)))
            .andExpect(jsonPath("$.[*].startingWorkTime").value(hasItem(DEFAULT_STARTING_WORK_TIME)))
            .andExpect(jsonPath("$.[*].endingWorkTime").value(hasItem(DEFAULT_ENDING_WORK_TIME)));
    }
}
