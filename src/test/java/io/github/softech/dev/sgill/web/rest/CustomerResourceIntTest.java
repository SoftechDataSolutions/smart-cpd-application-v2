package io.github.softech.dev.sgill.web.rest;

import io.github.softech.dev.sgill.SmartCpdApp;

import io.github.softech.dev.sgill.domain.Customer;
import io.github.softech.dev.sgill.domain.Company;
import io.github.softech.dev.sgill.domain.User;
import io.github.softech.dev.sgill.repository.CustomerRepository;
import io.github.softech.dev.sgill.repository.search.CustomerSearchRepository;
import io.github.softech.dev.sgill.service.CustomerService;
import io.github.softech.dev.sgill.web.rest.errors.ExceptionTranslator;
import io.github.softech.dev.sgill.service.dto.CustomerCriteria;
import io.github.softech.dev.sgill.service.CustomerQueryService;

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
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static io.github.softech.dev.sgill.web.rest.TestUtil.sameInstant;
import static io.github.softech.dev.sgill.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.softech.dev.sgill.domain.enumeration.TYPES;
/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartCpdApp.class)
public class CustomerResourceIntTest {

    private static final String DEFAULT_NORMALIZED = "AAAAAAAAAA";
    private static final String UPDATED_NORMALIZED = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "9291800631";
    private static final String UPDATED_PHONE = "(1662936618";

    private static final String DEFAULT_STREETADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREETADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTALCODE = "79780";
    private static final String UPDATED_POSTALCODE = "00932";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Instant DEFAULT_REGISTERED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REGISTERED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LASTACTIVE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LASTACTIVE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final ZonedDateTime DEFAULT_CYCLEDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CYCLEDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_AREASERVICED = "AAAAAAAAAA";
    private static final String UPDATED_AREASERVICED = "BBBBBBBBBB";

    private static final TYPES DEFAULT_SPECIALITIES = TYPES.RESIDENCE;
    private static final TYPES UPDATED_SPECIALITIES = TYPES.COMMERCIAL;

    private static final String DEFAULT_TRADES = "AAAAAAAAAA";
    private static final String UPDATED_TRADES = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_MONTH_YEAR = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOW = false;
    private static final Boolean UPDATED_SHOW = true;

    @Autowired
    private CustomerRepository customerRepository;

    

    @Autowired
    private CustomerService customerService;

    /**
     * This repository is mocked in the io.github.softech.dev.sgill.repository.search test package.
     *
     * @see io.github.softech.dev.sgill.repository.search.CustomerSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerSearchRepository mockCustomerSearchRepository;

    @Autowired
    private CustomerQueryService customerQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerResource customerResource = new CustomerResource(customerService, customerQueryService);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
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
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .normalized(DEFAULT_NORMALIZED)
            .phone(DEFAULT_PHONE)
            .streetaddress(DEFAULT_STREETADDRESS)
            .postalcode(DEFAULT_POSTALCODE)
            .city(DEFAULT_CITY)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .country(DEFAULT_COUNTRY)
            .registered(DEFAULT_REGISTERED)
            .lastactive(DEFAULT_LASTACTIVE)
            .points(DEFAULT_POINTS)
            .cycledate(DEFAULT_CYCLEDATE)
            .areaserviced(DEFAULT_AREASERVICED)
            .specialities(DEFAULT_SPECIALITIES)
            .trades(DEFAULT_TRADES)
            .monthYear(DEFAULT_MONTH_YEAR)
            .licenseNumber(DEFAULT_LICENSE_NUMBER)
            .show(DEFAULT_SHOW);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        customer.setUser(user);
        return customer;
    }

    @Before
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getNormalized()).isEqualTo(DEFAULT_NORMALIZED);
        assertThat(testCustomer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomer.getStreetaddress()).isEqualTo(DEFAULT_STREETADDRESS);
        assertThat(testCustomer.getPostalcode()).isEqualTo(DEFAULT_POSTALCODE);
        assertThat(testCustomer.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCustomer.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testCustomer.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCustomer.getRegistered()).isEqualTo(DEFAULT_REGISTERED);
        assertThat(testCustomer.getLastactive()).isEqualTo(DEFAULT_LASTACTIVE);
        assertThat(testCustomer.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCustomer.getCycledate()).isEqualTo(DEFAULT_CYCLEDATE);
        assertThat(testCustomer.getAreaserviced()).isEqualTo(DEFAULT_AREASERVICED);
        assertThat(testCustomer.getSpecialities()).isEqualTo(DEFAULT_SPECIALITIES);
        assertThat(testCustomer.getTrades()).isEqualTo(DEFAULT_TRADES);
        assertThat(testCustomer.getMonthYear()).isEqualTo(DEFAULT_MONTH_YEAR);
        assertThat(testCustomer.getLicenseNumber()).isEqualTo(DEFAULT_LICENSE_NUMBER);
        assertThat(testCustomer.isShow()).isEqualTo(DEFAULT_SHOW);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(1)).save(testCustomer);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(0)).save(customer);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setPhone(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetaddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setStreetaddress(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostalcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setPostalcode(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCity(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateProvinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setStateProvince(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setCountry(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMonthYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setMonthYear(null);

        // Create the Customer, which fails.

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].normalized").value(hasItem(DEFAULT_NORMALIZED.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].streetaddress").value(hasItem(DEFAULT_STREETADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postalcode").value(hasItem(DEFAULT_POSTALCODE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].registered").value(hasItem(DEFAULT_REGISTERED.toString())))
            .andExpect(jsonPath("$.[*].lastactive").value(hasItem(DEFAULT_LASTACTIVE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].cycledate").value(hasItem(sameInstant(DEFAULT_CYCLEDATE))))
            .andExpect(jsonPath("$.[*].areaserviced").value(hasItem(DEFAULT_AREASERVICED.toString())))
            .andExpect(jsonPath("$.[*].specialities").value(hasItem(DEFAULT_SPECIALITIES.toString())))
            .andExpect(jsonPath("$.[*].trades").value(hasItem(DEFAULT_TRADES.toString())))
            .andExpect(jsonPath("$.[*].monthYear").value(hasItem(DEFAULT_MONTH_YEAR.toString())))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].show").value(hasItem(DEFAULT_SHOW.booleanValue())));
    }
    

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.normalized").value(DEFAULT_NORMALIZED.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.streetaddress").value(DEFAULT_STREETADDRESS.toString()))
            .andExpect(jsonPath("$.postalcode").value(DEFAULT_POSTALCODE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.registered").value(DEFAULT_REGISTERED.toString()))
            .andExpect(jsonPath("$.lastactive").value(DEFAULT_LASTACTIVE.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.cycledate").value(sameInstant(DEFAULT_CYCLEDATE)))
            .andExpect(jsonPath("$.areaserviced").value(DEFAULT_AREASERVICED.toString()))
            .andExpect(jsonPath("$.specialities").value(DEFAULT_SPECIALITIES.toString()))
            .andExpect(jsonPath("$.trades").value(DEFAULT_TRADES.toString()))
            .andExpect(jsonPath("$.monthYear").value(DEFAULT_MONTH_YEAR.toString()))
            .andExpect(jsonPath("$.licenseNumber").value(DEFAULT_LICENSE_NUMBER.toString()))
            .andExpect(jsonPath("$.show").value(DEFAULT_SHOW.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllCustomersByNormalizedIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where normalized equals to DEFAULT_NORMALIZED
        defaultCustomerShouldBeFound("normalized.equals=" + DEFAULT_NORMALIZED);

        // Get all the customerList where normalized equals to UPDATED_NORMALIZED
        defaultCustomerShouldNotBeFound("normalized.equals=" + UPDATED_NORMALIZED);
    }

    @Test
    @Transactional
    public void getAllCustomersByNormalizedIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where normalized in DEFAULT_NORMALIZED or UPDATED_NORMALIZED
        defaultCustomerShouldBeFound("normalized.in=" + DEFAULT_NORMALIZED + "," + UPDATED_NORMALIZED);

        // Get all the customerList where normalized equals to UPDATED_NORMALIZED
        defaultCustomerShouldNotBeFound("normalized.in=" + UPDATED_NORMALIZED);
    }

    @Test
    @Transactional
    public void getAllCustomersByNormalizedIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where normalized is not null
        defaultCustomerShouldBeFound("normalized.specified=true");

        // Get all the customerList where normalized is null
        defaultCustomerShouldNotBeFound("normalized.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone equals to DEFAULT_PHONE
        defaultCustomerShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the customerList where phone equals to UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCustomerShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the customerList where phone equals to UPDATED_PHONE
        defaultCustomerShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where phone is not null
        defaultCustomerShouldBeFound("phone.specified=true");

        // Get all the customerList where phone is null
        defaultCustomerShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByStreetaddressIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where streetaddress equals to DEFAULT_STREETADDRESS
        defaultCustomerShouldBeFound("streetaddress.equals=" + DEFAULT_STREETADDRESS);

        // Get all the customerList where streetaddress equals to UPDATED_STREETADDRESS
        defaultCustomerShouldNotBeFound("streetaddress.equals=" + UPDATED_STREETADDRESS);
    }

    @Test
    @Transactional
    public void getAllCustomersByStreetaddressIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where streetaddress in DEFAULT_STREETADDRESS or UPDATED_STREETADDRESS
        defaultCustomerShouldBeFound("streetaddress.in=" + DEFAULT_STREETADDRESS + "," + UPDATED_STREETADDRESS);

        // Get all the customerList where streetaddress equals to UPDATED_STREETADDRESS
        defaultCustomerShouldNotBeFound("streetaddress.in=" + UPDATED_STREETADDRESS);
    }

    @Test
    @Transactional
    public void getAllCustomersByStreetaddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where streetaddress is not null
        defaultCustomerShouldBeFound("streetaddress.specified=true");

        // Get all the customerList where streetaddress is null
        defaultCustomerShouldNotBeFound("streetaddress.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPostalcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where postalcode equals to DEFAULT_POSTALCODE
        defaultCustomerShouldBeFound("postalcode.equals=" + DEFAULT_POSTALCODE);

        // Get all the customerList where postalcode equals to UPDATED_POSTALCODE
        defaultCustomerShouldNotBeFound("postalcode.equals=" + UPDATED_POSTALCODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPostalcodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where postalcode in DEFAULT_POSTALCODE or UPDATED_POSTALCODE
        defaultCustomerShouldBeFound("postalcode.in=" + DEFAULT_POSTALCODE + "," + UPDATED_POSTALCODE);

        // Get all the customerList where postalcode equals to UPDATED_POSTALCODE
        defaultCustomerShouldNotBeFound("postalcode.in=" + UPDATED_POSTALCODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPostalcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where postalcode is not null
        defaultCustomerShouldBeFound("postalcode.specified=true");

        // Get all the customerList where postalcode is null
        defaultCustomerShouldNotBeFound("postalcode.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city equals to DEFAULT_CITY
        defaultCustomerShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the customerList where city equals to UPDATED_CITY
        defaultCustomerShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city in DEFAULT_CITY or UPDATED_CITY
        defaultCustomerShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the customerList where city equals to UPDATED_CITY
        defaultCustomerShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city is not null
        defaultCustomerShouldBeFound("city.specified=true");

        // Get all the customerList where city is null
        defaultCustomerShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByStateProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where stateProvince equals to DEFAULT_STATE_PROVINCE
        defaultCustomerShouldBeFound("stateProvince.equals=" + DEFAULT_STATE_PROVINCE);

        // Get all the customerList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultCustomerShouldNotBeFound("stateProvince.equals=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void getAllCustomersByStateProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where stateProvince in DEFAULT_STATE_PROVINCE or UPDATED_STATE_PROVINCE
        defaultCustomerShouldBeFound("stateProvince.in=" + DEFAULT_STATE_PROVINCE + "," + UPDATED_STATE_PROVINCE);

        // Get all the customerList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultCustomerShouldNotBeFound("stateProvince.in=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    public void getAllCustomersByStateProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where stateProvince is not null
        defaultCustomerShouldBeFound("stateProvince.specified=true");

        // Get all the customerList where stateProvince is null
        defaultCustomerShouldNotBeFound("stateProvince.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where country equals to DEFAULT_COUNTRY
        defaultCustomerShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the customerList where country equals to UPDATED_COUNTRY
        defaultCustomerShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultCustomerShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the customerList where country equals to UPDATED_COUNTRY
        defaultCustomerShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where country is not null
        defaultCustomerShouldBeFound("country.specified=true");

        // Get all the customerList where country is null
        defaultCustomerShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByRegisteredIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where registered equals to DEFAULT_REGISTERED
        defaultCustomerShouldBeFound("registered.equals=" + DEFAULT_REGISTERED);

        // Get all the customerList where registered equals to UPDATED_REGISTERED
        defaultCustomerShouldNotBeFound("registered.equals=" + UPDATED_REGISTERED);
    }

    @Test
    @Transactional
    public void getAllCustomersByRegisteredIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where registered in DEFAULT_REGISTERED or UPDATED_REGISTERED
        defaultCustomerShouldBeFound("registered.in=" + DEFAULT_REGISTERED + "," + UPDATED_REGISTERED);

        // Get all the customerList where registered equals to UPDATED_REGISTERED
        defaultCustomerShouldNotBeFound("registered.in=" + UPDATED_REGISTERED);
    }

    @Test
    @Transactional
    public void getAllCustomersByRegisteredIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where registered is not null
        defaultCustomerShouldBeFound("registered.specified=true");

        // Get all the customerList where registered is null
        defaultCustomerShouldNotBeFound("registered.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLastactiveIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastactive equals to DEFAULT_LASTACTIVE
        defaultCustomerShouldBeFound("lastactive.equals=" + DEFAULT_LASTACTIVE);

        // Get all the customerList where lastactive equals to UPDATED_LASTACTIVE
        defaultCustomerShouldNotBeFound("lastactive.equals=" + UPDATED_LASTACTIVE);
    }

    @Test
    @Transactional
    public void getAllCustomersByLastactiveIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastactive in DEFAULT_LASTACTIVE or UPDATED_LASTACTIVE
        defaultCustomerShouldBeFound("lastactive.in=" + DEFAULT_LASTACTIVE + "," + UPDATED_LASTACTIVE);

        // Get all the customerList where lastactive equals to UPDATED_LASTACTIVE
        defaultCustomerShouldNotBeFound("lastactive.in=" + UPDATED_LASTACTIVE);
    }

    @Test
    @Transactional
    public void getAllCustomersByLastactiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastactive is not null
        defaultCustomerShouldBeFound("lastactive.specified=true");

        // Get all the customerList where lastactive is null
        defaultCustomerShouldNotBeFound("lastactive.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where points equals to DEFAULT_POINTS
        defaultCustomerShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the customerList where points equals to UPDATED_POINTS
        defaultCustomerShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllCustomersByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultCustomerShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the customerList where points equals to UPDATED_POINTS
        defaultCustomerShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllCustomersByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where points is not null
        defaultCustomerShouldBeFound("points.specified=true");

        // Get all the customerList where points is null
        defaultCustomerShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where points greater than or equals to DEFAULT_POINTS
        defaultCustomerShouldBeFound("points.greaterOrEqualThan=" + DEFAULT_POINTS);

        // Get all the customerList where points greater than or equals to UPDATED_POINTS
        defaultCustomerShouldNotBeFound("points.greaterOrEqualThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllCustomersByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where points less than or equals to DEFAULT_POINTS
        defaultCustomerShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the customerList where points less than or equals to UPDATED_POINTS
        defaultCustomerShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }


    @Test
    @Transactional
    public void getAllCustomersByCycledateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where cycledate equals to DEFAULT_CYCLEDATE
        defaultCustomerShouldBeFound("cycledate.equals=" + DEFAULT_CYCLEDATE);

        // Get all the customerList where cycledate equals to UPDATED_CYCLEDATE
        defaultCustomerShouldNotBeFound("cycledate.equals=" + UPDATED_CYCLEDATE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCycledateIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where cycledate in DEFAULT_CYCLEDATE or UPDATED_CYCLEDATE
        defaultCustomerShouldBeFound("cycledate.in=" + DEFAULT_CYCLEDATE + "," + UPDATED_CYCLEDATE);

        // Get all the customerList where cycledate equals to UPDATED_CYCLEDATE
        defaultCustomerShouldNotBeFound("cycledate.in=" + UPDATED_CYCLEDATE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCycledateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where cycledate is not null
        defaultCustomerShouldBeFound("cycledate.specified=true");

        // Get all the customerList where cycledate is null
        defaultCustomerShouldNotBeFound("cycledate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByCycledateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where cycledate greater than or equals to DEFAULT_CYCLEDATE
        defaultCustomerShouldBeFound("cycledate.greaterOrEqualThan=" + DEFAULT_CYCLEDATE);

        // Get all the customerList where cycledate greater than or equals to UPDATED_CYCLEDATE
        defaultCustomerShouldNotBeFound("cycledate.greaterOrEqualThan=" + UPDATED_CYCLEDATE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCycledateIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where cycledate less than or equals to DEFAULT_CYCLEDATE
        defaultCustomerShouldNotBeFound("cycledate.lessThan=" + DEFAULT_CYCLEDATE);

        // Get all the customerList where cycledate less than or equals to UPDATED_CYCLEDATE
        defaultCustomerShouldBeFound("cycledate.lessThan=" + UPDATED_CYCLEDATE);
    }


    @Test
    @Transactional
    public void getAllCustomersByAreaservicedIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where areaserviced equals to DEFAULT_AREASERVICED
        defaultCustomerShouldBeFound("areaserviced.equals=" + DEFAULT_AREASERVICED);

        // Get all the customerList where areaserviced equals to UPDATED_AREASERVICED
        defaultCustomerShouldNotBeFound("areaserviced.equals=" + UPDATED_AREASERVICED);
    }

    @Test
    @Transactional
    public void getAllCustomersByAreaservicedIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where areaserviced in DEFAULT_AREASERVICED or UPDATED_AREASERVICED
        defaultCustomerShouldBeFound("areaserviced.in=" + DEFAULT_AREASERVICED + "," + UPDATED_AREASERVICED);

        // Get all the customerList where areaserviced equals to UPDATED_AREASERVICED
        defaultCustomerShouldNotBeFound("areaserviced.in=" + UPDATED_AREASERVICED);
    }

    @Test
    @Transactional
    public void getAllCustomersByAreaservicedIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where areaserviced is not null
        defaultCustomerShouldBeFound("areaserviced.specified=true");

        // Get all the customerList where areaserviced is null
        defaultCustomerShouldNotBeFound("areaserviced.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersBySpecialitiesIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where specialities equals to DEFAULT_SPECIALITIES
        defaultCustomerShouldBeFound("specialities.equals=" + DEFAULT_SPECIALITIES);

        // Get all the customerList where specialities equals to UPDATED_SPECIALITIES
        defaultCustomerShouldNotBeFound("specialities.equals=" + UPDATED_SPECIALITIES);
    }

    @Test
    @Transactional
    public void getAllCustomersBySpecialitiesIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where specialities in DEFAULT_SPECIALITIES or UPDATED_SPECIALITIES
        defaultCustomerShouldBeFound("specialities.in=" + DEFAULT_SPECIALITIES + "," + UPDATED_SPECIALITIES);

        // Get all the customerList where specialities equals to UPDATED_SPECIALITIES
        defaultCustomerShouldNotBeFound("specialities.in=" + UPDATED_SPECIALITIES);
    }

    @Test
    @Transactional
    public void getAllCustomersBySpecialitiesIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where specialities is not null
        defaultCustomerShouldBeFound("specialities.specified=true");

        // Get all the customerList where specialities is null
        defaultCustomerShouldNotBeFound("specialities.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByTradesIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where trades equals to DEFAULT_TRADES
        defaultCustomerShouldBeFound("trades.equals=" + DEFAULT_TRADES);

        // Get all the customerList where trades equals to UPDATED_TRADES
        defaultCustomerShouldNotBeFound("trades.equals=" + UPDATED_TRADES);
    }

    @Test
    @Transactional
    public void getAllCustomersByTradesIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where trades in DEFAULT_TRADES or UPDATED_TRADES
        defaultCustomerShouldBeFound("trades.in=" + DEFAULT_TRADES + "," + UPDATED_TRADES);

        // Get all the customerList where trades equals to UPDATED_TRADES
        defaultCustomerShouldNotBeFound("trades.in=" + UPDATED_TRADES);
    }

    @Test
    @Transactional
    public void getAllCustomersByTradesIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where trades is not null
        defaultCustomerShouldBeFound("trades.specified=true");

        // Get all the customerList where trades is null
        defaultCustomerShouldNotBeFound("trades.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByMonthYearIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where monthYear equals to DEFAULT_MONTH_YEAR
        defaultCustomerShouldBeFound("monthYear.equals=" + DEFAULT_MONTH_YEAR);

        // Get all the customerList where monthYear equals to UPDATED_MONTH_YEAR
        defaultCustomerShouldNotBeFound("monthYear.equals=" + UPDATED_MONTH_YEAR);
    }

    @Test
    @Transactional
    public void getAllCustomersByMonthYearIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where monthYear in DEFAULT_MONTH_YEAR or UPDATED_MONTH_YEAR
        defaultCustomerShouldBeFound("monthYear.in=" + DEFAULT_MONTH_YEAR + "," + UPDATED_MONTH_YEAR);

        // Get all the customerList where monthYear equals to UPDATED_MONTH_YEAR
        defaultCustomerShouldNotBeFound("monthYear.in=" + UPDATED_MONTH_YEAR);
    }

    @Test
    @Transactional
    public void getAllCustomersByMonthYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where monthYear is not null
        defaultCustomerShouldBeFound("monthYear.specified=true");

        // Get all the customerList where monthYear is null
        defaultCustomerShouldNotBeFound("monthYear.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLicenseNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where licenseNumber equals to DEFAULT_LICENSE_NUMBER
        defaultCustomerShouldBeFound("licenseNumber.equals=" + DEFAULT_LICENSE_NUMBER);

        // Get all the customerList where licenseNumber equals to UPDATED_LICENSE_NUMBER
        defaultCustomerShouldNotBeFound("licenseNumber.equals=" + UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomersByLicenseNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where licenseNumber in DEFAULT_LICENSE_NUMBER or UPDATED_LICENSE_NUMBER
        defaultCustomerShouldBeFound("licenseNumber.in=" + DEFAULT_LICENSE_NUMBER + "," + UPDATED_LICENSE_NUMBER);

        // Get all the customerList where licenseNumber equals to UPDATED_LICENSE_NUMBER
        defaultCustomerShouldNotBeFound("licenseNumber.in=" + UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCustomersByLicenseNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where licenseNumber is not null
        defaultCustomerShouldBeFound("licenseNumber.specified=true");

        // Get all the customerList where licenseNumber is null
        defaultCustomerShouldNotBeFound("licenseNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByShowIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where show equals to DEFAULT_SHOW
        defaultCustomerShouldBeFound("show.equals=" + DEFAULT_SHOW);

        // Get all the customerList where show equals to UPDATED_SHOW
        defaultCustomerShouldNotBeFound("show.equals=" + UPDATED_SHOW);
    }

    @Test
    @Transactional
    public void getAllCustomersByShowIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where show in DEFAULT_SHOW or UPDATED_SHOW
        defaultCustomerShouldBeFound("show.in=" + DEFAULT_SHOW + "," + UPDATED_SHOW);

        // Get all the customerList where show equals to UPDATED_SHOW
        defaultCustomerShouldNotBeFound("show.in=" + UPDATED_SHOW);
    }

    @Test
    @Transactional
    public void getAllCustomersByShowIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where show is not null
        defaultCustomerShouldBeFound("show.specified=true");

        // Get all the customerList where show is null
        defaultCustomerShouldNotBeFound("show.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        customer.setCompany(company);
        customerRepository.saveAndFlush(customer);
        Long companyId = company.getId();

        // Get all the customerList where company equals to companyId
        defaultCustomerShouldBeFound("companyId.equals=" + companyId);

        // Get all the customerList where company equals to companyId + 1
        defaultCustomerShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }


    @Test
    @Transactional
    public void getAllCustomersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        customer.setUser(user);
        customerRepository.saveAndFlush(customer);
        Long userId = user.getId();

        // Get all the customerList where user equals to userId
        defaultCustomerShouldBeFound("userId.equals=" + userId);

        // Get all the customerList where user equals to userId + 1
        defaultCustomerShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].normalized").value(hasItem(DEFAULT_NORMALIZED.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].streetaddress").value(hasItem(DEFAULT_STREETADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postalcode").value(hasItem(DEFAULT_POSTALCODE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].registered").value(hasItem(DEFAULT_REGISTERED.toString())))
            .andExpect(jsonPath("$.[*].lastactive").value(hasItem(DEFAULT_LASTACTIVE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].cycledate").value(hasItem(sameInstant(DEFAULT_CYCLEDATE))))
            .andExpect(jsonPath("$.[*].areaserviced").value(hasItem(DEFAULT_AREASERVICED.toString())))
            .andExpect(jsonPath("$.[*].specialities").value(hasItem(DEFAULT_SPECIALITIES.toString())))
            .andExpect(jsonPath("$.[*].trades").value(hasItem(DEFAULT_TRADES.toString())))
            .andExpect(jsonPath("$.[*].monthYear").value(hasItem(DEFAULT_MONTH_YEAR.toString())))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].show").value(hasItem(DEFAULT_SHOW.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCustomerSearchRepository);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .normalized(UPDATED_NORMALIZED)
            .phone(UPDATED_PHONE)
            .streetaddress(UPDATED_STREETADDRESS)
            .postalcode(UPDATED_POSTALCODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .country(UPDATED_COUNTRY)
            .registered(UPDATED_REGISTERED)
            .lastactive(UPDATED_LASTACTIVE)
            .points(UPDATED_POINTS)
            .cycledate(UPDATED_CYCLEDATE)
            .areaserviced(UPDATED_AREASERVICED)
            .specialities(UPDATED_SPECIALITIES)
            .trades(UPDATED_TRADES)
            .monthYear(UPDATED_MONTH_YEAR)
            .licenseNumber(UPDATED_LICENSE_NUMBER)
            .show(UPDATED_SHOW);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getNormalized()).isEqualTo(UPDATED_NORMALIZED);
        assertThat(testCustomer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomer.getStreetaddress()).isEqualTo(UPDATED_STREETADDRESS);
        assertThat(testCustomer.getPostalcode()).isEqualTo(UPDATED_POSTALCODE);
        assertThat(testCustomer.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCustomer.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testCustomer.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCustomer.getRegistered()).isEqualTo(UPDATED_REGISTERED);
        assertThat(testCustomer.getLastactive()).isEqualTo(UPDATED_LASTACTIVE);
        assertThat(testCustomer.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCustomer.getCycledate()).isEqualTo(UPDATED_CYCLEDATE);
        assertThat(testCustomer.getAreaserviced()).isEqualTo(UPDATED_AREASERVICED);
        assertThat(testCustomer.getSpecialities()).isEqualTo(UPDATED_SPECIALITIES);
        assertThat(testCustomer.getTrades()).isEqualTo(UPDATED_TRADES);
        assertThat(testCustomer.getMonthYear()).isEqualTo(UPDATED_MONTH_YEAR);
        assertThat(testCustomer.getLicenseNumber()).isEqualTo(UPDATED_LICENSE_NUMBER);
        assertThat(testCustomer.isShow()).isEqualTo(UPDATED_SHOW);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(1)).save(testCustomer);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(0)).save(customer);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Customer in Elasticsearch
        verify(mockCustomerSearchRepository, times(1)).deleteById(customer.getId());
    }

    @Test
    @Transactional
    public void searchCustomer() throws Exception {
        // Initialize the database
        customerService.save(customer);
        when(mockCustomerSearchRepository.search(queryStringQuery("id:" + customer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(customer), PageRequest.of(0, 1), 1));
        // Search the customer
        restCustomerMockMvc.perform(get("/api/_search/customers?query=id:" + customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].normalized").value(hasItem(DEFAULT_NORMALIZED.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].streetaddress").value(hasItem(DEFAULT_STREETADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postalcode").value(hasItem(DEFAULT_POSTALCODE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].registered").value(hasItem(DEFAULT_REGISTERED.toString())))
            .andExpect(jsonPath("$.[*].lastactive").value(hasItem(DEFAULT_LASTACTIVE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].cycledate").value(hasItem(sameInstant(DEFAULT_CYCLEDATE))))
            .andExpect(jsonPath("$.[*].areaserviced").value(hasItem(DEFAULT_AREASERVICED.toString())))
            .andExpect(jsonPath("$.[*].specialities").value(hasItem(DEFAULT_SPECIALITIES.toString())))
            .andExpect(jsonPath("$.[*].trades").value(hasItem(DEFAULT_TRADES.toString())))
            .andExpect(jsonPath("$.[*].monthYear").value(hasItem(DEFAULT_MONTH_YEAR.toString())))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].show").value(hasItem(DEFAULT_SHOW.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);
        customer2.setId(2L);
        assertThat(customer1).isNotEqualTo(customer2);
        customer1.setId(null);
        assertThat(customer1).isNotEqualTo(customer2);
    }
}
