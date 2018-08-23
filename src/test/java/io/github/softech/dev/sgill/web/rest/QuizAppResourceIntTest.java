package io.github.softech.dev.sgill.web.rest;

import io.github.softech.dev.sgill.SmartCpdApp;

import io.github.softech.dev.sgill.domain.QuizApp;
import io.github.softech.dev.sgill.domain.Quiz;
import io.github.softech.dev.sgill.domain.Customer;
import io.github.softech.dev.sgill.repository.QuizAppRepository;
import io.github.softech.dev.sgill.repository.search.QuizAppSearchRepository;
import io.github.softech.dev.sgill.service.QuizAppService;
import io.github.softech.dev.sgill.web.rest.errors.ExceptionTranslator;
import io.github.softech.dev.sgill.service.dto.QuizAppCriteria;
import io.github.softech.dev.sgill.service.QuizAppQueryService;

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
 * Test class for the QuizAppResource REST controller.
 *
 * @see QuizAppResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartCpdApp.class)
public class QuizAppResourceIntTest {

    private static final String DEFAULT_OPTION = "AAAAAAAAAA";
    private static final String UPDATED_OPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CORRECT = false;
    private static final Boolean UPDATED_CORRECT = true;

    @Autowired
    private QuizAppRepository quizAppRepository;

    

    @Autowired
    private QuizAppService quizAppService;

    /**
     * This repository is mocked in the io.github.softech.dev.sgill.repository.search test package.
     *
     * @see io.github.softech.dev.sgill.repository.search.QuizAppSearchRepositoryMockConfiguration
     */
    @Autowired
    private QuizAppSearchRepository mockQuizAppSearchRepository;

    @Autowired
    private QuizAppQueryService quizAppQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuizAppMockMvc;

    private QuizApp quizApp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuizAppResource quizAppResource = new QuizAppResource(quizAppService, quizAppQueryService);
        this.restQuizAppMockMvc = MockMvcBuilders.standaloneSetup(quizAppResource)
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
    public static QuizApp createEntity(EntityManager em) {
        QuizApp quizApp = new QuizApp()
            .option(DEFAULT_OPTION)
            .correct(DEFAULT_CORRECT);
        return quizApp;
    }

    @Before
    public void initTest() {
        quizApp = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuizApp() throws Exception {
        int databaseSizeBeforeCreate = quizAppRepository.findAll().size();

        // Create the QuizApp
        restQuizAppMockMvc.perform(post("/api/quiz-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quizApp)))
            .andExpect(status().isCreated());

        // Validate the QuizApp in the database
        List<QuizApp> quizAppList = quizAppRepository.findAll();
        assertThat(quizAppList).hasSize(databaseSizeBeforeCreate + 1);
        QuizApp testQuizApp = quizAppList.get(quizAppList.size() - 1);
        assertThat(testQuizApp.getOption()).isEqualTo(DEFAULT_OPTION);
        assertThat(testQuizApp.isCorrect()).isEqualTo(DEFAULT_CORRECT);

        // Validate the QuizApp in Elasticsearch
        verify(mockQuizAppSearchRepository, times(1)).save(testQuizApp);
    }

    @Test
    @Transactional
    public void createQuizAppWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quizAppRepository.findAll().size();

        // Create the QuizApp with an existing ID
        quizApp.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizAppMockMvc.perform(post("/api/quiz-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quizApp)))
            .andExpect(status().isBadRequest());

        // Validate the QuizApp in the database
        List<QuizApp> quizAppList = quizAppRepository.findAll();
        assertThat(quizAppList).hasSize(databaseSizeBeforeCreate);

        // Validate the QuizApp in Elasticsearch
        verify(mockQuizAppSearchRepository, times(0)).save(quizApp);
    }

    @Test
    @Transactional
    public void checkCorrectIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizAppRepository.findAll().size();
        // set the field null
        quizApp.setCorrect(null);

        // Create the QuizApp, which fails.

        restQuizAppMockMvc.perform(post("/api/quiz-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quizApp)))
            .andExpect(status().isBadRequest());

        List<QuizApp> quizAppList = quizAppRepository.findAll();
        assertThat(quizAppList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuizApps() throws Exception {
        // Initialize the database
        quizAppRepository.saveAndFlush(quizApp);

        // Get all the quizAppList
        restQuizAppMockMvc.perform(get("/api/quiz-apps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION.toString())))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getQuizApp() throws Exception {
        // Initialize the database
        quizAppRepository.saveAndFlush(quizApp);

        // Get the quizApp
        restQuizAppMockMvc.perform(get("/api/quiz-apps/{id}", quizApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quizApp.getId().intValue()))
            .andExpect(jsonPath("$.option").value(DEFAULT_OPTION.toString()))
            .andExpect(jsonPath("$.correct").value(DEFAULT_CORRECT.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllQuizAppsByOptionIsEqualToSomething() throws Exception {
        // Initialize the database
        quizAppRepository.saveAndFlush(quizApp);

        // Get all the quizAppList where option equals to DEFAULT_OPTION
        defaultQuizAppShouldBeFound("option.equals=" + DEFAULT_OPTION);

        // Get all the quizAppList where option equals to UPDATED_OPTION
        defaultQuizAppShouldNotBeFound("option.equals=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    public void getAllQuizAppsByOptionIsInShouldWork() throws Exception {
        // Initialize the database
        quizAppRepository.saveAndFlush(quizApp);

        // Get all the quizAppList where option in DEFAULT_OPTION or UPDATED_OPTION
        defaultQuizAppShouldBeFound("option.in=" + DEFAULT_OPTION + "," + UPDATED_OPTION);

        // Get all the quizAppList where option equals to UPDATED_OPTION
        defaultQuizAppShouldNotBeFound("option.in=" + UPDATED_OPTION);
    }

    @Test
    @Transactional
    public void getAllQuizAppsByOptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizAppRepository.saveAndFlush(quizApp);

        // Get all the quizAppList where option is not null
        defaultQuizAppShouldBeFound("option.specified=true");

        // Get all the quizAppList where option is null
        defaultQuizAppShouldNotBeFound("option.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuizAppsByCorrectIsEqualToSomething() throws Exception {
        // Initialize the database
        quizAppRepository.saveAndFlush(quizApp);

        // Get all the quizAppList where correct equals to DEFAULT_CORRECT
        defaultQuizAppShouldBeFound("correct.equals=" + DEFAULT_CORRECT);

        // Get all the quizAppList where correct equals to UPDATED_CORRECT
        defaultQuizAppShouldNotBeFound("correct.equals=" + UPDATED_CORRECT);
    }

    @Test
    @Transactional
    public void getAllQuizAppsByCorrectIsInShouldWork() throws Exception {
        // Initialize the database
        quizAppRepository.saveAndFlush(quizApp);

        // Get all the quizAppList where correct in DEFAULT_CORRECT or UPDATED_CORRECT
        defaultQuizAppShouldBeFound("correct.in=" + DEFAULT_CORRECT + "," + UPDATED_CORRECT);

        // Get all the quizAppList where correct equals to UPDATED_CORRECT
        defaultQuizAppShouldNotBeFound("correct.in=" + UPDATED_CORRECT);
    }

    @Test
    @Transactional
    public void getAllQuizAppsByCorrectIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizAppRepository.saveAndFlush(quizApp);

        // Get all the quizAppList where correct is not null
        defaultQuizAppShouldBeFound("correct.specified=true");

        // Get all the quizAppList where correct is null
        defaultQuizAppShouldNotBeFound("correct.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuizAppsByQuizIsEqualToSomething() throws Exception {
        // Initialize the database
        Quiz quiz = QuizResourceIntTest.createEntity(em);
        em.persist(quiz);
        em.flush();
        quizApp.setQuiz(quiz);
        quizAppRepository.saveAndFlush(quizApp);
        Long quizId = quiz.getId();

        // Get all the quizAppList where quiz equals to quizId
        defaultQuizAppShouldBeFound("quizId.equals=" + quizId);

        // Get all the quizAppList where quiz equals to quizId + 1
        defaultQuizAppShouldNotBeFound("quizId.equals=" + (quizId + 1));
    }


    @Test
    @Transactional
    public void getAllQuizAppsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        quizApp.setCustomer(customer);
        quizAppRepository.saveAndFlush(quizApp);
        Long customerId = customer.getId();

        // Get all the quizAppList where customer equals to customerId
        defaultQuizAppShouldBeFound("customerId.equals=" + customerId);

        // Get all the quizAppList where customer equals to customerId + 1
        defaultQuizAppShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultQuizAppShouldBeFound(String filter) throws Exception {
        restQuizAppMockMvc.perform(get("/api/quiz-apps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION.toString())))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultQuizAppShouldNotBeFound(String filter) throws Exception {
        restQuizAppMockMvc.perform(get("/api/quiz-apps?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingQuizApp() throws Exception {
        // Get the quizApp
        restQuizAppMockMvc.perform(get("/api/quiz-apps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuizApp() throws Exception {
        // Initialize the database
        quizAppService.save(quizApp);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockQuizAppSearchRepository);

        int databaseSizeBeforeUpdate = quizAppRepository.findAll().size();

        // Update the quizApp
        QuizApp updatedQuizApp = quizAppRepository.findById(quizApp.getId()).get();
        // Disconnect from session so that the updates on updatedQuizApp are not directly saved in db
        em.detach(updatedQuizApp);
        updatedQuizApp
            .option(UPDATED_OPTION)
            .correct(UPDATED_CORRECT);

        restQuizAppMockMvc.perform(put("/api/quiz-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuizApp)))
            .andExpect(status().isOk());

        // Validate the QuizApp in the database
        List<QuizApp> quizAppList = quizAppRepository.findAll();
        assertThat(quizAppList).hasSize(databaseSizeBeforeUpdate);
        QuizApp testQuizApp = quizAppList.get(quizAppList.size() - 1);
        assertThat(testQuizApp.getOption()).isEqualTo(UPDATED_OPTION);
        assertThat(testQuizApp.isCorrect()).isEqualTo(UPDATED_CORRECT);

        // Validate the QuizApp in Elasticsearch
        verify(mockQuizAppSearchRepository, times(1)).save(testQuizApp);
    }

    @Test
    @Transactional
    public void updateNonExistingQuizApp() throws Exception {
        int databaseSizeBeforeUpdate = quizAppRepository.findAll().size();

        // Create the QuizApp

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restQuizAppMockMvc.perform(put("/api/quiz-apps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quizApp)))
            .andExpect(status().isBadRequest());

        // Validate the QuizApp in the database
        List<QuizApp> quizAppList = quizAppRepository.findAll();
        assertThat(quizAppList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuizApp in Elasticsearch
        verify(mockQuizAppSearchRepository, times(0)).save(quizApp);
    }

    @Test
    @Transactional
    public void deleteQuizApp() throws Exception {
        // Initialize the database
        quizAppService.save(quizApp);

        int databaseSizeBeforeDelete = quizAppRepository.findAll().size();

        // Get the quizApp
        restQuizAppMockMvc.perform(delete("/api/quiz-apps/{id}", quizApp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuizApp> quizAppList = quizAppRepository.findAll();
        assertThat(quizAppList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the QuizApp in Elasticsearch
        verify(mockQuizAppSearchRepository, times(1)).deleteById(quizApp.getId());
    }

    @Test
    @Transactional
    public void searchQuizApp() throws Exception {
        // Initialize the database
        quizAppService.save(quizApp);
        when(mockQuizAppSearchRepository.search(queryStringQuery("id:" + quizApp.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(quizApp), PageRequest.of(0, 1), 1));
        // Search the quizApp
        restQuizAppMockMvc.perform(get("/api/_search/quiz-apps?query=id:" + quizApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].option").value(hasItem(DEFAULT_OPTION.toString())))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizApp.class);
        QuizApp quizApp1 = new QuizApp();
        quizApp1.setId(1L);
        QuizApp quizApp2 = new QuizApp();
        quizApp2.setId(quizApp1.getId());
        assertThat(quizApp1).isEqualTo(quizApp2);
        quizApp2.setId(2L);
        assertThat(quizApp1).isNotEqualTo(quizApp2);
        quizApp1.setId(null);
        assertThat(quizApp1).isNotEqualTo(quizApp2);
    }
}
