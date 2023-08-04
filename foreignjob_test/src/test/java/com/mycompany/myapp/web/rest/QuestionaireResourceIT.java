package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Questionaire;
import com.mycompany.myapp.repository.QuestionaireRepository;
import com.mycompany.myapp.repository.search.QuestionaireSearchRepository;
import com.mycompany.myapp.service.dto.QuestionaireDTO;
import com.mycompany.myapp.service.mapper.QuestionaireMapper;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestionaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionaireResourceIT {

    private static final String DEFAULT_QUEST_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_QUEST_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_CORRECT_OPTION = "AAAAAAAAAA";
    private static final String UPDATED_CORRECT_OPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questionaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/questionaires";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionaireRepository questionaireRepository;

    @Autowired
    private QuestionaireMapper questionaireMapper;

    @Autowired
    private QuestionaireSearchRepository questionaireSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionaireMockMvc;

    private Questionaire questionaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionaire createEntity(EntityManager em) {
        Questionaire questionaire = new Questionaire()
            .questTitle(DEFAULT_QUEST_TITLE)
            .options(DEFAULT_OPTIONS)
            .correctOption(DEFAULT_CORRECT_OPTION);
        return questionaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionaire createUpdatedEntity(EntityManager em) {
        Questionaire questionaire = new Questionaire()
            .questTitle(UPDATED_QUEST_TITLE)
            .options(UPDATED_OPTIONS)
            .correctOption(UPDATED_CORRECT_OPTION);
        return questionaire;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        questionaireSearchRepository.deleteAll();
        assertThat(questionaireSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        questionaire = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionaire() throws Exception {
        int databaseSizeBeforeCreate = questionaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        // Create the Questionaire
        QuestionaireDTO questionaireDTO = questionaireMapper.toDto(questionaire);
        restQuestionaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Questionaire testQuestionaire = questionaireList.get(questionaireList.size() - 1);
        assertThat(testQuestionaire.getQuestTitle()).isEqualTo(DEFAULT_QUEST_TITLE);
        assertThat(testQuestionaire.getOptions()).isEqualTo(DEFAULT_OPTIONS);
        assertThat(testQuestionaire.getCorrectOption()).isEqualTo(DEFAULT_CORRECT_OPTION);
    }

    @Test
    @Transactional
    void createQuestionaireWithExistingId() throws Exception {
        // Create the Questionaire with an existing ID
        questionaire.setId(1L);
        QuestionaireDTO questionaireDTO = questionaireMapper.toDto(questionaire);

        int databaseSizeBeforeCreate = questionaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionaireMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllQuestionaires() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        // Get all the questionaireList
        restQuestionaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].questTitle").value(hasItem(DEFAULT_QUEST_TITLE)))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)))
            .andExpect(jsonPath("$.[*].correctOption").value(hasItem(DEFAULT_CORRECT_OPTION)));
    }

    @Test
    @Transactional
    void getQuestionaire() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        // Get the questionaire
        restQuestionaireMockMvc
            .perform(get(ENTITY_API_URL_ID, questionaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionaire.getId().intValue()))
            .andExpect(jsonPath("$.questTitle").value(DEFAULT_QUEST_TITLE))
            .andExpect(jsonPath("$.options").value(DEFAULT_OPTIONS))
            .andExpect(jsonPath("$.correctOption").value(DEFAULT_CORRECT_OPTION));
    }

    @Test
    @Transactional
    void getNonExistingQuestionaire() throws Exception {
        // Get the questionaire
        restQuestionaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestionaire() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        questionaireSearchRepository.save(questionaire);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());

        // Update the questionaire
        Questionaire updatedQuestionaire = questionaireRepository.findById(questionaire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestionaire are not directly saved in db
        em.detach(updatedQuestionaire);
        updatedQuestionaire.questTitle(UPDATED_QUEST_TITLE).options(UPDATED_OPTIONS).correctOption(UPDATED_CORRECT_OPTION);
        QuestionaireDTO questionaireDTO = questionaireMapper.toDto(updatedQuestionaire);

        restQuestionaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        Questionaire testQuestionaire = questionaireList.get(questionaireList.size() - 1);
        assertThat(testQuestionaire.getQuestTitle()).isEqualTo(UPDATED_QUEST_TITLE);
        assertThat(testQuestionaire.getOptions()).isEqualTo(UPDATED_OPTIONS);
        assertThat(testQuestionaire.getCorrectOption()).isEqualTo(UPDATED_CORRECT_OPTION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Questionaire> questionaireSearchList = IterableUtils.toList(questionaireSearchRepository.findAll());
                Questionaire testQuestionaireSearch = questionaireSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testQuestionaireSearch.getQuestTitle()).isEqualTo(UPDATED_QUEST_TITLE);
                assertThat(testQuestionaireSearch.getOptions()).isEqualTo(UPDATED_OPTIONS);
                assertThat(testQuestionaireSearch.getCorrectOption()).isEqualTo(UPDATED_CORRECT_OPTION);
            });
    }

    @Test
    @Transactional
    void putNonExistingQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        questionaire.setId(count.incrementAndGet());

        // Create the Questionaire
        QuestionaireDTO questionaireDTO = questionaireMapper.toDto(questionaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        questionaire.setId(count.incrementAndGet());

        // Create the Questionaire
        QuestionaireDTO questionaireDTO = questionaireMapper.toDto(questionaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        questionaire.setId(count.incrementAndGet());

        // Create the Questionaire
        QuestionaireDTO questionaireDTO = questionaireMapper.toDto(questionaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateQuestionaireWithPatch() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();

        // Update the questionaire using partial update
        Questionaire partialUpdatedQuestionaire = new Questionaire();
        partialUpdatedQuestionaire.setId(questionaire.getId());

        partialUpdatedQuestionaire.options(UPDATED_OPTIONS).correctOption(UPDATED_CORRECT_OPTION);

        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        Questionaire testQuestionaire = questionaireList.get(questionaireList.size() - 1);
        assertThat(testQuestionaire.getQuestTitle()).isEqualTo(DEFAULT_QUEST_TITLE);
        assertThat(testQuestionaire.getOptions()).isEqualTo(UPDATED_OPTIONS);
        assertThat(testQuestionaire.getCorrectOption()).isEqualTo(UPDATED_CORRECT_OPTION);
    }

    @Test
    @Transactional
    void fullUpdateQuestionaireWithPatch() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);

        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();

        // Update the questionaire using partial update
        Questionaire partialUpdatedQuestionaire = new Questionaire();
        partialUpdatedQuestionaire.setId(questionaire.getId());

        partialUpdatedQuestionaire.questTitle(UPDATED_QUEST_TITLE).options(UPDATED_OPTIONS).correctOption(UPDATED_CORRECT_OPTION);

        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        Questionaire testQuestionaire = questionaireList.get(questionaireList.size() - 1);
        assertThat(testQuestionaire.getQuestTitle()).isEqualTo(UPDATED_QUEST_TITLE);
        assertThat(testQuestionaire.getOptions()).isEqualTo(UPDATED_OPTIONS);
        assertThat(testQuestionaire.getCorrectOption()).isEqualTo(UPDATED_CORRECT_OPTION);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        questionaire.setId(count.incrementAndGet());

        // Create the Questionaire
        QuestionaireDTO questionaireDTO = questionaireMapper.toDto(questionaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        questionaire.setId(count.incrementAndGet());

        // Create the Questionaire
        QuestionaireDTO questionaireDTO = questionaireMapper.toDto(questionaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionaire() throws Exception {
        int databaseSizeBeforeUpdate = questionaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        questionaire.setId(count.incrementAndGet());

        // Create the Questionaire
        QuestionaireDTO questionaireDTO = questionaireMapper.toDto(questionaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionaire in the database
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteQuestionaire() throws Exception {
        // Initialize the database
        questionaireRepository.saveAndFlush(questionaire);
        questionaireRepository.save(questionaire);
        questionaireSearchRepository.save(questionaire);

        int databaseSizeBeforeDelete = questionaireRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the questionaire
        restQuestionaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questionaire> questionaireList = questionaireRepository.findAll();
        assertThat(questionaireList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(questionaireSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchQuestionaire() throws Exception {
        // Initialize the database
        questionaire = questionaireRepository.saveAndFlush(questionaire);
        questionaireSearchRepository.save(questionaire);

        // Search the questionaire
        restQuestionaireMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + questionaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].questTitle").value(hasItem(DEFAULT_QUEST_TITLE)))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)))
            .andExpect(jsonPath("$.[*].correctOption").value(hasItem(DEFAULT_CORRECT_OPTION)));
    }
}
