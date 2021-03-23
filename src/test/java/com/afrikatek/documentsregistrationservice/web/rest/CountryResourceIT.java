package com.afrikatek.documentsregistrationservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsregistrationservice.IntegrationTest;
import com.afrikatek.documentsregistrationservice.domain.Country;
import com.afrikatek.documentsregistrationservice.repository.CountryRepository;
import com.afrikatek.documentsregistrationservice.repository.search.CountrySearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.CountryDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.CountryMapper;
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
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CountryResourceIT {

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CALLING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CALLING_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_REGION = "AAAAAAAAAA";
    private static final String UPDATED_SUB_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final Integer DEFAULT_POPULATION = 1;
    private static final Integer UPDATED_POPULATION = 2;

    private static final String DEFAULT_TIME_ZONE = "AAAAAAAAAA";
    private static final String UPDATED_TIME_ZONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERIC_CODE = 1;
    private static final Integer UPDATED_NUMERIC_CODE = 2;

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/countries";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    /**
     * This repository is mocked in the com.afrikatek.documentsregistrationservice.repository.search test package.
     *
     * @see com.afrikatek.documentsregistrationservice.repository.search.CountrySearchRepositoryMockConfiguration
     */
    @Autowired
    private CountrySearchRepository mockCountrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryMockMvc;

    private Country country;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity(EntityManager em) {
        Country country = new Country()
            .countryName(DEFAULT_COUNTRY_NAME)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .callingCode(DEFAULT_CALLING_CODE)
            .subRegion(DEFAULT_SUB_REGION)
            .region(DEFAULT_REGION)
            .population(DEFAULT_POPULATION)
            .timeZone(DEFAULT_TIME_ZONE)
            .numericCode(DEFAULT_NUMERIC_CODE);
        return country;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity(EntityManager em) {
        Country country = new Country()
            .countryName(UPDATED_COUNTRY_NAME)
            .countryCode(UPDATED_COUNTRY_CODE)
            .callingCode(UPDATED_CALLING_CODE)
            .subRegion(UPDATED_SUB_REGION)
            .region(UPDATED_REGION)
            .population(UPDATED_POPULATION)
            .timeZone(UPDATED_TIME_ZONE)
            .numericCode(UPDATED_NUMERIC_CODE);
        return country;
    }

    @BeforeEach
    public void initTest() {
        country = createEntity(em);
    }

    @Test
    @Transactional
    void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();
        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
        assertThat(testCountry.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testCountry.getCallingCode()).isEqualTo(DEFAULT_CALLING_CODE);
        assertThat(testCountry.getSubRegion()).isEqualTo(DEFAULT_SUB_REGION);
        assertThat(testCountry.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testCountry.getPopulation()).isEqualTo(DEFAULT_POPULATION);
        assertThat(testCountry.getTimeZone()).isEqualTo(DEFAULT_TIME_ZONE);
        assertThat(testCountry.getNumericCode()).isEqualTo(DEFAULT_NUMERIC_CODE);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(1)).save(testCountry);
    }

    @Test
    @Transactional
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(0)).save(country);
    }

    @Test
    @Transactional
    void checkCountryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryName(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryCode(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCallingCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCallingCode(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountries() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].callingCode").value(hasItem(DEFAULT_CALLING_CODE)))
            .andExpect(jsonPath("$.[*].subRegion").value(hasItem(DEFAULT_SUB_REGION)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].population").value(hasItem(DEFAULT_POPULATION)))
            .andExpect(jsonPath("$.[*].timeZone").value(hasItem(DEFAULT_TIME_ZONE)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)));
    }

    @Test
    @Transactional
    void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc
            .perform(get(ENTITY_API_URL_ID, country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE))
            .andExpect(jsonPath("$.callingCode").value(DEFAULT_CALLING_CODE))
            .andExpect(jsonPath("$.subRegion").value(DEFAULT_SUB_REGION))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION))
            .andExpect(jsonPath("$.population").value(DEFAULT_POPULATION))
            .andExpect(jsonPath("$.timeZone").value(DEFAULT_TIME_ZONE))
            .andExpect(jsonPath("$.numericCode").value(DEFAULT_NUMERIC_CODE));
    }

    @Test
    @Transactional
    void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).get();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry
            .countryName(UPDATED_COUNTRY_NAME)
            .countryCode(UPDATED_COUNTRY_CODE)
            .callingCode(UPDATED_CALLING_CODE)
            .subRegion(UPDATED_SUB_REGION)
            .region(UPDATED_REGION)
            .population(UPDATED_POPULATION)
            .timeZone(UPDATED_TIME_ZONE)
            .numericCode(UPDATED_NUMERIC_CODE);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
        assertThat(testCountry.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testCountry.getCallingCode()).isEqualTo(UPDATED_CALLING_CODE);
        assertThat(testCountry.getSubRegion()).isEqualTo(UPDATED_SUB_REGION);
        assertThat(testCountry.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testCountry.getPopulation()).isEqualTo(UPDATED_POPULATION);
        assertThat(testCountry.getTimeZone()).isEqualTo(UPDATED_TIME_ZONE);
        assertThat(testCountry.getNumericCode()).isEqualTo(UPDATED_NUMERIC_CODE);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository).save(testCountry);
    }

    @Test
    @Transactional
    void putNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(0)).save(country);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(0)).save(country);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(0)).save(country);
    }

    @Test
    @Transactional
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .countryName(UPDATED_COUNTRY_NAME)
            .subRegion(UPDATED_SUB_REGION)
            .population(UPDATED_POPULATION)
            .timeZone(UPDATED_TIME_ZONE);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
        assertThat(testCountry.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testCountry.getCallingCode()).isEqualTo(DEFAULT_CALLING_CODE);
        assertThat(testCountry.getSubRegion()).isEqualTo(UPDATED_SUB_REGION);
        assertThat(testCountry.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testCountry.getPopulation()).isEqualTo(UPDATED_POPULATION);
        assertThat(testCountry.getTimeZone()).isEqualTo(UPDATED_TIME_ZONE);
        assertThat(testCountry.getNumericCode()).isEqualTo(DEFAULT_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setId(country.getId());

        partialUpdatedCountry
            .countryName(UPDATED_COUNTRY_NAME)
            .countryCode(UPDATED_COUNTRY_CODE)
            .callingCode(UPDATED_CALLING_CODE)
            .subRegion(UPDATED_SUB_REGION)
            .region(UPDATED_REGION)
            .population(UPDATED_POPULATION)
            .timeZone(UPDATED_TIME_ZONE)
            .numericCode(UPDATED_NUMERIC_CODE);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
        assertThat(testCountry.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testCountry.getCallingCode()).isEqualTo(UPDATED_CALLING_CODE);
        assertThat(testCountry.getSubRegion()).isEqualTo(UPDATED_SUB_REGION);
        assertThat(testCountry.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testCountry.getPopulation()).isEqualTo(UPDATED_POPULATION);
        assertThat(testCountry.getTimeZone()).isEqualTo(UPDATED_TIME_ZONE);
        assertThat(testCountry.getNumericCode()).isEqualTo(UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(0)).save(country);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(0)).save(country);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(0)).save(country);
    }

    @Test
    @Transactional
    void deleteCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Delete the country
        restCountryMockMvc
            .perform(delete(ENTITY_API_URL_ID, country.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(1)).deleteById(country.getId());
    }

    @Test
    @Transactional
    void searchCountry() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        countryRepository.saveAndFlush(country);
        when(mockCountrySearchRepository.search(queryStringQuery("id:" + country.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(country), PageRequest.of(0, 1), 1));

        // Search the country
        restCountryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].callingCode").value(hasItem(DEFAULT_CALLING_CODE)))
            .andExpect(jsonPath("$.[*].subRegion").value(hasItem(DEFAULT_SUB_REGION)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].population").value(hasItem(DEFAULT_POPULATION)))
            .andExpect(jsonPath("$.[*].timeZone").value(hasItem(DEFAULT_TIME_ZONE)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)));
    }
}
