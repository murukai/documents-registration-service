package com.afrikatek.documentsregistrationservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsregistrationservice.IntegrationTest;
import com.afrikatek.documentsregistrationservice.domain.Applicant;
import com.afrikatek.documentsregistrationservice.domain.Country;
import com.afrikatek.documentsregistrationservice.domain.MarriageDetails;
import com.afrikatek.documentsregistrationservice.repository.MarriageDetailsRepository;
import com.afrikatek.documentsregistrationservice.repository.search.MarriageDetailsSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.MarriageDetailsDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.MarriageDetailsMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link MarriageDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MarriageDetailsResourceIT {

    private static final LocalDate DEFAULT_DATE_OF_MARRIAGE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_MARRIAGE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SPOUSE_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SPOUSE_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE_OF_MARRIAGE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_MARRIAGE = "BBBBBBBBBB";

    private static final String DEFAULT_SPOUSE_PLACE_OF_BIRTH = "AAAAAAAAAA";
    private static final String UPDATED_SPOUSE_PLACE_OF_BIRTH = "BBBBBBBBBB";

    private static final String DEFAULT_MARRIAGE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MARRIAGE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MARRIED_BEFORE = false;
    private static final Boolean UPDATED_MARRIED_BEFORE = true;

    private static final String ENTITY_API_URL = "/api/marriage-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/marriage-details";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MarriageDetailsRepository marriageDetailsRepository;

    @Autowired
    private MarriageDetailsMapper marriageDetailsMapper;

    /**
     * This repository is mocked in the com.afrikatek.documentsregistrationservice.repository.search test package.
     *
     * @see com.afrikatek.documentsregistrationservice.repository.search.MarriageDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private MarriageDetailsSearchRepository mockMarriageDetailsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarriageDetailsMockMvc;

    private MarriageDetails marriageDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarriageDetails createEntity(EntityManager em) {
        MarriageDetails marriageDetails = new MarriageDetails()
            .dateOfMarriage(DEFAULT_DATE_OF_MARRIAGE)
            .spouseFullName(DEFAULT_SPOUSE_FULL_NAME)
            .placeOfMarriage(DEFAULT_PLACE_OF_MARRIAGE)
            .spousePlaceOfBirth(DEFAULT_SPOUSE_PLACE_OF_BIRTH)
            .marriageNumber(DEFAULT_MARRIAGE_NUMBER)
            .marriedBefore(DEFAULT_MARRIED_BEFORE);
        // Add required entity
        Applicant applicant;
        if (TestUtil.findAll(em, Applicant.class).isEmpty()) {
            applicant = ApplicantResourceIT.createEntity(em);
            em.persist(applicant);
            em.flush();
        } else {
            applicant = TestUtil.findAll(em, Applicant.class).get(0);
        }
        marriageDetails.setApplicant(applicant);
        // Add required entity
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        marriageDetails.setCountryOfMarriage(country);
        // Add required entity
        marriageDetails.setSpouseCountryOfBirth(country);
        return marriageDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MarriageDetails createUpdatedEntity(EntityManager em) {
        MarriageDetails marriageDetails = new MarriageDetails()
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .spouseFullName(UPDATED_SPOUSE_FULL_NAME)
            .placeOfMarriage(UPDATED_PLACE_OF_MARRIAGE)
            .spousePlaceOfBirth(UPDATED_SPOUSE_PLACE_OF_BIRTH)
            .marriageNumber(UPDATED_MARRIAGE_NUMBER)
            .marriedBefore(UPDATED_MARRIED_BEFORE);
        // Add required entity
        Applicant applicant;
        if (TestUtil.findAll(em, Applicant.class).isEmpty()) {
            applicant = ApplicantResourceIT.createUpdatedEntity(em);
            em.persist(applicant);
            em.flush();
        } else {
            applicant = TestUtil.findAll(em, Applicant.class).get(0);
        }
        marriageDetails.setApplicant(applicant);
        // Add required entity
        Country country;
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createUpdatedEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        marriageDetails.setCountryOfMarriage(country);
        // Add required entity
        marriageDetails.setSpouseCountryOfBirth(country);
        return marriageDetails;
    }

    @BeforeEach
    public void initTest() {
        marriageDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createMarriageDetails() throws Exception {
        int databaseSizeBeforeCreate = marriageDetailsRepository.findAll().size();
        // Create the MarriageDetails
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);
        restMarriageDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        MarriageDetails testMarriageDetails = marriageDetailsList.get(marriageDetailsList.size() - 1);
        assertThat(testMarriageDetails.getDateOfMarriage()).isEqualTo(DEFAULT_DATE_OF_MARRIAGE);
        assertThat(testMarriageDetails.getSpouseFullName()).isEqualTo(DEFAULT_SPOUSE_FULL_NAME);
        assertThat(testMarriageDetails.getPlaceOfMarriage()).isEqualTo(DEFAULT_PLACE_OF_MARRIAGE);
        assertThat(testMarriageDetails.getSpousePlaceOfBirth()).isEqualTo(DEFAULT_SPOUSE_PLACE_OF_BIRTH);
        assertThat(testMarriageDetails.getMarriageNumber()).isEqualTo(DEFAULT_MARRIAGE_NUMBER);
        assertThat(testMarriageDetails.getMarriedBefore()).isEqualTo(DEFAULT_MARRIED_BEFORE);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository, times(1)).save(testMarriageDetails);
    }

    @Test
    @Transactional
    void createMarriageDetailsWithExistingId() throws Exception {
        // Create the MarriageDetails with an existing ID
        marriageDetails.setId(1L);
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        int databaseSizeBeforeCreate = marriageDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarriageDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository, times(0)).save(marriageDetails);
    }

    @Test
    @Transactional
    void checkDateOfMarriageIsRequired() throws Exception {
        int databaseSizeBeforeTest = marriageDetailsRepository.findAll().size();
        // set the field null
        marriageDetails.setDateOfMarriage(null);

        // Create the MarriageDetails, which fails.
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        restMarriageDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpouseFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = marriageDetailsRepository.findAll().size();
        // set the field null
        marriageDetails.setSpouseFullName(null);

        // Create the MarriageDetails, which fails.
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        restMarriageDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlaceOfMarriageIsRequired() throws Exception {
        int databaseSizeBeforeTest = marriageDetailsRepository.findAll().size();
        // set the field null
        marriageDetails.setPlaceOfMarriage(null);

        // Create the MarriageDetails, which fails.
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        restMarriageDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpousePlaceOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = marriageDetailsRepository.findAll().size();
        // set the field null
        marriageDetails.setSpousePlaceOfBirth(null);

        // Create the MarriageDetails, which fails.
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        restMarriageDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMarriageNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = marriageDetailsRepository.findAll().size();
        // set the field null
        marriageDetails.setMarriageNumber(null);

        // Create the MarriageDetails, which fails.
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        restMarriageDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMarriedBeforeIsRequired() throws Exception {
        int databaseSizeBeforeTest = marriageDetailsRepository.findAll().size();
        // set the field null
        marriageDetails.setMarriedBefore(null);

        // Create the MarriageDetails, which fails.
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        restMarriageDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarriageDetails() throws Exception {
        // Initialize the database
        marriageDetailsRepository.saveAndFlush(marriageDetails);

        // Get all the marriageDetailsList
        restMarriageDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marriageDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfMarriage").value(hasItem(DEFAULT_DATE_OF_MARRIAGE.toString())))
            .andExpect(jsonPath("$.[*].spouseFullName").value(hasItem(DEFAULT_SPOUSE_FULL_NAME)))
            .andExpect(jsonPath("$.[*].placeOfMarriage").value(hasItem(DEFAULT_PLACE_OF_MARRIAGE)))
            .andExpect(jsonPath("$.[*].spousePlaceOfBirth").value(hasItem(DEFAULT_SPOUSE_PLACE_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].marriageNumber").value(hasItem(DEFAULT_MARRIAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].marriedBefore").value(hasItem(DEFAULT_MARRIED_BEFORE.booleanValue())));
    }

    @Test
    @Transactional
    void getMarriageDetails() throws Exception {
        // Initialize the database
        marriageDetailsRepository.saveAndFlush(marriageDetails);

        // Get the marriageDetails
        restMarriageDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, marriageDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marriageDetails.getId().intValue()))
            .andExpect(jsonPath("$.dateOfMarriage").value(DEFAULT_DATE_OF_MARRIAGE.toString()))
            .andExpect(jsonPath("$.spouseFullName").value(DEFAULT_SPOUSE_FULL_NAME))
            .andExpect(jsonPath("$.placeOfMarriage").value(DEFAULT_PLACE_OF_MARRIAGE))
            .andExpect(jsonPath("$.spousePlaceOfBirth").value(DEFAULT_SPOUSE_PLACE_OF_BIRTH))
            .andExpect(jsonPath("$.marriageNumber").value(DEFAULT_MARRIAGE_NUMBER))
            .andExpect(jsonPath("$.marriedBefore").value(DEFAULT_MARRIED_BEFORE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMarriageDetails() throws Exception {
        // Get the marriageDetails
        restMarriageDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMarriageDetails() throws Exception {
        // Initialize the database
        marriageDetailsRepository.saveAndFlush(marriageDetails);

        int databaseSizeBeforeUpdate = marriageDetailsRepository.findAll().size();

        // Update the marriageDetails
        MarriageDetails updatedMarriageDetails = marriageDetailsRepository.findById(marriageDetails.getId()).get();
        // Disconnect from session so that the updates on updatedMarriageDetails are not directly saved in db
        em.detach(updatedMarriageDetails);
        updatedMarriageDetails
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .spouseFullName(UPDATED_SPOUSE_FULL_NAME)
            .placeOfMarriage(UPDATED_PLACE_OF_MARRIAGE)
            .spousePlaceOfBirth(UPDATED_SPOUSE_PLACE_OF_BIRTH)
            .marriageNumber(UPDATED_MARRIAGE_NUMBER)
            .marriedBefore(UPDATED_MARRIED_BEFORE);
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(updatedMarriageDetails);

        restMarriageDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marriageDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeUpdate);
        MarriageDetails testMarriageDetails = marriageDetailsList.get(marriageDetailsList.size() - 1);
        assertThat(testMarriageDetails.getDateOfMarriage()).isEqualTo(UPDATED_DATE_OF_MARRIAGE);
        assertThat(testMarriageDetails.getSpouseFullName()).isEqualTo(UPDATED_SPOUSE_FULL_NAME);
        assertThat(testMarriageDetails.getPlaceOfMarriage()).isEqualTo(UPDATED_PLACE_OF_MARRIAGE);
        assertThat(testMarriageDetails.getSpousePlaceOfBirth()).isEqualTo(UPDATED_SPOUSE_PLACE_OF_BIRTH);
        assertThat(testMarriageDetails.getMarriageNumber()).isEqualTo(UPDATED_MARRIAGE_NUMBER);
        assertThat(testMarriageDetails.getMarriedBefore()).isEqualTo(UPDATED_MARRIED_BEFORE);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository).save(testMarriageDetails);
    }

    @Test
    @Transactional
    void putNonExistingMarriageDetails() throws Exception {
        int databaseSizeBeforeUpdate = marriageDetailsRepository.findAll().size();
        marriageDetails.setId(count.incrementAndGet());

        // Create the MarriageDetails
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarriageDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, marriageDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository, times(0)).save(marriageDetails);
    }

    @Test
    @Transactional
    void putWithIdMismatchMarriageDetails() throws Exception {
        int databaseSizeBeforeUpdate = marriageDetailsRepository.findAll().size();
        marriageDetails.setId(count.incrementAndGet());

        // Create the MarriageDetails
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarriageDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository, times(0)).save(marriageDetails);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMarriageDetails() throws Exception {
        int databaseSizeBeforeUpdate = marriageDetailsRepository.findAll().size();
        marriageDetails.setId(count.incrementAndGet());

        // Create the MarriageDetails
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarriageDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository, times(0)).save(marriageDetails);
    }

    @Test
    @Transactional
    void partialUpdateMarriageDetailsWithPatch() throws Exception {
        // Initialize the database
        marriageDetailsRepository.saveAndFlush(marriageDetails);

        int databaseSizeBeforeUpdate = marriageDetailsRepository.findAll().size();

        // Update the marriageDetails using partial update
        MarriageDetails partialUpdatedMarriageDetails = new MarriageDetails();
        partialUpdatedMarriageDetails.setId(marriageDetails.getId());

        partialUpdatedMarriageDetails.marriedBefore(UPDATED_MARRIED_BEFORE);

        restMarriageDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarriageDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarriageDetails))
            )
            .andExpect(status().isOk());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeUpdate);
        MarriageDetails testMarriageDetails = marriageDetailsList.get(marriageDetailsList.size() - 1);
        assertThat(testMarriageDetails.getDateOfMarriage()).isEqualTo(DEFAULT_DATE_OF_MARRIAGE);
        assertThat(testMarriageDetails.getSpouseFullName()).isEqualTo(DEFAULT_SPOUSE_FULL_NAME);
        assertThat(testMarriageDetails.getPlaceOfMarriage()).isEqualTo(DEFAULT_PLACE_OF_MARRIAGE);
        assertThat(testMarriageDetails.getSpousePlaceOfBirth()).isEqualTo(DEFAULT_SPOUSE_PLACE_OF_BIRTH);
        assertThat(testMarriageDetails.getMarriageNumber()).isEqualTo(DEFAULT_MARRIAGE_NUMBER);
        assertThat(testMarriageDetails.getMarriedBefore()).isEqualTo(UPDATED_MARRIED_BEFORE);
    }

    @Test
    @Transactional
    void fullUpdateMarriageDetailsWithPatch() throws Exception {
        // Initialize the database
        marriageDetailsRepository.saveAndFlush(marriageDetails);

        int databaseSizeBeforeUpdate = marriageDetailsRepository.findAll().size();

        // Update the marriageDetails using partial update
        MarriageDetails partialUpdatedMarriageDetails = new MarriageDetails();
        partialUpdatedMarriageDetails.setId(marriageDetails.getId());

        partialUpdatedMarriageDetails
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .spouseFullName(UPDATED_SPOUSE_FULL_NAME)
            .placeOfMarriage(UPDATED_PLACE_OF_MARRIAGE)
            .spousePlaceOfBirth(UPDATED_SPOUSE_PLACE_OF_BIRTH)
            .marriageNumber(UPDATED_MARRIAGE_NUMBER)
            .marriedBefore(UPDATED_MARRIED_BEFORE);

        restMarriageDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMarriageDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarriageDetails))
            )
            .andExpect(status().isOk());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeUpdate);
        MarriageDetails testMarriageDetails = marriageDetailsList.get(marriageDetailsList.size() - 1);
        assertThat(testMarriageDetails.getDateOfMarriage()).isEqualTo(UPDATED_DATE_OF_MARRIAGE);
        assertThat(testMarriageDetails.getSpouseFullName()).isEqualTo(UPDATED_SPOUSE_FULL_NAME);
        assertThat(testMarriageDetails.getPlaceOfMarriage()).isEqualTo(UPDATED_PLACE_OF_MARRIAGE);
        assertThat(testMarriageDetails.getSpousePlaceOfBirth()).isEqualTo(UPDATED_SPOUSE_PLACE_OF_BIRTH);
        assertThat(testMarriageDetails.getMarriageNumber()).isEqualTo(UPDATED_MARRIAGE_NUMBER);
        assertThat(testMarriageDetails.getMarriedBefore()).isEqualTo(UPDATED_MARRIED_BEFORE);
    }

    @Test
    @Transactional
    void patchNonExistingMarriageDetails() throws Exception {
        int databaseSizeBeforeUpdate = marriageDetailsRepository.findAll().size();
        marriageDetails.setId(count.incrementAndGet());

        // Create the MarriageDetails
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarriageDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, marriageDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository, times(0)).save(marriageDetails);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMarriageDetails() throws Exception {
        int databaseSizeBeforeUpdate = marriageDetailsRepository.findAll().size();
        marriageDetails.setId(count.incrementAndGet());

        // Create the MarriageDetails
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarriageDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository, times(0)).save(marriageDetails);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMarriageDetails() throws Exception {
        int databaseSizeBeforeUpdate = marriageDetailsRepository.findAll().size();
        marriageDetails.setId(count.incrementAndGet());

        // Create the MarriageDetails
        MarriageDetailsDTO marriageDetailsDTO = marriageDetailsMapper.toDto(marriageDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMarriageDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(marriageDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MarriageDetails in the database
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository, times(0)).save(marriageDetails);
    }

    @Test
    @Transactional
    void deleteMarriageDetails() throws Exception {
        // Initialize the database
        marriageDetailsRepository.saveAndFlush(marriageDetails);

        int databaseSizeBeforeDelete = marriageDetailsRepository.findAll().size();

        // Delete the marriageDetails
        restMarriageDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, marriageDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MarriageDetails> marriageDetailsList = marriageDetailsRepository.findAll();
        assertThat(marriageDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MarriageDetails in Elasticsearch
        verify(mockMarriageDetailsSearchRepository, times(1)).deleteById(marriageDetails.getId());
    }

    @Test
    @Transactional
    void searchMarriageDetails() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        marriageDetailsRepository.saveAndFlush(marriageDetails);
        when(mockMarriageDetailsSearchRepository.search(queryStringQuery("id:" + marriageDetails.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(marriageDetails), PageRequest.of(0, 1), 1));

        // Search the marriageDetails
        restMarriageDetailsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + marriageDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marriageDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfMarriage").value(hasItem(DEFAULT_DATE_OF_MARRIAGE.toString())))
            .andExpect(jsonPath("$.[*].spouseFullName").value(hasItem(DEFAULT_SPOUSE_FULL_NAME)))
            .andExpect(jsonPath("$.[*].placeOfMarriage").value(hasItem(DEFAULT_PLACE_OF_MARRIAGE)))
            .andExpect(jsonPath("$.[*].spousePlaceOfBirth").value(hasItem(DEFAULT_SPOUSE_PLACE_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].marriageNumber").value(hasItem(DEFAULT_MARRIAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].marriedBefore").value(hasItem(DEFAULT_MARRIED_BEFORE.booleanValue())));
    }
}
