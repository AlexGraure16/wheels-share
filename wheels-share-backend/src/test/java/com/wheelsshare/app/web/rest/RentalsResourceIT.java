package com.wheelsshare.app.web.rest;

import com.wheelsshare.app.WheelsShareApp;
import com.wheelsshare.app.domain.Rentals;
import com.wheelsshare.app.repository.RentalsRepository;
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
 * Integration tests for the {@link RentalsResource} REST controller.
 */
@SpringBootTest(classes = WheelsShareApp.class)
public class RentalsResourceIT {

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
    private RentalsRepository rentalsRepository;

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

    private MockMvc restRentalsMockMvc;

    private Rentals rentals;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RentalsResource rentalsResource = new RentalsResource(rentalsRepository);
        this.restRentalsMockMvc = MockMvcBuilders.standaloneSetup(rentalsResource)
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
    public static Rentals createEntity(EntityManager em) {
        Rentals rentals = new Rentals()
            .carId(DEFAULT_CAR_ID)
            .userEmailAddress(DEFAULT_USER_EMAIL_ADDRESS)
            .rentPeriod(DEFAULT_RENT_PERIOD)
            .price(DEFAULT_PRICE)
            .ongoing(DEFAULT_ONGOING);
        return rentals;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rentals createUpdatedEntity(EntityManager em) {
        Rentals rentals = new Rentals()
            .carId(UPDATED_CAR_ID)
            .userEmailAddress(UPDATED_USER_EMAIL_ADDRESS)
            .rentPeriod(UPDATED_RENT_PERIOD)
            .price(UPDATED_PRICE)
            .ongoing(UPDATED_ONGOING);
        return rentals;
    }

    @BeforeEach
    public void initTest() {
        rentals = createEntity(em);
    }

    @Test
    @Transactional
    public void createRentals() throws Exception {
        int databaseSizeBeforeCreate = rentalsRepository.findAll().size();

        // Create the Rentals
        restRentalsMockMvc.perform(post("/api/rentals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rentals)))
            .andExpect(status().isCreated());

        // Validate the Rentals in the database
        List<Rentals> rentalsList = rentalsRepository.findAll();
        assertThat(rentalsList).hasSize(databaseSizeBeforeCreate + 1);
        Rentals testRentals = rentalsList.get(rentalsList.size() - 1);
        assertThat(testRentals.getCarId()).isEqualTo(DEFAULT_CAR_ID);
        assertThat(testRentals.getUserEmailAddress()).isEqualTo(DEFAULT_USER_EMAIL_ADDRESS);
        assertThat(testRentals.getRentPeriod()).isEqualTo(DEFAULT_RENT_PERIOD);
        assertThat(testRentals.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRentals.isOngoing()).isEqualTo(DEFAULT_ONGOING);
    }

    @Test
    @Transactional
    public void createRentalsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rentalsRepository.findAll().size();

        // Create the Rentals with an existing ID
        rentals.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalsMockMvc.perform(post("/api/rentals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rentals)))
            .andExpect(status().isBadRequest());

        // Validate the Rentals in the database
        List<Rentals> rentalsList = rentalsRepository.findAll();
        assertThat(rentalsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCarIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalsRepository.findAll().size();
        // set the field null
        rentals.setCarId(null);

        // Create the Rentals, which fails.

        restRentalsMockMvc.perform(post("/api/rentals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rentals)))
            .andExpect(status().isBadRequest());

        List<Rentals> rentalsList = rentalsRepository.findAll();
        assertThat(rentalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalsRepository.findAll().size();
        // set the field null
        rentals.setUserEmailAddress(null);

        // Create the Rentals, which fails.

        restRentalsMockMvc.perform(post("/api/rentals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rentals)))
            .andExpect(status().isBadRequest());

        List<Rentals> rentalsList = rentalsRepository.findAll();
        assertThat(rentalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRentPeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalsRepository.findAll().size();
        // set the field null
        rentals.setRentPeriod(null);

        // Create the Rentals, which fails.

        restRentalsMockMvc.perform(post("/api/rentals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rentals)))
            .andExpect(status().isBadRequest());

        List<Rentals> rentalsList = rentalsRepository.findAll();
        assertThat(rentalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalsRepository.findAll().size();
        // set the field null
        rentals.setPrice(null);

        // Create the Rentals, which fails.

        restRentalsMockMvc.perform(post("/api/rentals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rentals)))
            .andExpect(status().isBadRequest());

        List<Rentals> rentalsList = rentalsRepository.findAll();
        assertThat(rentalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOngoingIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalsRepository.findAll().size();
        // set the field null
        rentals.setOngoing(null);

        // Create the Rentals, which fails.

        restRentalsMockMvc.perform(post("/api/rentals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rentals)))
            .andExpect(status().isBadRequest());

        List<Rentals> rentalsList = rentalsRepository.findAll();
        assertThat(rentalsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRentals() throws Exception {
        // Initialize the database
        rentalsRepository.saveAndFlush(rentals);

        // Get all the rentalsList
        restRentalsMockMvc.perform(get("/api/rentals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentals.getId().intValue())))
            .andExpect(jsonPath("$.[*].carId").value(hasItem(DEFAULT_CAR_ID.intValue())))
            .andExpect(jsonPath("$.[*].userEmailAddress").value(hasItem(DEFAULT_USER_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].rentPeriod").value(hasItem(DEFAULT_RENT_PERIOD)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].ongoing").value(hasItem(DEFAULT_ONGOING.booleanValue())));
    }

    @Test
    @Transactional
    public void getRentals() throws Exception {
        // Initialize the database
        rentalsRepository.saveAndFlush(rentals);

        // Get the rentals
        restRentalsMockMvc.perform(get("/api/rentals/{id}", rentals.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rentals.getId().intValue()))
            .andExpect(jsonPath("$.carId").value(DEFAULT_CAR_ID.intValue()))
            .andExpect(jsonPath("$.userEmailAddress").value(DEFAULT_USER_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.rentPeriod").value(DEFAULT_RENT_PERIOD))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.ongoing").value(DEFAULT_ONGOING.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRentals() throws Exception {
        // Get the rentals
        restRentalsMockMvc.perform(get("/api/rentals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRentals() throws Exception {
        // Initialize the database
        rentalsRepository.saveAndFlush(rentals);

        int databaseSizeBeforeUpdate = rentalsRepository.findAll().size();

        // Update the rentals
        Rentals updatedRentals = rentalsRepository.findById(rentals.getId()).get();
        // Disconnect from session so that the updates on updatedRentals are not directly saved in db
        em.detach(updatedRentals);
        updatedRentals
            .carId(UPDATED_CAR_ID)
            .userEmailAddress(UPDATED_USER_EMAIL_ADDRESS)
            .rentPeriod(UPDATED_RENT_PERIOD)
            .price(UPDATED_PRICE)
            .ongoing(UPDATED_ONGOING);

        restRentalsMockMvc.perform(put("/api/rentals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRentals)))
            .andExpect(status().isOk());

        // Validate the Rentals in the database
        List<Rentals> rentalsList = rentalsRepository.findAll();
        assertThat(rentalsList).hasSize(databaseSizeBeforeUpdate);
        Rentals testRentals = rentalsList.get(rentalsList.size() - 1);
        assertThat(testRentals.getCarId()).isEqualTo(UPDATED_CAR_ID);
        assertThat(testRentals.getUserEmailAddress()).isEqualTo(UPDATED_USER_EMAIL_ADDRESS);
        assertThat(testRentals.getRentPeriod()).isEqualTo(UPDATED_RENT_PERIOD);
        assertThat(testRentals.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRentals.isOngoing()).isEqualTo(UPDATED_ONGOING);
    }

    @Test
    @Transactional
    public void updateNonExistingRentals() throws Exception {
        int databaseSizeBeforeUpdate = rentalsRepository.findAll().size();

        // Create the Rentals

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalsMockMvc.perform(put("/api/rents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rentals)))
            .andExpect(status().isBadRequest());

        // Validate the Rentals in the database
        List<Rentals> rentsList = rentalsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRentals() throws Exception {
        // Initialize the database
        rentalsRepository.saveAndFlush(rentals);

        int databaseSizeBeforeDelete = rentalsRepository.findAll().size();

        // Delete the rents
        restRentalsMockMvc.perform(delete("/api/rents/{id}", rentals.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rentals> rentsList = rentalsRepository.findAll();
        assertThat(rentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
