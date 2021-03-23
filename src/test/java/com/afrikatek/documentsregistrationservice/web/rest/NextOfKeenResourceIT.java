package com.afrikatek.documentsregistrationservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsregistrationservice.IntegrationTest;
import com.afrikatek.documentsregistrationservice.domain.Applicant;
import com.afrikatek.documentsregistrationservice.domain.NextOfKeen;
import com.afrikatek.documentsregistrationservice.repository.NextOfKeenRepository;
import com.afrikatek.documentsregistrationservice.repository.search.NextOfKeenSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.NextOfKeenDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.NextOfKeenMapper;
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
 * Integration tests for the {@link NextOfKeenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NextOfKeenResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CELLPHONE = "AAAAAAAAAA";
    private static final String UPDATED_CELLPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-of-keens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/next-of-keens";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NextOfKeenRepository nextOfKeenRepository;

    @Autowired
    private NextOfKeenMapper nextOfKeenMapper;

    /**
     * This repository is mocked in the com.afrikatek.documentsregistrationservice.repository.search test package.
     *
     * @see com.afrikatek.documentsregistrationservice.repository.search.NextOfKeenSearchRepositoryMockConfiguration
     */
    @Autowired
    private NextOfKeenSearchRepository mockNextOfKeenSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOfKeenMockMvc;

    private NextOfKeen nextOfKeen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOfKeen createEntity(EntityManager em) {
        NextOfKeen nextOfKeen = new NextOfKeen().fullName(DEFAULT_FULL_NAME).address(DEFAULT_ADDRESS).cellphone(DEFAULT_CELLPHONE);
        // Add required entity
        Applicant applicant;
        if (TestUtil.findAll(em, Applicant.class).isEmpty()) {
            applicant = ApplicantResourceIT.createEntity(em);
            em.persist(applicant);
            em.flush();
        } else {
            applicant = TestUtil.findAll(em, Applicant.class).get(0);
        }
        nextOfKeen.setApplicant(applicant);
        return nextOfKeen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOfKeen createUpdatedEntity(EntityManager em) {
        NextOfKeen nextOfKeen = new NextOfKeen().fullName(UPDATED_FULL_NAME).address(UPDATED_ADDRESS).cellphone(UPDATED_CELLPHONE);
        // Add required entity
        Applicant applicant;
        if (TestUtil.findAll(em, Applicant.class).isEmpty()) {
            applicant = ApplicantResourceIT.createUpdatedEntity(em);
            em.persist(applicant);
            em.flush();
        } else {
            applicant = TestUtil.findAll(em, Applicant.class).get(0);
        }
        nextOfKeen.setApplicant(applicant);
        return nextOfKeen;
    }

    @BeforeEach
    public void initTest() {
        nextOfKeen = createEntity(em);
    }

    @Test
    @Transactional
    void createNextOfKeen() throws Exception {
        int databaseSizeBeforeCreate = nextOfKeenRepository.findAll().size();
        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);
        restNextOfKeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isCreated());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeCreate + 1);
        NextOfKeen testNextOfKeen = nextOfKeenList.get(nextOfKeenList.size() - 1);
        assertThat(testNextOfKeen.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testNextOfKeen.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testNextOfKeen.getCellphone()).isEqualTo(DEFAULT_CELLPHONE);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository, times(1)).save(testNextOfKeen);
    }

    @Test
    @Transactional
    void createNextOfKeenWithExistingId() throws Exception {
        // Create the NextOfKeen with an existing ID
        nextOfKeen.setId(1L);
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        int databaseSizeBeforeCreate = nextOfKeenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOfKeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeCreate);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository, times(0)).save(nextOfKeen);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nextOfKeenRepository.findAll().size();
        // set the field null
        nextOfKeen.setFullName(null);

        // Create the NextOfKeen, which fails.
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        restNextOfKeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isBadRequest());

        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = nextOfKeenRepository.findAll().size();
        // set the field null
        nextOfKeen.setAddress(null);

        // Create the NextOfKeen, which fails.
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        restNextOfKeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isBadRequest());

        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCellphoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = nextOfKeenRepository.findAll().size();
        // set the field null
        nextOfKeen.setCellphone(null);

        // Create the NextOfKeen, which fails.
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        restNextOfKeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isBadRequest());

        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOfKeens() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        // Get all the nextOfKeenList
        restNextOfKeenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOfKeen.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].cellphone").value(hasItem(DEFAULT_CELLPHONE)));
    }

    @Test
    @Transactional
    void getNextOfKeen() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        // Get the nextOfKeen
        restNextOfKeenMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOfKeen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOfKeen.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.cellphone").value(DEFAULT_CELLPHONE));
    }

    @Test
    @Transactional
    void getNonExistingNextOfKeen() throws Exception {
        // Get the nextOfKeen
        restNextOfKeenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNextOfKeen() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();

        // Update the nextOfKeen
        NextOfKeen updatedNextOfKeen = nextOfKeenRepository.findById(nextOfKeen.getId()).get();
        // Disconnect from session so that the updates on updatedNextOfKeen are not directly saved in db
        em.detach(updatedNextOfKeen);
        updatedNextOfKeen.fullName(UPDATED_FULL_NAME).address(UPDATED_ADDRESS).cellphone(UPDATED_CELLPHONE);
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(updatedNextOfKeen);

        restNextOfKeenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOfKeenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
        NextOfKeen testNextOfKeen = nextOfKeenList.get(nextOfKeenList.size() - 1);
        assertThat(testNextOfKeen.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testNextOfKeen.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testNextOfKeen.getCellphone()).isEqualTo(UPDATED_CELLPHONE);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository).save(testNextOfKeen);
    }

    @Test
    @Transactional
    void putNonExistingNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOfKeenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository, times(0)).save(nextOfKeen);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository, times(0)).save(nextOfKeen);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository, times(0)).save(nextOfKeen);
    }

    @Test
    @Transactional
    void partialUpdateNextOfKeenWithPatch() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();

        // Update the nextOfKeen using partial update
        NextOfKeen partialUpdatedNextOfKeen = new NextOfKeen();
        partialUpdatedNextOfKeen.setId(nextOfKeen.getId());

        partialUpdatedNextOfKeen.address(UPDATED_ADDRESS);

        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOfKeen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNextOfKeen))
            )
            .andExpect(status().isOk());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
        NextOfKeen testNextOfKeen = nextOfKeenList.get(nextOfKeenList.size() - 1);
        assertThat(testNextOfKeen.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testNextOfKeen.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testNextOfKeen.getCellphone()).isEqualTo(DEFAULT_CELLPHONE);
    }

    @Test
    @Transactional
    void fullUpdateNextOfKeenWithPatch() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();

        // Update the nextOfKeen using partial update
        NextOfKeen partialUpdatedNextOfKeen = new NextOfKeen();
        partialUpdatedNextOfKeen.setId(nextOfKeen.getId());

        partialUpdatedNextOfKeen.fullName(UPDATED_FULL_NAME).address(UPDATED_ADDRESS).cellphone(UPDATED_CELLPHONE);

        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOfKeen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNextOfKeen))
            )
            .andExpect(status().isOk());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
        NextOfKeen testNextOfKeen = nextOfKeenList.get(nextOfKeenList.size() - 1);
        assertThat(testNextOfKeen.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testNextOfKeen.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testNextOfKeen.getCellphone()).isEqualTo(UPDATED_CELLPHONE);
    }

    @Test
    @Transactional
    void patchNonExistingNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOfKeenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository, times(0)).save(nextOfKeen);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository, times(0)).save(nextOfKeen);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository, times(0)).save(nextOfKeen);
    }

    @Test
    @Transactional
    void deleteNextOfKeen() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        int databaseSizeBeforeDelete = nextOfKeenRepository.findAll().size();

        // Delete the nextOfKeen
        restNextOfKeenMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOfKeen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NextOfKeen in Elasticsearch
        verify(mockNextOfKeenSearchRepository, times(1)).deleteById(nextOfKeen.getId());
    }

    @Test
    @Transactional
    void searchNextOfKeen() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);
        when(mockNextOfKeenSearchRepository.search(queryStringQuery("id:" + nextOfKeen.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(nextOfKeen), PageRequest.of(0, 1), 1));

        // Search the nextOfKeen
        restNextOfKeenMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + nextOfKeen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOfKeen.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].cellphone").value(hasItem(DEFAULT_CELLPHONE)));
    }
}
