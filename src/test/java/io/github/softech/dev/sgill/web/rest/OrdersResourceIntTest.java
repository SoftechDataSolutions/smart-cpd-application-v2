package io.github.softech.dev.sgill.web.rest;

import io.github.softech.dev.sgill.SmartCpdApp;

import io.github.softech.dev.sgill.domain.CourseHistory;
import io.github.softech.dev.sgill.domain.Orders;
import io.github.softech.dev.sgill.domain.Cart;
import io.github.softech.dev.sgill.repository.*;
import io.github.softech.dev.sgill.repository.search.OrdersSearchRepository;
import io.github.softech.dev.sgill.service.CourseHistoryService;
import io.github.softech.dev.sgill.service.CustomerService;
import io.github.softech.dev.sgill.service.OrdersService;
import io.github.softech.dev.sgill.web.rest.errors.ExceptionTranslator;
import io.github.softech.dev.sgill.service.dto.OrdersCriteria;
import io.github.softech.dev.sgill.service.OrdersQueryService;

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

import io.github.softech.dev.sgill.domain.enumeration.NOTIFICATIONS;
import io.github.softech.dev.sgill.domain.enumeration.PAYMENT;
/**
 * Test class for the OrdersResource REST controller.
 *
 * @see OrdersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartCpdApp.class)
public class OrdersResourceIntTest {

    private static final Instant DEFAULT_CREATEDDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final NOTIFICATIONS DEFAULT_STATUS = NOTIFICATIONS.ORDERPROCESSING;
    private static final NOTIFICATIONS UPDATED_STATUS = NOTIFICATIONS.COMPLETE;

    private static final PAYMENT DEFAULT_PAYMENT = PAYMENT.PAYPAL;
    private static final PAYMENT UPDATED_PAYMENT = PAYMENT.STRIPE;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CourseHistoryRepository courseHistoryRepository;

    @Autowired
    private CourseHistoryService courseHistoryService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseCartBridgeRepository courseCartBridgeRepository;

    @Autowired
    private OrdersService ordersService;

    /**
     * This repository is mocked in the io.github.softech.dev.sgill.repository.search test package.
     *
     * @see io.github.softech.dev.sgill.repository.search.OrdersSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrdersSearchRepository mockOrdersSearchRepository;

    @Autowired
    private OrdersQueryService ordersQueryService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrdersMockMvc;

    private Orders orders;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrdersResource ordersResource = new OrdersResource(ordersService, ordersQueryService, courseHistoryRepository,
            courseCartBridgeRepository, courseHistoryService, courseRepository, customerRepository, customerService);
        this.restOrdersMockMvc = MockMvcBuilders.standaloneSetup(ordersResource)
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
    public static Orders createEntity(EntityManager em) {
        Orders orders = new Orders()
            .createddate(DEFAULT_CREATEDDATE)
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS)
            .payment(DEFAULT_PAYMENT);
        return orders;
    }

    @Before
    public void initTest() {
        orders = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrders() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders
        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate + 1);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getCreateddate()).isEqualTo(DEFAULT_CREATEDDATE);
        assertThat(testOrders.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testOrders.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrders.getPayment()).isEqualTo(DEFAULT_PAYMENT);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(1)).save(testOrders);
    }

    @Test
    @Transactional
    public void createOrdersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders with an existing ID
        orders.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(0)).save(orders);
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList
        restOrdersMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].createddate").value(hasItem(DEFAULT_CREATEDDATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.toString())));
    }
    

    @Test
    @Transactional
    public void getOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orders.getId().intValue()))
            .andExpect(jsonPath("$.createddate").value(DEFAULT_CREATEDDATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.payment").value(DEFAULT_PAYMENT.toString()));
    }

    @Test
    @Transactional
    public void getAllOrdersByCreateddateIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where createddate equals to DEFAULT_CREATEDDATE
        defaultOrdersShouldBeFound("createddate.equals=" + DEFAULT_CREATEDDATE);

        // Get all the ordersList where createddate equals to UPDATED_CREATEDDATE
        defaultOrdersShouldNotBeFound("createddate.equals=" + UPDATED_CREATEDDATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByCreateddateIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where createddate in DEFAULT_CREATEDDATE or UPDATED_CREATEDDATE
        defaultOrdersShouldBeFound("createddate.in=" + DEFAULT_CREATEDDATE + "," + UPDATED_CREATEDDATE);

        // Get all the ordersList where createddate equals to UPDATED_CREATEDDATE
        defaultOrdersShouldNotBeFound("createddate.in=" + UPDATED_CREATEDDATE);
    }

    @Test
    @Transactional
    public void getAllOrdersByCreateddateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where createddate is not null
        defaultOrdersShouldBeFound("createddate.specified=true");

        // Get all the ordersList where createddate is null
        defaultOrdersShouldNotBeFound("createddate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where amount equals to DEFAULT_AMOUNT
        defaultOrdersShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the ordersList where amount equals to UPDATED_AMOUNT
        defaultOrdersShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdersByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultOrdersShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the ordersList where amount equals to UPDATED_AMOUNT
        defaultOrdersShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllOrdersByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where amount is not null
        defaultOrdersShouldBeFound("amount.specified=true");

        // Get all the ordersList where amount is null
        defaultOrdersShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status equals to DEFAULT_STATUS
        defaultOrdersShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the ordersList where status equals to UPDATED_STATUS
        defaultOrdersShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOrdersShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the ordersList where status equals to UPDATED_STATUS
        defaultOrdersShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where status is not null
        defaultOrdersShouldBeFound("status.specified=true");

        // Get all the ordersList where status is null
        defaultOrdersShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where payment equals to DEFAULT_PAYMENT
        defaultOrdersShouldBeFound("payment.equals=" + DEFAULT_PAYMENT);

        // Get all the ordersList where payment equals to UPDATED_PAYMENT
        defaultOrdersShouldNotBeFound("payment.equals=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllOrdersByPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where payment in DEFAULT_PAYMENT or UPDATED_PAYMENT
        defaultOrdersShouldBeFound("payment.in=" + DEFAULT_PAYMENT + "," + UPDATED_PAYMENT);

        // Get all the ordersList where payment equals to UPDATED_PAYMENT
        defaultOrdersShouldNotBeFound("payment.in=" + UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllOrdersByPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the ordersList where payment is not null
        defaultOrdersShouldBeFound("payment.specified=true");

        // Get all the ordersList where payment is null
        defaultOrdersShouldNotBeFound("payment.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrdersByCartIsEqualToSomething() throws Exception {
        // Initialize the database
        Cart cart = CartResourceIntTest.createEntity(em);
        em.persist(cart);
        em.flush();
        orders.setCart(cart);
        ordersRepository.saveAndFlush(orders);
        Long cartId = cart.getId();

        // Get all the ordersList where cart equals to cartId
        defaultOrdersShouldBeFound("cartId.equals=" + cartId);

        // Get all the ordersList where cart equals to cartId + 1
        defaultOrdersShouldNotBeFound("cartId.equals=" + (cartId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrdersShouldBeFound(String filter) throws Exception {
        restOrdersMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].createddate").value(hasItem(DEFAULT_CREATEDDATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrdersShouldNotBeFound(String filter) throws Exception {
        restOrdersMockMvc.perform(get("/api/orders?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingOrders() throws Exception {
        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrders() throws Exception {
        // Initialize the database
        ordersService.save(orders);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockOrdersSearchRepository);

        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders
        Orders updatedOrders = ordersRepository.findById(orders.getId()).get();
        // Disconnect from session so that the updates on updatedOrders are not directly saved in db
        em.detach(updatedOrders);
        updatedOrders
            .createddate(UPDATED_CREATEDDATE)
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .payment(UPDATED_PAYMENT);

        restOrdersMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrders)))
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getCreateddate()).isEqualTo(UPDATED_CREATEDDATE);
        assertThat(testOrders.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testOrders.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrders.getPayment()).isEqualTo(UPDATED_PAYMENT);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(1)).save(testOrders);
    }

    @Test
    @Transactional
    public void updateNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Create the Orders

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restOrdersMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(0)).save(orders);
    }

    @Test
    @Transactional
    public void deleteOrders() throws Exception {
        // Initialize the database
        ordersService.save(orders);

        int databaseSizeBeforeDelete = ordersRepository.findAll().size();

        // Get the orders
        restOrdersMockMvc.perform(delete("/api/orders/{id}", orders.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Orders> ordersList = ordersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Orders in Elasticsearch
        verify(mockOrdersSearchRepository, times(1)).deleteById(orders.getId());
    }

    @Test
    @Transactional
    public void searchOrders() throws Exception {
        // Initialize the database
        ordersService.save(orders);
        when(mockOrdersSearchRepository.search(queryStringQuery("id:" + orders.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(orders), PageRequest.of(0, 1), 1));
        // Search the orders
        restOrdersMockMvc.perform(get("/api/_search/orders?query=id:" + orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].createddate").value(hasItem(DEFAULT_CREATEDDATE.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Orders.class);
        Orders orders1 = new Orders();
        orders1.setId(1L);
        Orders orders2 = new Orders();
        orders2.setId(orders1.getId());
        assertThat(orders1).isEqualTo(orders2);
        orders2.setId(2L);
        assertThat(orders1).isNotEqualTo(orders2);
        orders1.setId(null);
        assertThat(orders1).isNotEqualTo(orders2);
    }
}
