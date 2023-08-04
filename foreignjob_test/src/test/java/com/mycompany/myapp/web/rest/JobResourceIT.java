package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Job;
import com.mycompany.myapp.domain.enumeration.Language;
import com.mycompany.myapp.repository.JobRepository;
import com.mycompany.myapp.repository.search.JobSearchRepository;
import com.mycompany.myapp.service.JobService;
import com.mycompany.myapp.service.dto.JobDTO;
import com.mycompany.myapp.service.mapper.JobMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JobResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobResourceIT {

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_INDUSTRY = "AAAAAAAAAA";
    private static final String UPDATED_INDUSTRY = "BBBBBBBBBB";

    private static final Long DEFAULT_VACANCIES = 1L;
    private static final Long UPDATED_VACANCIES = 2L;

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_REQUIREMENT = "AAAAAAAAAA";
    private static final String UPDATED_JOB_REQUIREMENT = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_RESPONSIBILITY = "AAAAAAAAAA";
    private static final String UPDATED_JOB_RESPONSIBILITY = "BBBBBBBBBB";

    private static final String DEFAULT_SKILLS = "AAAAAAAAAA";
    private static final String UPDATED_SKILLS = "BBBBBBBBBB";

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    private static final Long DEFAULT_MIN_SALARY = 1L;
    private static final Long UPDATED_MIN_SALARY = 2L;

    private static final Long DEFAULT_MAX_SALARY = 1L;
    private static final Long UPDATED_MAX_SALARY = 2L;

    private static final Double DEFAULT_WORKING_HOURS = 1D;
    private static final Double UPDATED_WORKING_HOURS = 2D;

    private static final String DEFAULT_BENEFITS = "AAAAAAAAAA";
    private static final String UPDATED_BENEFITS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/jobs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobRepository jobRepository;

    @Mock
    private JobRepository jobRepositoryMock;

    @Autowired
    private JobMapper jobMapper;

    @Mock
    private JobService jobServiceMock;

    @Autowired
    private JobSearchRepository jobSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobMockMvc;

    private Job job;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createEntity(EntityManager em) {
        Job job = new Job()
            .jobTitle(DEFAULT_JOB_TITLE)
            .department(DEFAULT_DEPARTMENT)
            .industry(DEFAULT_INDUSTRY)
            .vacancies(DEFAULT_VACANCIES)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .country(DEFAULT_COUNTRY)
            .jobRequirement(DEFAULT_JOB_REQUIREMENT)
            .jobResponsibility(DEFAULT_JOB_RESPONSIBILITY)
            .skills(DEFAULT_SKILLS)
            .language(DEFAULT_LANGUAGE)
            .minSalary(DEFAULT_MIN_SALARY)
            .maxSalary(DEFAULT_MAX_SALARY)
            .workingHours(DEFAULT_WORKING_HOURS)
            .benefits(DEFAULT_BENEFITS);
        return job;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Job createUpdatedEntity(EntityManager em) {
        Job job = new Job()
            .jobTitle(UPDATED_JOB_TITLE)
            .department(UPDATED_DEPARTMENT)
            .industry(UPDATED_INDUSTRY)
            .vacancies(UPDATED_VACANCIES)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .country(UPDATED_COUNTRY)
            .jobRequirement(UPDATED_JOB_REQUIREMENT)
            .jobResponsibility(UPDATED_JOB_RESPONSIBILITY)
            .skills(UPDATED_SKILLS)
            .language(UPDATED_LANGUAGE)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .workingHours(UPDATED_WORKING_HOURS)
            .benefits(UPDATED_BENEFITS);
        return job;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        jobSearchRepository.deleteAll();
        assertThat(jobSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        job = createEntity(em);
    }

    @Test
    @Transactional
    void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());
        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);
        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testJob.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testJob.getIndustry()).isEqualTo(DEFAULT_INDUSTRY);
        assertThat(testJob.getVacancies()).isEqualTo(DEFAULT_VACANCIES);
        assertThat(testJob.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testJob.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testJob.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testJob.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testJob.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testJob.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testJob.getJobRequirement()).isEqualTo(DEFAULT_JOB_REQUIREMENT);
        assertThat(testJob.getJobResponsibility()).isEqualTo(DEFAULT_JOB_RESPONSIBILITY);
        assertThat(testJob.getSkills()).isEqualTo(DEFAULT_SKILLS);
        assertThat(testJob.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testJob.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testJob.getMaxSalary()).isEqualTo(DEFAULT_MAX_SALARY);
        assertThat(testJob.getWorkingHours()).isEqualTo(DEFAULT_WORKING_HOURS);
        assertThat(testJob.getBenefits()).isEqualTo(DEFAULT_BENEFITS);
    }

    @Test
    @Transactional
    void createJobWithExistingId() throws Exception {
        // Create the Job with an existing ID
        job.setId(1L);
        JobDTO jobDTO = jobMapper.toDto(job);

        int databaseSizeBeforeCreate = jobRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobList
        restJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].industry").value(hasItem(DEFAULT_INDUSTRY)))
            .andExpect(jsonPath("$.[*].vacancies").value(hasItem(DEFAULT_VACANCIES.intValue())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].jobRequirement").value(hasItem(DEFAULT_JOB_REQUIREMENT)))
            .andExpect(jsonPath("$.[*].jobResponsibility").value(hasItem(DEFAULT_JOB_RESPONSIBILITY)))
            .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(DEFAULT_MAX_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].workingHours").value(hasItem(DEFAULT_WORKING_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].benefits").value(hasItem(DEFAULT_BENEFITS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsEnabled() throws Exception {
        when(jobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(jobServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJobsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(jobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(jobRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc
            .perform(get(ENTITY_API_URL_ID, job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.industry").value(DEFAULT_INDUSTRY))
            .andExpect(jsonPath("$.vacancies").value(DEFAULT_VACANCIES.intValue()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.jobRequirement").value(DEFAULT_JOB_REQUIREMENT))
            .andExpect(jsonPath("$.jobResponsibility").value(DEFAULT_JOB_RESPONSIBILITY))
            .andExpect(jsonPath("$.skills").value(DEFAULT_SKILLS))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.minSalary").value(DEFAULT_MIN_SALARY.intValue()))
            .andExpect(jsonPath("$.maxSalary").value(DEFAULT_MAX_SALARY.intValue()))
            .andExpect(jsonPath("$.workingHours").value(DEFAULT_WORKING_HOURS.doubleValue()))
            .andExpect(jsonPath("$.benefits").value(DEFAULT_BENEFITS));
    }

    @Test
    @Transactional
    void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        jobSearchRepository.save(job);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());

        // Update the job
        Job updatedJob = jobRepository.findById(job.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJob are not directly saved in db
        em.detach(updatedJob);
        updatedJob
            .jobTitle(UPDATED_JOB_TITLE)
            .department(UPDATED_DEPARTMENT)
            .industry(UPDATED_INDUSTRY)
            .vacancies(UPDATED_VACANCIES)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .country(UPDATED_COUNTRY)
            .jobRequirement(UPDATED_JOB_REQUIREMENT)
            .jobResponsibility(UPDATED_JOB_RESPONSIBILITY)
            .skills(UPDATED_SKILLS)
            .language(UPDATED_LANGUAGE)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .workingHours(UPDATED_WORKING_HOURS)
            .benefits(UPDATED_BENEFITS);
        JobDTO jobDTO = jobMapper.toDto(updatedJob);

        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testJob.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testJob.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(testJob.getVacancies()).isEqualTo(UPDATED_VACANCIES);
        assertThat(testJob.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testJob.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testJob.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testJob.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testJob.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testJob.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testJob.getJobRequirement()).isEqualTo(UPDATED_JOB_REQUIREMENT);
        assertThat(testJob.getJobResponsibility()).isEqualTo(UPDATED_JOB_RESPONSIBILITY);
        assertThat(testJob.getSkills()).isEqualTo(UPDATED_SKILLS);
        assertThat(testJob.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testJob.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testJob.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
        assertThat(testJob.getWorkingHours()).isEqualTo(UPDATED_WORKING_HOURS);
        assertThat(testJob.getBenefits()).isEqualTo(UPDATED_BENEFITS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Job> jobSearchList = IterableUtils.toList(jobSearchRepository.findAll());
                Job testJobSearch = jobSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testJobSearch.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
                assertThat(testJobSearch.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
                assertThat(testJobSearch.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
                assertThat(testJobSearch.getVacancies()).isEqualTo(UPDATED_VACANCIES);
                assertThat(testJobSearch.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
                assertThat(testJobSearch.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
                assertThat(testJobSearch.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
                assertThat(testJobSearch.getCity()).isEqualTo(UPDATED_CITY);
                assertThat(testJobSearch.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
                assertThat(testJobSearch.getCountry()).isEqualTo(UPDATED_COUNTRY);
                assertThat(testJobSearch.getJobRequirement()).isEqualTo(UPDATED_JOB_REQUIREMENT);
                assertThat(testJobSearch.getJobResponsibility()).isEqualTo(UPDATED_JOB_RESPONSIBILITY);
                assertThat(testJobSearch.getSkills()).isEqualTo(UPDATED_SKILLS);
                assertThat(testJobSearch.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
                assertThat(testJobSearch.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
                assertThat(testJobSearch.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
                assertThat(testJobSearch.getWorkingHours()).isEqualTo(UPDATED_WORKING_HOURS);
                assertThat(testJobSearch.getBenefits()).isEqualTo(UPDATED_BENEFITS);
            });
    }

    @Test
    @Transactional
    void putNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());
        job.setId(count.incrementAndGet());

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());
        job.setId(count.incrementAndGet());

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());
        job.setId(count.incrementAndGet());

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateJobWithPatch() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job using partial update
        Job partialUpdatedJob = new Job();
        partialUpdatedJob.setId(job.getId());

        partialUpdatedJob
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .jobResponsibility(UPDATED_JOB_RESPONSIBILITY)
            .maxSalary(UPDATED_MAX_SALARY)
            .benefits(UPDATED_BENEFITS);

        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJob))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testJob.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testJob.getIndustry()).isEqualTo(DEFAULT_INDUSTRY);
        assertThat(testJob.getVacancies()).isEqualTo(DEFAULT_VACANCIES);
        assertThat(testJob.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testJob.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testJob.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testJob.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testJob.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testJob.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testJob.getJobRequirement()).isEqualTo(DEFAULT_JOB_REQUIREMENT);
        assertThat(testJob.getJobResponsibility()).isEqualTo(UPDATED_JOB_RESPONSIBILITY);
        assertThat(testJob.getSkills()).isEqualTo(DEFAULT_SKILLS);
        assertThat(testJob.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testJob.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testJob.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
        assertThat(testJob.getWorkingHours()).isEqualTo(DEFAULT_WORKING_HOURS);
        assertThat(testJob.getBenefits()).isEqualTo(UPDATED_BENEFITS);
    }

    @Test
    @Transactional
    void fullUpdateJobWithPatch() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job using partial update
        Job partialUpdatedJob = new Job();
        partialUpdatedJob.setId(job.getId());

        partialUpdatedJob
            .jobTitle(UPDATED_JOB_TITLE)
            .department(UPDATED_DEPARTMENT)
            .industry(UPDATED_INDUSTRY)
            .vacancies(UPDATED_VACANCIES)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .country(UPDATED_COUNTRY)
            .jobRequirement(UPDATED_JOB_REQUIREMENT)
            .jobResponsibility(UPDATED_JOB_RESPONSIBILITY)
            .skills(UPDATED_SKILLS)
            .language(UPDATED_LANGUAGE)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalary(UPDATED_MAX_SALARY)
            .workingHours(UPDATED_WORKING_HOURS)
            .benefits(UPDATED_BENEFITS);

        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJob))
            )
            .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobList.get(jobList.size() - 1);
        assertThat(testJob.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testJob.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testJob.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(testJob.getVacancies()).isEqualTo(UPDATED_VACANCIES);
        assertThat(testJob.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testJob.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testJob.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testJob.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testJob.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testJob.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testJob.getJobRequirement()).isEqualTo(UPDATED_JOB_REQUIREMENT);
        assertThat(testJob.getJobResponsibility()).isEqualTo(UPDATED_JOB_RESPONSIBILITY);
        assertThat(testJob.getSkills()).isEqualTo(UPDATED_SKILLS);
        assertThat(testJob.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testJob.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testJob.getMaxSalary()).isEqualTo(UPDATED_MAX_SALARY);
        assertThat(testJob.getWorkingHours()).isEqualTo(UPDATED_WORKING_HOURS);
        assertThat(testJob.getBenefits()).isEqualTo(UPDATED_BENEFITS);
    }

    @Test
    @Transactional
    void patchNonExistingJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());
        job.setId(count.incrementAndGet());

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());
        job.setId(count.incrementAndGet());

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJob() throws Exception {
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());
        job.setId(count.incrementAndGet());

        // Create the Job
        JobDTO jobDTO = jobMapper.toDto(job);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Job in the database
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        jobRepository.save(job);
        jobSearchRepository.save(job);

        int databaseSizeBeforeDelete = jobRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(jobSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the job
        restJobMockMvc.perform(delete(ENTITY_API_URL_ID, job.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Job> jobList = jobRepository.findAll();
        assertThat(jobList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(jobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchJob() throws Exception {
        // Initialize the database
        job = jobRepository.saveAndFlush(job);
        jobSearchRepository.save(job);

        // Search the job
        restJobMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].industry").value(hasItem(DEFAULT_INDUSTRY)))
            .andExpect(jsonPath("$.[*].vacancies").value(hasItem(DEFAULT_VACANCIES.intValue())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].jobRequirement").value(hasItem(DEFAULT_JOB_REQUIREMENT)))
            .andExpect(jsonPath("$.[*].jobResponsibility").value(hasItem(DEFAULT_JOB_RESPONSIBILITY)))
            .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].maxSalary").value(hasItem(DEFAULT_MAX_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].workingHours").value(hasItem(DEFAULT_WORKING_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].benefits").value(hasItem(DEFAULT_BENEFITS)));
    }
}
