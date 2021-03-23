package com.afrikatek.documentsregistrationservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsregistrationservice.IntegrationTest;
import com.afrikatek.documentsregistrationservice.domain.Holiday;
import com.afrikatek.documentsregistrationservice.domain.enumeration.HolidayMonth;
import com.afrikatek.documentsregistrationservice.repository.HolidayRepository;
import com.afrikatek.documentsregistrationservice.repository.search.HolidaySearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.HolidayDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.HolidayMapper;
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
 * Integration tests for the {@link HolidayResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HolidayResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final HolidayMonth DEFAULT_MONTH = HolidayMonth.JANUARY;
    private static final HolidayMonth UPDATED_MONTH = HolidayMonth.FEBRUARY;

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;

    private static final String ENTITY_API_URL = "/api/holidays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/holidays";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private HolidayMapper holidayMapper;

    /**
     * This repository is mocked in the com.afrikatek.documentsregistrationservice.repository.search test package.
     *
     * @see com.afrikatek.documentsregistrationservice.repository.search.HolidaySearchRepositoryMockConfiguration
     */
    @Autowired
    private HolidaySearchRepository mockHolidaySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHolidayMockMvc;

    private Holiday holiday;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Holiday createEntity(EntityManager em) {
        Holiday holiday = new Holiday().name(DEFAULT_NAME).month(DEFAULT_MONTH).day(DEFAULT_DAY);
        return holiday;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Holiday createUpdatedEntity(EntityManager em) {
        Holiday holiday = new Holiday().name(UPDATED_NAME).month(UPDATED_MONTH).day(UPDATED_DAY);
        return holiday;
    }

    @BeforeEach
    public void initTest() {
        holiday = createEntity(em);
    }

    @Test
    @Transactional
    void createHoliday() throws Exception {
        int databaseSizeBeforeCreate = holidayRepository.findAll().size();
        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);
        restHolidayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
            .andExpect(status().isCreated());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeCreate + 1);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHoliday.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testHoliday.getDay()).isEqualTo(DEFAULT_DAY);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(1)).save(testHoliday);
    }

    @Test
    @Transactional
    void createHolidayWithExistingId() throws Exception {
        // Create the Holiday with an existing ID
        holiday.setId(1L);
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        int databaseSizeBeforeCreate = holidayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHolidayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeCreate);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(0)).save(holiday);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayRepository.findAll().size();
        // set the field null
        holiday.setName(null);

        // Create the Holiday, which fails.
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        restHolidayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
            .andExpect(status().isBadRequest());

        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayRepository.findAll().size();
        // set the field null
        holiday.setMonth(null);

        // Create the Holiday, which fails.
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        restHolidayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
            .andExpect(status().isBadRequest());

        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayRepository.findAll().size();
        // set the field null
        holiday.setDay(null);

        // Create the Holiday, which fails.
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        restHolidayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
            .andExpect(status().isBadRequest());

        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHolidays() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get all the holidayList
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)));
    }

    @Test
    @Transactional
    void getHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        // Get the holiday
        restHolidayMockMvc
            .perform(get(ENTITY_API_URL_ID, holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(holiday.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY));
    }

    @Test
    @Transactional
    void getNonExistingHoliday() throws Exception {
        // Get the holiday
        restHolidayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday
        Holiday updatedHoliday = holidayRepository.findById(holiday.getId()).get();
        // Disconnect from session so that the updates on updatedHoliday are not directly saved in db
        em.detach(updatedHoliday);
        updatedHoliday.name(UPDATED_NAME).month(UPDATED_MONTH).day(UPDATED_DAY);
        HolidayDTO holidayDTO = holidayMapper.toDto(updatedHoliday);

        restHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holidayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHoliday.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testHoliday.getDay()).isEqualTo(UPDATED_DAY);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository).save(testHoliday);
    }

    @Test
    @Transactional
    void putNonExistingHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holidayDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(0)).save(holiday);
    }

    @Test
    @Transactional
    void putWithIdMismatchHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(0)).save(holiday);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(0)).save(holiday);
    }

    @Test
    @Transactional
    void partialUpdateHolidayWithPatch() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday using partial update
        Holiday partialUpdatedHoliday = new Holiday();
        partialUpdatedHoliday.setId(holiday.getId());

        partialUpdatedHoliday.name(UPDATED_NAME).day(UPDATED_DAY);

        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoliday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoliday))
            )
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHoliday.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testHoliday.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    void fullUpdateHolidayWithPatch() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();

        // Update the holiday using partial update
        Holiday partialUpdatedHoliday = new Holiday();
        partialUpdatedHoliday.setId(holiday.getId());

        partialUpdatedHoliday.name(UPDATED_NAME).month(UPDATED_MONTH).day(UPDATED_DAY);

        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHoliday.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHoliday))
            )
            .andExpect(status().isOk());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);
        Holiday testHoliday = holidayList.get(holidayList.size() - 1);
        assertThat(testHoliday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHoliday.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testHoliday.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    void patchNonExistingHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, holidayDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(0)).save(holiday);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(0)).save(holiday);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHoliday() throws Exception {
        int databaseSizeBeforeUpdate = holidayRepository.findAll().size();
        holiday.setId(count.incrementAndGet());

        // Create the Holiday
        HolidayDTO holidayDTO = holidayMapper.toDto(holiday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(holidayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Holiday in the database
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(0)).save(holiday);
    }

    @Test
    @Transactional
    void deleteHoliday() throws Exception {
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);

        int databaseSizeBeforeDelete = holidayRepository.findAll().size();

        // Delete the holiday
        restHolidayMockMvc
            .perform(delete(ENTITY_API_URL_ID, holiday.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Holiday in Elasticsearch
        verify(mockHolidaySearchRepository, times(1)).deleteById(holiday.getId());
    }

    @Test
    @Transactional
    void searchHoliday() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        holidayRepository.saveAndFlush(holiday);
        when(mockHolidaySearchRepository.search(queryStringQuery("id:" + holiday.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(holiday), PageRequest.of(0, 1), 1));

        // Search the holiday
        restHolidayMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + holiday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holiday.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)));
    }
}
