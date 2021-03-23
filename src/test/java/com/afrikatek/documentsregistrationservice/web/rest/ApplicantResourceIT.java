package com.afrikatek.documentsregistrationservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsregistrationservice.IntegrationTest;
import com.afrikatek.documentsregistrationservice.domain.Applicant;
import com.afrikatek.documentsregistrationservice.domain.User;
import com.afrikatek.documentsregistrationservice.domain.enumeration.EyeColor;
import com.afrikatek.documentsregistrationservice.domain.enumeration.Gender;
import com.afrikatek.documentsregistrationservice.domain.enumeration.HairColor;
import com.afrikatek.documentsregistrationservice.domain.enumeration.MaritalStatus;
import com.afrikatek.documentsregistrationservice.repository.ApplicantRepository;
import com.afrikatek.documentsregistrationservice.repository.search.ApplicantSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.ApplicantMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ApplicantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplicantResourceIT {

    private static final String DEFAULT_FIRST_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAMES = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INITIALS = "AAAAAAAAAA";
    private static final String UPDATED_INITIALS = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_EMAIL = "vU@lws.S";
    private static final String UPDATED_EMAIL = "~@M):]V.4OI";

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.MARRIED;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.SINGLE;

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTH_ENTRY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_ENTRY_NUMBER = "BBBBBBBBBB";

    private static final EyeColor DEFAULT_EYE_COLOR = EyeColor.BROWN;
    private static final EyeColor UPDATED_EYE_COLOR = EyeColor.BLACK;

    private static final HairColor DEFAULT_HAIR_COLOR = HairColor.BLACK;
    private static final HairColor UPDATED_HAIR_COLOR = HairColor.BROWN;

    private static final String DEFAULT_VISIBLE_MARKS = "AAAAAAAAAA";
    private static final String UPDATED_VISIBLE_MARKS = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/applicants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/applicants";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ApplicantMapper applicantMapper;

    /**
     * This repository is mocked in the com.afrikatek.documentsregistrationservice.repository.search test package.
     *
     * @see com.afrikatek.documentsregistrationservice.repository.search.ApplicantSearchRepositoryMockConfiguration
     */
    @Autowired
    private ApplicantSearchRepository mockApplicantSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicantMockMvc;

    private Applicant applicant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applicant createEntity(EntityManager em) {
        Applicant applicant = new Applicant()
            .firstNames(DEFAULT_FIRST_NAMES)
            .lastName(DEFAULT_LAST_NAME)
            .initials(DEFAULT_INITIALS)
            .gender(DEFAULT_GENDER)
            .email(DEFAULT_EMAIL)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .idNumber(DEFAULT_ID_NUMBER)
            .birthEntryNumber(DEFAULT_BIRTH_ENTRY_NUMBER)
            .eyeColor(DEFAULT_EYE_COLOR)
            .hairColor(DEFAULT_HAIR_COLOR)
            .visibleMarks(DEFAULT_VISIBLE_MARKS)
            .profession(DEFAULT_PROFESSION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        applicant.setUser(user);
        return applicant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applicant createUpdatedEntity(EntityManager em) {
        Applicant applicant = new Applicant()
            .firstNames(UPDATED_FIRST_NAMES)
            .lastName(UPDATED_LAST_NAME)
            .initials(UPDATED_INITIALS)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .birthEntryNumber(UPDATED_BIRTH_ENTRY_NUMBER)
            .eyeColor(UPDATED_EYE_COLOR)
            .hairColor(UPDATED_HAIR_COLOR)
            .visibleMarks(UPDATED_VISIBLE_MARKS)
            .profession(UPDATED_PROFESSION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        applicant.setUser(user);
        return applicant;
    }

    @BeforeEach
    public void initTest() {
        applicant = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicant() throws Exception {
        int databaseSizeBeforeCreate = applicantRepository.findAll().size();
        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);
        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isCreated());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeCreate + 1);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirstNames()).isEqualTo(DEFAULT_FIRST_NAMES);
        assertThat(testApplicant.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testApplicant.getInitials()).isEqualTo(DEFAULT_INITIALS);
        assertThat(testApplicant.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testApplicant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testApplicant.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testApplicant.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testApplicant.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testApplicant.getBirthEntryNumber()).isEqualTo(DEFAULT_BIRTH_ENTRY_NUMBER);
        assertThat(testApplicant.getEyeColor()).isEqualTo(DEFAULT_EYE_COLOR);
        assertThat(testApplicant.getHairColor()).isEqualTo(DEFAULT_HAIR_COLOR);
        assertThat(testApplicant.getVisibleMarks()).isEqualTo(DEFAULT_VISIBLE_MARKS);
        assertThat(testApplicant.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testApplicant.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testApplicant.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository, times(1)).save(testApplicant);
    }

    @Test
    @Transactional
    void createApplicantWithExistingId() throws Exception {
        // Create the Applicant with an existing ID
        applicant.setId(1L);
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        int databaseSizeBeforeCreate = applicantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeCreate);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository, times(0)).save(applicant);
    }

    @Test
    @Transactional
    void checkFirstNamesIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setFirstNames(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setLastName(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInitialsIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setInitials(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setEmail(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setDateOfBirth(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setIdNumber(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthEntryNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setBirthEntryNumber(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEyeColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setEyeColor(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHairColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setHairColor(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVisibleMarksIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setVisibleMarks(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfessionIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setProfession(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicants() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList
        restApplicantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicant.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstNames").value(hasItem(DEFAULT_FIRST_NAMES)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].initials").value(hasItem(DEFAULT_INITIALS)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].birthEntryNumber").value(hasItem(DEFAULT_BIRTH_ENTRY_NUMBER)))
            .andExpect(jsonPath("$.[*].eyeColor").value(hasItem(DEFAULT_EYE_COLOR.toString())))
            .andExpect(jsonPath("$.[*].hairColor").value(hasItem(DEFAULT_HAIR_COLOR.toString())))
            .andExpect(jsonPath("$.[*].visibleMarks").value(hasItem(DEFAULT_VISIBLE_MARKS)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get the applicant
        restApplicantMockMvc
            .perform(get(ENTITY_API_URL_ID, applicant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicant.getId().intValue()))
            .andExpect(jsonPath("$.firstNames").value(DEFAULT_FIRST_NAMES))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.initials").value(DEFAULT_INITIALS))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.birthEntryNumber").value(DEFAULT_BIRTH_ENTRY_NUMBER))
            .andExpect(jsonPath("$.eyeColor").value(DEFAULT_EYE_COLOR.toString()))
            .andExpect(jsonPath("$.hairColor").value(DEFAULT_HAIR_COLOR.toString()))
            .andExpect(jsonPath("$.visibleMarks").value(DEFAULT_VISIBLE_MARKS))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingApplicant() throws Exception {
        // Get the applicant
        restApplicantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant
        Applicant updatedApplicant = applicantRepository.findById(applicant.getId()).get();
        // Disconnect from session so that the updates on updatedApplicant are not directly saved in db
        em.detach(updatedApplicant);
        updatedApplicant
            .firstNames(UPDATED_FIRST_NAMES)
            .lastName(UPDATED_LAST_NAME)
            .initials(UPDATED_INITIALS)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .birthEntryNumber(UPDATED_BIRTH_ENTRY_NUMBER)
            .eyeColor(UPDATED_EYE_COLOR)
            .hairColor(UPDATED_HAIR_COLOR)
            .visibleMarks(UPDATED_VISIBLE_MARKS)
            .profession(UPDATED_PROFESSION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ApplicantDTO applicantDTO = applicantMapper.toDto(updatedApplicant);

        restApplicantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirstNames()).isEqualTo(UPDATED_FIRST_NAMES);
        assertThat(testApplicant.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicant.getInitials()).isEqualTo(UPDATED_INITIALS);
        assertThat(testApplicant.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testApplicant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApplicant.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testApplicant.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testApplicant.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testApplicant.getBirthEntryNumber()).isEqualTo(UPDATED_BIRTH_ENTRY_NUMBER);
        assertThat(testApplicant.getEyeColor()).isEqualTo(UPDATED_EYE_COLOR);
        assertThat(testApplicant.getHairColor()).isEqualTo(UPDATED_HAIR_COLOR);
        assertThat(testApplicant.getVisibleMarks()).isEqualTo(UPDATED_VISIBLE_MARKS);
        assertThat(testApplicant.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testApplicant.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testApplicant.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository).save(testApplicant);
    }

    @Test
    @Transactional
    void putNonExistingApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository, times(0)).save(applicant);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository, times(0)).save(applicant);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository, times(0)).save(applicant);
    }

    @Test
    @Transactional
    void partialUpdateApplicantWithPatch() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant using partial update
        Applicant partialUpdatedApplicant = new Applicant();
        partialUpdatedApplicant.setId(applicant.getId());

        partialUpdatedApplicant
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .eyeColor(UPDATED_EYE_COLOR)
            .visibleMarks(UPDATED_VISIBLE_MARKS);

        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicant))
            )
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirstNames()).isEqualTo(DEFAULT_FIRST_NAMES);
        assertThat(testApplicant.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicant.getInitials()).isEqualTo(DEFAULT_INITIALS);
        assertThat(testApplicant.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testApplicant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApplicant.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testApplicant.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testApplicant.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testApplicant.getBirthEntryNumber()).isEqualTo(DEFAULT_BIRTH_ENTRY_NUMBER);
        assertThat(testApplicant.getEyeColor()).isEqualTo(UPDATED_EYE_COLOR);
        assertThat(testApplicant.getHairColor()).isEqualTo(DEFAULT_HAIR_COLOR);
        assertThat(testApplicant.getVisibleMarks()).isEqualTo(UPDATED_VISIBLE_MARKS);
        assertThat(testApplicant.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testApplicant.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testApplicant.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateApplicantWithPatch() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant using partial update
        Applicant partialUpdatedApplicant = new Applicant();
        partialUpdatedApplicant.setId(applicant.getId());

        partialUpdatedApplicant
            .firstNames(UPDATED_FIRST_NAMES)
            .lastName(UPDATED_LAST_NAME)
            .initials(UPDATED_INITIALS)
            .gender(UPDATED_GENDER)
            .email(UPDATED_EMAIL)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .birthEntryNumber(UPDATED_BIRTH_ENTRY_NUMBER)
            .eyeColor(UPDATED_EYE_COLOR)
            .hairColor(UPDATED_HAIR_COLOR)
            .visibleMarks(UPDATED_VISIBLE_MARKS)
            .profession(UPDATED_PROFESSION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicant))
            )
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getFirstNames()).isEqualTo(UPDATED_FIRST_NAMES);
        assertThat(testApplicant.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testApplicant.getInitials()).isEqualTo(UPDATED_INITIALS);
        assertThat(testApplicant.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testApplicant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testApplicant.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testApplicant.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testApplicant.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testApplicant.getBirthEntryNumber()).isEqualTo(UPDATED_BIRTH_ENTRY_NUMBER);
        assertThat(testApplicant.getEyeColor()).isEqualTo(UPDATED_EYE_COLOR);
        assertThat(testApplicant.getHairColor()).isEqualTo(UPDATED_HAIR_COLOR);
        assertThat(testApplicant.getVisibleMarks()).isEqualTo(UPDATED_VISIBLE_MARKS);
        assertThat(testApplicant.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testApplicant.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testApplicant.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository, times(0)).save(applicant);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository, times(0)).save(applicant);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository, times(0)).save(applicant);
    }

    @Test
    @Transactional
    void deleteApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeDelete = applicantRepository.findAll().size();

        // Delete the applicant
        restApplicantMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Applicant in Elasticsearch
        verify(mockApplicantSearchRepository, times(1)).deleteById(applicant.getId());
    }

    @Test
    @Transactional
    void searchApplicant() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);
        when(mockApplicantSearchRepository.search(queryStringQuery("id:" + applicant.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(applicant), PageRequest.of(0, 1), 1));

        // Search the applicant
        restApplicantMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + applicant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicant.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstNames").value(hasItem(DEFAULT_FIRST_NAMES)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].initials").value(hasItem(DEFAULT_INITIALS)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].birthEntryNumber").value(hasItem(DEFAULT_BIRTH_ENTRY_NUMBER)))
            .andExpect(jsonPath("$.[*].eyeColor").value(hasItem(DEFAULT_EYE_COLOR.toString())))
            .andExpect(jsonPath("$.[*].hairColor").value(hasItem(DEFAULT_HAIR_COLOR.toString())))
            .andExpect(jsonPath("$.[*].visibleMarks").value(hasItem(DEFAULT_VISIBLE_MARKS)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
}
