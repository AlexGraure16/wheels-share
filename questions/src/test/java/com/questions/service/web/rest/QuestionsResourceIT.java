package com.questions.service.web.rest;

import com.questions.service.QuestionsServiceApp;
import com.questions.service.domain.Questions;
import com.questions.service.repository.QuestionsRepository;
import com.questions.service.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.questions.service.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link QuestionsResource} REST controller.
 */
@SpringBootTest(classes = QuestionsServiceApp.class)
public class QuestionsResourceIT {

    private static final String DEFAULT_USER_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER = null;
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    @Autowired
    private QuestionsRepository questionsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restQuestionsMockMvc;

    private Questions questions;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionsResource questionsResource = new QuestionsResource(questionsRepository);
        this.restQuestionsMockMvc = MockMvcBuilders.standaloneSetup(questionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questions createEntity(EntityManager em) {
        Questions questions = new Questions()
            .userEmailAddress(DEFAULT_USER_EMAIL_ADDRESS)
            .question(DEFAULT_QUESTION)
            .answer(DEFAULT_ANSWER);
        return questions;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questions createUpdatedEntity(EntityManager em) {
        Questions questions = new Questions()
            .userEmailAddress(UPDATED_USER_EMAIL_ADDRESS)
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER);
        return questions;
    }

    @BeforeEach
    public void initTest() {
        questions = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestions() throws Exception {
        int databaseSizeBeforeCreate = questionsRepository.findAll().size();

        // Create the Questions
        restQuestionsMockMvc.perform(post("/api/questions/add")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isCreated());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate + 1);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getUserEmailAddress()).isEqualTo(DEFAULT_USER_EMAIL_ADDRESS);
        assertThat(testQuestions.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testQuestions.getAnswer()).isEqualTo(DEFAULT_ANSWER);
    }

    @Test
    @Transactional
    public void createQuestionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionsRepository.findAll().size();

        // Create the Questions with an existing ID
        questions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionsMockMvc.perform(post("/api/questions/add")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList
        restQuestionsMockMvc.perform(get("/api/questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].userEmailAddress").value(hasItem(DEFAULT_USER_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)));
    }

    @Test
    @Transactional
    public void getAllPendingQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList
        restQuestionsMockMvc.perform(get("/api/pending-questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questions.getId().intValue())))
            .andExpect(jsonPath("$.[*].userEmailAddress").value(hasItem(DEFAULT_USER_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)));
    }

    @Test
    @Transactional
    public void getAllAnsweredQuestions() throws Exception {
        // Initialize the database
        questions.answer(UPDATED_ANSWER);
        questionsRepository.saveAndFlush(questions);

        // Get all the questionsList
        restQuestionsMockMvc.perform(get("/api/answered-questions"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(UPDATED_ANSWER)));
    }

    @Test
    @Transactional
    public void getQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        // Get the questions
        restQuestionsMockMvc.perform(get("/api/questions/{id}", questions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(questions.getId().intValue()))
            .andExpect(jsonPath("$.userEmailAddress").value(DEFAULT_USER_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER));
    }

    @Test
    @Transactional
    public void getNonExistingQuestions() throws Exception {
        // Get the questions
        restQuestionsMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Update the questions
        Questions updatedQuestions = questionsRepository.findById(questions.getId()).get();
        // Disconnect from session so that the updates on updatedQuestions are not directly saved in db
        em.detach(updatedQuestions);
        updatedQuestions
            .userEmailAddress(UPDATED_USER_EMAIL_ADDRESS)
            .question(UPDATED_QUESTION)
            .answer(UPDATED_ANSWER);

        restQuestionsMockMvc.perform(post("/api/questions/answer")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestions)))
            .andExpect(status().isOk());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
        Questions testQuestions = questionsList.get(questionsList.size() - 1);
        assertThat(testQuestions.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestions() throws Exception {
        int databaseSizeBeforeUpdate = questionsRepository.findAll().size();

        // Create the Questions

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionsMockMvc.perform(post("/api/questions/answer")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questions)))
            .andExpect(status().isBadRequest());

        // Validate the Questions in the database
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestions() throws Exception {
        // Initialize the database
        questionsRepository.saveAndFlush(questions);

        int databaseSizeBeforeDelete = questionsRepository.findAll().size();

        // Delete the questions
        restQuestionsMockMvc.perform(delete("/api/questions/{id}", questions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questions> questionsList = questionsRepository.findAll();
        assertThat(questionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
