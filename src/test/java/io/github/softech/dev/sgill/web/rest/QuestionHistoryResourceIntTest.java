package io.github.softech.dev.sgill.web.rest;

import io.github.softech.dev.sgill.SmartCpdApp;

import io.github.softech.dev.sgill.domain.Questionhistory;
import io.github.softech.dev.sgill.domain.Customer;
import io.github.softech.dev.sgill.domain.Question;
import io.github.softech.dev.sgill.domain.Choice;
import io.github.softech.dev.sgill.repository.QuestionHistoryRepository;
import io.github.softech.dev.sgill.repository.search.QuestionhistorySearchRepository;
import io.github.softech.dev.sgill.service.QuestionhistoryService;
import io.github.softech.dev.sgill.web.rest.errors.ExceptionTranslator;
import io.github.softech.dev.sgill.service.QuestionhistoryQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Test class for the QuestionhistoryResource REST controller.
 *
 * @see QuestionhistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartCpdApp.class)
public class QuestionhistoryResourceIntTest {

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CORRECT = false;
    private static final Boolean UPDATED_CORRECT = true;

    @Autowired
    private QuestionHistoryRepository questionhistoryRepository;

    

    @Autowired
    private QuestionhistoryService questionhistoryService;

    /**
     * This repository is mocked in the io.github.softech.dev.sgill.repository.search test package.
     *
     * @see io.github.softech.dev.sgill.repository.search.QuestionhistorySearchRepositoryMockConfiguration
     */
    @Autowired
    private QuestionhistorySearchRepository mockQuestionhistorySearchRepository;

    @Autowired
    private QuestionhistoryQueryService questionhistoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuestionhistoryMockMvc;

    private Questionhistory questionhistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuestionhistoryResource questionhistoryResource = new QuestionhistoryResource(questionhistoryService, questionhistoryQueryService);
        this.restQuestionhistoryMockMvc = MockMvcBuilders.standaloneSetup(questionhistoryResource)
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
    public static Questionhistory createEntity(EntityManager em) {
        Questionhistory questionhistory = new Questionhistory()
            .timestamp(DEFAULT_TIMESTAMP)
            .correct(DEFAULT_CORRECT);
        // Add required entity
        Choice choice = ChoiceResourceIntTest.createEntity(em);
        em.persist(choice);
        em.flush();
        questionhistory.setChoice(choice);
        return questionhistory;
    }

    @Before
    public void initTest() {
        questionhistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestionhistory() throws Exception {
        int databaseSizeBeforeCreate = questionhistoryRepository.findAll().size();

        // Create the Questionhistory
        restQuestionhistoryMockMvc.perform(post("/api/questionhistories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionhistory)))
            .andExpect(status().isCreated());

        // Validate the Questionhistory in the database
        List<Questionhistory> questionhistoryList = questionhistoryRepository.findAll();
        assertThat(questionhistoryList).hasSize(databaseSizeBeforeCreate + 1);
        Questionhistory testQuestionhistory = questionhistoryList.get(questionhistoryList.size() - 1);
        assertThat(testQuestionhistory.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testQuestionhistory.isCorrect()).isEqualTo(DEFAULT_CORRECT);

        // Validate the Questionhistory in Elasticsearch
        verify(mockQuestionhistorySearchRepository, times(1)).save(testQuestionhistory);
    }

    @Test
    @Transactional
    public void createQuestionhistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionhistoryRepository.findAll().size();

        // Create the Questionhistory with an existing ID
        questionhistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionhistoryMockMvc.perform(post("/api/questionhistories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionhistory)))
            .andExpect(status().isBadRequest());

        // Validate the Questionhistory in the database
        List<Questionhistory> questionhistoryList = questionhistoryRepository.findAll();
        assertThat(questionhistoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the Questionhistory in Elasticsearch
        verify(mockQuestionhistorySearchRepository, times(0)).save(questionhistory);
    }

    @Test
    @Transactional
    public void getAllQuestionhistories() throws Exception {
        // Initialize the database
        questionhistoryRepository.saveAndFlush(questionhistory);

        // Get all the questionhistoryList
        restQuestionhistoryMockMvc.perform(get("/api/questionhistories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionhistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getQuestionhistory() throws Exception {
        // Initialize the database
        questionhistoryRepository.saveAndFlush(questionhistory);

        // Get the questionhistory
        restQuestionhistoryMockMvc.perform(get("/api/questionhistories/{id}", questionhistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(questionhistory.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.correct").value(DEFAULT_CORRECT.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllQuestionhistoriesByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        questionhistoryRepository.saveAndFlush(questionhistory);

        // Get all the questionhistoryList where timestamp equals to DEFAULT_TIMESTAMP
        defaultQuestionhistoryShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the questionhistoryList where timestamp equals to UPDATED_TIMESTAMP
        defaultQuestionhistoryShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllQuestionhistoriesByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        questionhistoryRepository.saveAndFlush(questionhistory);

        // Get all the questionhistoryList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultQuestionhistoryShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the questionhistoryList where timestamp equals to UPDATED_TIMESTAMP
        defaultQuestionhistoryShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllQuestionhistoriesByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionhistoryRepository.saveAndFlush(questionhistory);

        // Get all the questionhistoryList where timestamp is not null
        defaultQuestionhistoryShouldBeFound("timestamp.specified=true");

        // Get all the questionhistoryList where timestamp is null
        defaultQuestionhistoryShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionhistoriesByCorrectIsEqualToSomething() throws Exception {
        // Initialize the database
        questionhistoryRepository.saveAndFlush(questionhistory);

        // Get all the questionhistoryList where correct equals to DEFAULT_CORRECT
        defaultQuestionhistoryShouldBeFound("correct.equals=" + DEFAULT_CORRECT);

        // Get all the questionhistoryList where correct equals to UPDATED_CORRECT
        defaultQuestionhistoryShouldNotBeFound("correct.equals=" + UPDATED_CORRECT);
    }

    @Test
    @Transactional
    public void getAllQuestionhistoriesByCorrectIsInShouldWork() throws Exception {
        // Initialize the database
        questionhistoryRepository.saveAndFlush(questionhistory);

        // Get all the questionhistoryList where correct in DEFAULT_CORRECT or UPDATED_CORRECT
        defaultQuestionhistoryShouldBeFound("correct.in=" + DEFAULT_CORRECT + "," + UPDATED_CORRECT);

        // Get all the questionhistoryList where correct equals to UPDATED_CORRECT
        defaultQuestionhistoryShouldNotBeFound("correct.in=" + UPDATED_CORRECT);
    }

    @Test
    @Transactional
    public void getAllQuestionhistoriesByCorrectIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionhistoryRepository.saveAndFlush(questionhistory);

        // Get all the questionhistoryList where correct is not null
        defaultQuestionhistoryShouldBeFound("correct.specified=true");

        // Get all the questionhistoryList where correct is null
        defaultQuestionhistoryShouldNotBeFound("correct.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionhistoriesByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        Customer customer = CustomerResourceIntTest.createEntity(em);
        em.persist(customer);
        em.flush();
        questionhistory.setCustomer(customer);
        questionhistoryRepository.saveAndFlush(questionhistory);
        Long customerId = customer.getId();

        // Get all the questionhistoryList where customer equals to customerId
        defaultQuestionhistoryShouldBeFound("customerId.equals=" + customerId);

        // Get all the questionhistoryList where customer equals to customerId + 1
        defaultQuestionhistoryShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionhistoriesByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        Question question = QuestionResourceIntTest.createEntity(em);
        em.persist(question);
        em.flush();
        questionhistory.setQuestion(question);
        questionhistoryRepository.saveAndFlush(questionhistory);
        Long questionId = question.getId();

        // Get all the questionhistoryList where question equals to questionId
        defaultQuestionhistoryShouldBeFound("questionId.equals=" + questionId);

        // Get all the questionhistoryList where question equals to questionId + 1
        defaultQuestionhistoryShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionhistoriesByChoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        Choice choice = ChoiceResourceIntTest.createEntity(em);
        em.persist(choice);
        em.flush();
        questionhistory.setChoice(choice);
        questionhistoryRepository.saveAndFlush(questionhistory);
        Long choiceId = choice.getId();

        // Get all the questionhistoryList where choice equals to choiceId
        defaultQuestionhistoryShouldBeFound("choiceId.equals=" + choiceId);

        // Get all the questionhistoryList where choice equals to choiceId + 1
        defaultQuestionhistoryShouldNotBeFound("choiceId.equals=" + (choiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultQuestionhistoryShouldBeFound(String filter) throws Exception {
        restQuestionhistoryMockMvc.perform(get("/api/questionhistories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionhistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultQuestionhistoryShouldNotBeFound(String filter) throws Exception {
        restQuestionhistoryMockMvc.perform(get("/api/questionhistories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingQuestionhistory() throws Exception {
        // Get the questionhistory
        restQuestionhistoryMockMvc.perform(get("/api/questionhistories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionhistory() throws Exception {
        // Initialize the database
        questionhistoryService.save(questionhistory);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockQuestionhistorySearchRepository);

        int databaseSizeBeforeUpdate = questionhistoryRepository.findAll().size();

        // Update the questionhistory
        Questionhistory updatedQuestionhistory = questionhistoryRepository.findById(questionhistory.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionhistory are not directly saved in db
        em.detach(updatedQuestionhistory);
        updatedQuestionhistory
            .timestamp(UPDATED_TIMESTAMP)
            .correct(UPDATED_CORRECT);

        restQuestionhistoryMockMvc.perform(put("/api/questionhistories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestionhistory)))
            .andExpect(status().isOk());

        // Validate the Questionhistory in the database
        List<Questionhistory> questionhistoryList = questionhistoryRepository.findAll();
        assertThat(questionhistoryList).hasSize(databaseSizeBeforeUpdate);
        Questionhistory testQuestionhistory = questionhistoryList.get(questionhistoryList.size() - 1);
        assertThat(testQuestionhistory.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testQuestionhistory.isCorrect()).isEqualTo(UPDATED_CORRECT);

        // Validate the Questionhistory in Elasticsearch
        verify(mockQuestionhistorySearchRepository, times(1)).save(testQuestionhistory);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestionhistory() throws Exception {
        int databaseSizeBeforeUpdate = questionhistoryRepository.findAll().size();

        // Create the Questionhistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restQuestionhistoryMockMvc.perform(put("/api/questionhistories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(questionhistory)))
            .andExpect(status().isBadRequest());

        // Validate the Questionhistory in the database
        List<Questionhistory> questionhistoryList = questionhistoryRepository.findAll();
        assertThat(questionhistoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Questionhistory in Elasticsearch
        verify(mockQuestionhistorySearchRepository, times(0)).save(questionhistory);
    }

    @Test
    @Transactional
    public void deleteQuestionhistory() throws Exception {
        // Initialize the database
        questionhistoryService.save(questionhistory);

        int databaseSizeBeforeDelete = questionhistoryRepository.findAll().size();

        // Get the questionhistory
        restQuestionhistoryMockMvc.perform(delete("/api/questionhistories/{id}", questionhistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Questionhistory> questionhistoryList = questionhistoryRepository.findAll();
        assertThat(questionhistoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Questionhistory in Elasticsearch
        verify(mockQuestionhistorySearchRepository, times(1)).deleteById(questionhistory.getId());
    }

    @Test
    @Transactional
    public void searchQuestionhistory() throws Exception {
        // Initialize the database
        questionhistoryService.save(questionhistory);
        when(mockQuestionhistorySearchRepository.search(queryStringQuery("id:" + questionhistory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(questionhistory), PageRequest.of(0, 1), 1));
        // Search the questionhistory
        restQuestionhistoryMockMvc.perform(get("/api/_search/questionhistories?query=id:" + questionhistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionhistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Questionhistory.class);
        Questionhistory questionhistory1 = new Questionhistory();
        questionhistory1.setId(1L);
        Questionhistory questionhistory2 = new Questionhistory();
        questionhistory2.setId(questionhistory1.getId());
        assertThat(questionhistory1).isEqualTo(questionhistory2);
        questionhistory2.setId(2L);
        assertThat(questionhistory1).isNotEqualTo(questionhistory2);
        questionhistory1.setId(null);
        assertThat(questionhistory1).isNotEqualTo(questionhistory2);
    }
}
