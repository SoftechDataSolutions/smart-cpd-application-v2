package io.github.softech.dev.sgill.web.rest;

import io.github.softech.dev.sgill.SmartCpdApp;

import io.github.softech.dev.sgill.domain.Question;
import io.github.softech.dev.sgill.domain.Choice;
import io.github.softech.dev.sgill.domain.Quiz;
import io.github.softech.dev.sgill.repository.QuestionRepository;
import io.github.softech.dev.sgill.repository.search.QuestionSearchRepository;
import io.github.softech.dev.sgill.service.QuestionService;
import io.github.softech.dev.sgill.web.rest.errors.ExceptionTranslator;
import io.github.softech.dev.sgill.service.dto.QuestionCriteria;
import io.github.softech.dev.sgill.service.QuestionQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static io.github.softech.dev.sgill.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the QuestionResource REST controller.
 *
 * @see QuestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartCpdApp.class)
public class QuestionResourceIntTest {

    private static final String DEFAULT_TEXT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_QUESTION = "BBBBBBBBBB";

    private static final String DEFAULT_DIFFICULTY = "AAAAAAAAAA";
    private static final String UPDATED_DIFFICULTY = "BBBBBBBBBB";

    @Autowired
    private QuestionRepository questionRepository;

    

    @Autowired
    private QuestionService questionService;

    /**
     * This repository is mocked in the io.github.softech.dev.sgill.repository.search test package.
     *
     * @see io.github.softech.dev.sgill.repository.search.QuestionSearchRepositoryMockConfiguration
     */
    @Autowired
    private QuestionSearchRepository mockQuestionSearchRepository;

    @Autowired
    private QuestionQueryService questionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuestionMockMvc;

    private Question question;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionResource questionResource = new QuestionResource(questionService, questionQueryService);
        this.restQuestionMockMvc = MockMvcBuilders.standaloneSetup(questionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .textQuestion(DEFAULT_TEXT_QUESTION)
            .difficulty(DEFAULT_DIFFICULTY);
        return question;
    }

    @Before
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question
        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getTextQuestion()).isEqualTo(DEFAULT_TEXT_QUESTION);
        assertThat(testQuestion.getDifficulty()).isEqualTo(DEFAULT_DIFFICULTY);

        // Validate the Question in Elasticsearch
        verify(mockQuestionSearchRepository, times(1)).save(testQuestion);
    }

    @Test
    @Transactional
    public void createQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question with an existing ID
        question.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Question in Elasticsearch
        verify(mockQuestionSearchRepository, times(0)).save(question);
    }

    @Test
    @Transactional
    public void checkTextQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setTextQuestion(null);

        // Create the Question, which fails.

        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].textQuestion").value(hasItem(DEFAULT_TEXT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.toString())));
    }
    

    @Test
    @Transactional
    public void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.textQuestion").value(DEFAULT_TEXT_QUESTION.toString()))
            .andExpect(jsonPath("$.difficulty").value(DEFAULT_DIFFICULTY.toString()));
    }

    @Test
    @Transactional
    public void getAllQuestionsByTextQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where textQuestion equals to DEFAULT_TEXT_QUESTION
        defaultQuestionShouldBeFound("textQuestion.equals=" + DEFAULT_TEXT_QUESTION);

        // Get all the questionList where textQuestion equals to UPDATED_TEXT_QUESTION
        defaultQuestionShouldNotBeFound("textQuestion.equals=" + UPDATED_TEXT_QUESTION);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTextQuestionIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where textQuestion in DEFAULT_TEXT_QUESTION or UPDATED_TEXT_QUESTION
        defaultQuestionShouldBeFound("textQuestion.in=" + DEFAULT_TEXT_QUESTION + "," + UPDATED_TEXT_QUESTION);

        // Get all the questionList where textQuestion equals to UPDATED_TEXT_QUESTION
        defaultQuestionShouldNotBeFound("textQuestion.in=" + UPDATED_TEXT_QUESTION);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTextQuestionIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where textQuestion is not null
        defaultQuestionShouldBeFound("textQuestion.specified=true");

        // Get all the questionList where textQuestion is null
        defaultQuestionShouldNotBeFound("textQuestion.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByDifficultyIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where difficulty equals to DEFAULT_DIFFICULTY
        defaultQuestionShouldBeFound("difficulty.equals=" + DEFAULT_DIFFICULTY);

        // Get all the questionList where difficulty equals to UPDATED_DIFFICULTY
        defaultQuestionShouldNotBeFound("difficulty.equals=" + UPDATED_DIFFICULTY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByDifficultyIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where difficulty in DEFAULT_DIFFICULTY or UPDATED_DIFFICULTY
        defaultQuestionShouldBeFound("difficulty.in=" + DEFAULT_DIFFICULTY + "," + UPDATED_DIFFICULTY);

        // Get all the questionList where difficulty equals to UPDATED_DIFFICULTY
        defaultQuestionShouldNotBeFound("difficulty.in=" + UPDATED_DIFFICULTY);
    }

    @Test
    @Transactional
    public void getAllQuestionsByDifficultyIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where difficulty is not null
        defaultQuestionShouldBeFound("difficulty.specified=true");

        // Get all the questionList where difficulty is null
        defaultQuestionShouldNotBeFound("difficulty.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByChoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        Choice choice = ChoiceResourceIntTest.createEntity(em);
        em.persist(choice);
        em.flush();
        question.addChoice(choice);
        questionRepository.saveAndFlush(question);
        Long choiceId = choice.getId();

        // Get all the questionList where choice equals to choiceId
        defaultQuestionShouldBeFound("choiceId.equals=" + choiceId);

        // Get all the questionList where choice equals to choiceId + 1
        defaultQuestionShouldNotBeFound("choiceId.equals=" + (choiceId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionsByQuizIsEqualToSomething() throws Exception {
        // Initialize the database
        Quiz quiz = QuizResourceIntTest.createEntity(em);
        em.persist(quiz);
        em.flush();
        question.setQuiz(quiz);
        questionRepository.saveAndFlush(question);
        Long quizId = quiz.getId();

        // Get all the questionList where quiz equals to quizId
        defaultQuestionShouldBeFound("quizId.equals=" + quizId);

        // Get all the questionList where quiz equals to quizId + 1
        defaultQuestionShouldNotBeFound("quizId.equals=" + (quizId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultQuestionShouldBeFound(String filter) throws Exception {
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].textQuestion").value(hasItem(DEFAULT_TEXT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultQuestionShouldNotBeFound(String filter) throws Exception {
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestion() throws Exception {
        // Initialize the database
        questionService.save(question);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockQuestionSearchRepository);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .textQuestion(UPDATED_TEXT_QUESTION)
            .difficulty(UPDATED_DIFFICULTY);

        restQuestionMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestion)))
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getTextQuestion()).isEqualTo(UPDATED_TEXT_QUESTION);
        assertThat(testQuestion.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);

        // Validate the Question in Elasticsearch
        verify(mockQuestionSearchRepository, times(1)).save(testQuestion);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Create the Question

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restQuestionMockMvc.perform(put("/api/questions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Question in Elasticsearch
        verify(mockQuestionSearchRepository, times(0)).save(question);
    }

    @Test
    @Transactional
    public void deleteQuestion() throws Exception {
        // Initialize the database
        questionService.save(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Get the question
        restQuestionMockMvc.perform(delete("/api/questions/{id}", question.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Question in Elasticsearch
        verify(mockQuestionSearchRepository, times(1)).deleteById(question.getId());
    }

    @Test
    @Transactional
    public void searchQuestion() throws Exception {
        // Initialize the database
        questionService.save(question);
        when(mockQuestionSearchRepository.search(queryStringQuery("id:" + question.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(question), PageRequest.of(0, 1), 1));
        // Search the question
        restQuestionMockMvc.perform(get("/api/_search/questions?query=id:" + question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].textQuestion").value(hasItem(DEFAULT_TEXT_QUESTION.toString())))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = new Question();
        question1.setId(1L);
        Question question2 = new Question();
        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);
        question2.setId(2L);
        assertThat(question1).isNotEqualTo(question2);
        question1.setId(null);
        assertThat(question1).isNotEqualTo(question2);
    }
}
