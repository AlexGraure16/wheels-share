package com.wheelsshare.app.web.rest;

import com.wheelsshare.app.WheelsShareApp;
import com.wheelsshare.app.domain.Rents;
import com.wheelsshare.app.repository.RentsRepository;
import com.wheelsshare.app.web.rest.errors.ExceptionTranslator;
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

import static com.wheelsshare.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RentsResource} REST controller.
 */
@SpringBootTest(classes = WheelsShareApp.class)
public class RentsResourceIT {

    private static final Long DEFAULT_CAR_ID = 1L;
    private static final Long UPDATED_CAR_ID = 2L;

    private static final String DEFAULT_USER_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_RENT_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_RENT_PERIOD = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Boolean DEFAULT_ONGOING = false;
    private static final Boolean UPDATED_ONGOING = true;

    @Autowired
    private RentsRepository rentsRepository;

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

    private MockMvc restRentsMockMvc;

    private Rents rents;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RentsResource rentsResource = new RentsResource(rentsRepository);
        this.restRentsMockMvc = MockMvcBuilders.standaloneSetup(rentsResource)
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
    public static Rents createEntity(EntityManager em) {
        Rents rents = new Rents()
            .carId(DEFAULT_CAR_ID)
            .userEmailAddress(DEFAULT_USER_EMAIL_ADDRESS)
            .rentPeriod(DEFAULT_RENT_PERIOD)
            .price(DEFAULT_PRICE)
            .ongoing(DEFAULT_ONGOING);
        return rents;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rents createUpdatedEntity(EntityManager em) {
        Rents rents = new Rents()
            .carId(UPDATED_CAR_ID)
            .userEmailAddress(UPDATED_USER_EMAIL_ADDRESS)
            .rentPeriod(UPDATED_RENT_PERIOD)
            .price(UPDATED_PRICE)
            .ongoing(UPDATED_ONGOING);
        return rents;
    }

    @BeforeEach
    public void initTest() {
        rents = createEntity(em);
    }

    @Test
    @Transactional
    public void createRents() throws Exception {
        int databaseSizeBeforeCreate = rentsRepository.findAll().size();

        // Create the Rents
        restRentsMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rents)))
            .andExpect(status().isCreated());

        // Validate the Rents in the database
        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeCreate + 1);
        Rents testRents = rentsList.get(rentsList.size() - 1);
        assertThat(testRents.getCarId()).isEqualTo(DEFAULT_CAR_ID);
        assertThat(testRents.getUserEmailAddress()).isEqualTo(DEFAULT_USER_EMAIL_ADDRESS);
        assertThat(testRents.getRentPeriod()).isEqualTo(DEFAULT_RENT_PERIOD);
        assertThat(testRents.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRents.isOngoing()).isEqualTo(DEFAULT_ONGOING);
    }

    @Test
    @Transactional
    public void createRentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rentsRepository.findAll().size();

        // Create the Rents with an existing ID
        rents.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentsMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rents)))
            .andExpect(status().isBadRequest());

        // Validate the Rents in the database
        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCarIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentsRepository.findAll().size();
        // set the field null
        rents.setCarId(null);

        // Create the Rents, which fails.

        restRentsMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rents)))
            .andExpect(status().isBadRequest());

        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentsRepository.findAll().size();
        // set the field null
        rents.setUserEmailAddress(null);

        // Create the Rents, which fails.

        restRentsMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rents)))
            .andExpect(status().isBadRequest());

        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRentPeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentsRepository.findAll().size();
        // set the field null
        rents.setRentPeriod(null);

        // Create the Rents, which fails.

        restRentsMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rents)))
            .andExpect(status().isBadRequest());

        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentsRepository.findAll().size();
        // set the field null
        rents.setPrice(null);

        // Create the Rents, which fails.

        restRentsMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rents)))
            .andExpect(status().isBadRequest());

        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOngoingIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentsRepository.findAll().size();
        // set the field null
        rents.setOngoing(null);

        // Create the Rents, which fails.

        restRentsMockMvc.perform(post("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rents)))
            .andExpect(status().isBadRequest());

        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRents() throws Exception {
        // Initialize the database
        rentsRepository.saveAndFlush(rents);

        // Get all the rentsList
        restRentsMockMvc.perform(get("/api/rents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rents.getId().intValue())))
            .andExpect(jsonPath("$.[*].carId").value(hasItem(DEFAULT_CAR_ID.intValue())))
            .andExpect(jsonPath("$.[*].userEmailAddress").value(hasItem(DEFAULT_USER_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].rentPeriod").value(hasItem(DEFAULT_RENT_PERIOD)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].ongoing").value(hasItem(DEFAULT_ONGOING.booleanValue())));
    }

    @Test
    @Transactional
    public void getRents() throws Exception {
        // Initialize the database
        rentsRepository.saveAndFlush(rents);

        // Get the rents
        restRentsMockMvc.perform(get("/api/rents/{id}", rents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rents.getId().intValue()))
            .andExpect(jsonPath("$.carId").value(DEFAULT_CAR_ID.intValue()))
            .andExpect(jsonPath("$.userEmailAddress").value(DEFAULT_USER_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.rentPeriod").value(DEFAULT_RENT_PERIOD))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.ongoing").value(DEFAULT_ONGOING.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRents() throws Exception {
        // Get the rents
        restRentsMockMvc.perform(get("/api/rents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRents() throws Exception {
        // Initialize the database
        rentsRepository.saveAndFlush(rents);

        int databaseSizeBeforeUpdate = rentsRepository.findAll().size();

        // Update the rents
        Rents updatedRents = rentsRepository.findById(rents.getId()).get();
        // Disconnect from session so that the updates on updatedRents are not directly saved in db
        em.detach(updatedRents);
        updatedRents
            .carId(UPDATED_CAR_ID)
            .userEmailAddress(UPDATED_USER_EMAIL_ADDRESS)
            .rentPeriod(UPDATED_RENT_PERIOD)
            .price(UPDATED_PRICE)
            .ongoing(UPDATED_ONGOING);

        restRentsMockMvc.perform(put("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRents)))
            .andExpect(status().isOk());

        // Validate the Rents in the database
        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeUpdate);
        Rents testRents = rentsList.get(rentsList.size() - 1);
        assertThat(testRents.getCarId()).isEqualTo(UPDATED_CAR_ID);
        assertThat(testRents.getUserEmailAddress()).isEqualTo(UPDATED_USER_EMAIL_ADDRESS);
        assertThat(testRents.getRentPeriod()).isEqualTo(UPDATED_RENT_PERIOD);
        assertThat(testRents.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRents.isOngoing()).isEqualTo(UPDATED_ONGOING);
    }

    @Test
    @Transactional
    public void updateNonExistingRents() throws Exception {
        int databaseSizeBeforeUpdate = rentsRepository.findAll().size();

        // Create the Rents

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentsMockMvc.perform(put("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rents)))
            .andExpect(status().isBadRequest());

        // Validate the Rents in the database
        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRents() throws Exception {
        // Initialize the database
        rentsRepository.saveAndFlush(rents);

        int databaseSizeBeforeDelete = rentsRepository.findAll().size();

        // Delete the rents
        restRentsMockMvc.perform(delete("/api/rents/{id}", rents.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rents> rentsList = rentsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
