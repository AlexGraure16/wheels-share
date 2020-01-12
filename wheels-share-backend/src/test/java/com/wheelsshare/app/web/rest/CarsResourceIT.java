package com.wheelsshare.app.web.rest;

import com.wheelsshare.app.WheelsShareApp;
import com.wheelsshare.app.domain.Cars;
import com.wheelsshare.app.domain.enumeration.Fuel;
import com.wheelsshare.app.repository.CarsRepository;
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
 * Integration tests for the {@link CarsResource} REST controller.
 */
@SpringBootTest(classes = WheelsShareApp.class)
public class CarsResourceIT {

    private static final String DEFAULT_NAME = "BMW";
    private static final String UPDATED_NAME = "BMW";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO = "https://ro.wikipedia.org/wiki/BMW_X5#/media/Fi%C8%99ier:2019_BMW_X5_M50d_Automatic_3.0.jpg";
    private static final String UPDATED_PHOTO = "https://ro.wikipedia.org/wiki/BMW_X5#/media/Fi%C8%99ier:2019_BMW_X5_M50d_Automatic_3.0.jpg";

    private static final Boolean DEFAULT_AIR_CONDITIONING = false;
    private static final Boolean UPDATED_AIR_CONDITIONING = true;

    private static final Boolean DEFAULT_RADIO = false;
    private static final Boolean UPDATED_RADIO = true;

    private static final Boolean DEFAULT_ABS = false;
    private static final Boolean UPDATED_ABS = true;

    private static final Boolean DEFAULT_ELECTRIC_WINDOWS = false;
    private static final Boolean UPDATED_ELECTRIC_WINDOWS = true;

    private static final Boolean DEFAULT_CENTRAL_LOCKING = false;
    private static final Boolean UPDATED_CENTRAL_LOCKING = true;

    private static final Boolean DEFAULT_BIG_TRUNK = false;
    private static final Boolean UPDATED_BIG_TRUNK = true;

    private static final Boolean DEFAULT_FUEL_EFFICIENCY = false;
    private static final Boolean UPDATED_FUEL_EFFICIENCY = true;

    private static final Boolean DEFAULT_FAMILY_SIZE = false;
    private static final Boolean UPDATED_FAMILY_SIZE = true;

    private static final Boolean DEFAULT_AUTOMATIC_GEAR_BOX = false;
    private static final Boolean UPDATED_AUTOMATIC_GEAR_BOX = true;

    private static final Integer DEFAULT_SEATS_NUMBER = 1;
    private static final Integer UPDATED_SEATS_NUMBER = 2;

    private static final Fuel DEFAULT_FUEL = Fuel.DIESEL;
    private static final Fuel UPDATED_FUEL = Fuel.GAS;

    private static final Double DEFAULT_PRICE_PER_DAY = 1D;
    private static final Double UPDATED_PRICE_PER_DAY = 2D;

    @Autowired
    private CarsRepository carsRepository;

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

    private MockMvc restCarsMockMvc;

    private Cars cars;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarsResource carsResource = new CarsResource(carsRepository);
        this.restCarsMockMvc = MockMvcBuilders.standaloneSetup(carsResource)
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
    public static Cars createEntity(EntityManager em) {
        Cars cars = new Cars()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .photo(DEFAULT_PHOTO)
            .airConditioning(DEFAULT_AIR_CONDITIONING)
            .radio(DEFAULT_RADIO)
            .abs(DEFAULT_ABS)
            .electricWindows(DEFAULT_ELECTRIC_WINDOWS)
            .centralLocking(DEFAULT_CENTRAL_LOCKING)
            .bigTrunk(DEFAULT_BIG_TRUNK)
            .fuelEfficiency(DEFAULT_FUEL_EFFICIENCY)
            .familySize(DEFAULT_FAMILY_SIZE)
            .automaticGearBox(DEFAULT_AUTOMATIC_GEAR_BOX)
            .seatsNumber(DEFAULT_SEATS_NUMBER)
            .fuel(DEFAULT_FUEL)
            .pricePerDay(DEFAULT_PRICE_PER_DAY);
        return cars;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cars createUpdatedEntity(EntityManager em) {
        Cars cars = new Cars()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .photo(UPDATED_PHOTO)
            .airConditioning(UPDATED_AIR_CONDITIONING)
            .radio(UPDATED_RADIO)
            .abs(UPDATED_ABS)
            .electricWindows(UPDATED_ELECTRIC_WINDOWS)
            .centralLocking(UPDATED_CENTRAL_LOCKING)
            .bigTrunk(UPDATED_BIG_TRUNK)
            .fuelEfficiency(UPDATED_FUEL_EFFICIENCY)
            .familySize(UPDATED_FAMILY_SIZE)
            .automaticGearBox(UPDATED_AUTOMATIC_GEAR_BOX)
            .seatsNumber(UPDATED_SEATS_NUMBER)
            .fuel(UPDATED_FUEL)
            .pricePerDay(UPDATED_PRICE_PER_DAY);
        return cars;
    }

    @BeforeEach
    public void initTest() {
        cars = createEntity(em);
    }

    @Test
    @Transactional
    public void createCars() throws Exception {
        int databaseSizeBeforeCreate = carsRepository.findAll().size();

        // Create the Cars
        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isCreated());

        // Validate the Cars in the database
        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeCreate + 1);
        Cars testCars = carsList.get(carsList.size() - 1);
        assertThat(testCars.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCars.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCars.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testCars.isAirConditioning()).isEqualTo(DEFAULT_AIR_CONDITIONING);
        assertThat(testCars.isRadio()).isEqualTo(DEFAULT_RADIO);
        assertThat(testCars.isAbs()).isEqualTo(DEFAULT_ABS);
        assertThat(testCars.isElectricWindows()).isEqualTo(DEFAULT_ELECTRIC_WINDOWS);
        assertThat(testCars.isCentralLocking()).isEqualTo(DEFAULT_CENTRAL_LOCKING);
        assertThat(testCars.isBigTrunk()).isEqualTo(DEFAULT_BIG_TRUNK);
        assertThat(testCars.isFuelEfficiency()).isEqualTo(DEFAULT_FUEL_EFFICIENCY);
        assertThat(testCars.isFamilySize()).isEqualTo(DEFAULT_FAMILY_SIZE);
        assertThat(testCars.isAutomaticGearBox()).isEqualTo(DEFAULT_AUTOMATIC_GEAR_BOX);
        assertThat(testCars.getSeatsNumber()).isEqualTo(DEFAULT_SEATS_NUMBER);
        assertThat(testCars.getFuel()).isEqualTo(DEFAULT_FUEL);
        assertThat(testCars.getPricePerDay()).isEqualTo(DEFAULT_PRICE_PER_DAY);
    }

    @Test
    @Transactional
    public void createCarsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carsRepository.findAll().size();

        // Create the Cars with an existing ID
        cars.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        // Validate the Cars in the database
        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setName(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setDescription(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAirConditioningIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setAirConditioning(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRadioIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setRadio(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAbsIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setAbs(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkElectricWindowsIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setElectricWindows(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCentralLockingIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setCentralLocking(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBigTrunkIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setBigTrunk(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFuelEfficiencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setFuelEfficiency(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFamilySizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setFamilySize(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAutomaticGearBoxIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setAutomaticGearBox(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSeatsNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setSeatsNumber(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFuelIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setFuel(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPricePerDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = carsRepository.findAll().size();
        // set the field null
        cars.setPricePerDay(null);

        // Create the Cars, which fails.

        restCarsMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCars() throws Exception {
        // Initialize the database
        carsRepository.saveAndFlush(cars);

        // Get all the carsList
        restCarsMockMvc.perform(get("/api/cars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cars.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].airConditioning").value(hasItem(DEFAULT_AIR_CONDITIONING)))
            .andExpect(jsonPath("$.[*].radio").value(hasItem(DEFAULT_RADIO)))
            .andExpect(jsonPath("$.[*].abs").value(hasItem(DEFAULT_ABS)))
            .andExpect(jsonPath("$.[*].electricWindows").value(hasItem(DEFAULT_ELECTRIC_WINDOWS)))
            .andExpect(jsonPath("$.[*].centralLocking").value(hasItem(DEFAULT_CENTRAL_LOCKING)))
            .andExpect(jsonPath("$.[*].bigTrunk").value(hasItem(DEFAULT_BIG_TRUNK)))
            .andExpect(jsonPath("$.[*].fuelEfficiency").value(hasItem(DEFAULT_FUEL_EFFICIENCY)))
            .andExpect(jsonPath("$.[*].familySize").value(hasItem(DEFAULT_FAMILY_SIZE)))
            .andExpect(jsonPath("$.[*].automaticGearBox").value(hasItem(DEFAULT_AUTOMATIC_GEAR_BOX)))
            .andExpect(jsonPath("$.[*].seatsNumber").value(hasItem(DEFAULT_SEATS_NUMBER)))
            .andExpect(jsonPath("$.[*].fuel").value(hasItem(DEFAULT_FUEL.toString())))
            .andExpect(jsonPath("$.[*].pricePerDay").value(hasItem(DEFAULT_PRICE_PER_DAY)));
    }

    @Test
    @Transactional
    public void getCars() throws Exception {
        // Initialize the database
        carsRepository.saveAndFlush(cars);

        // Get the cars
        restCarsMockMvc.perform(get("/api/cars/{id}", cars.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cars.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO))
            .andExpect(jsonPath("$.airConditioning").value(DEFAULT_AIR_CONDITIONING))
            .andExpect(jsonPath("$.radio").value(DEFAULT_RADIO))
            .andExpect(jsonPath("$.abs").value(DEFAULT_ABS))
            .andExpect(jsonPath("$.electricWindows").value(DEFAULT_ELECTRIC_WINDOWS))
            .andExpect(jsonPath("$.centralLocking").value(DEFAULT_CENTRAL_LOCKING))
            .andExpect(jsonPath("$.bigTrunk").value(DEFAULT_BIG_TRUNK))
            .andExpect(jsonPath("$.fuelEfficiency").value(DEFAULT_FUEL_EFFICIENCY))
            .andExpect(jsonPath("$.familySize").value(DEFAULT_FAMILY_SIZE))
            .andExpect(jsonPath("$.automaticGearBox").value(DEFAULT_AUTOMATIC_GEAR_BOX))
            .andExpect(jsonPath("$.seatsNumber").value(DEFAULT_SEATS_NUMBER))
            .andExpect(jsonPath("$.fuel").value(DEFAULT_FUEL.toString()))
            .andExpect(jsonPath("$.pricePerDay").value(DEFAULT_PRICE_PER_DAY));
    }

    @Test
    @Transactional
    public void getNonExistingCars() throws Exception {
        // Get the cars
        restCarsMockMvc.perform(get("/api/cars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCars() throws Exception {
        // Initialize the database
        carsRepository.saveAndFlush(cars);

        int databaseSizeBeforeUpdate = carsRepository.findAll().size();

        // Update the cars
        Cars updatedCars = carsRepository.findById(cars.getId()).get();
        // Disconnect from session so that the updates on updatedCars are not directly saved in db
        em.detach(updatedCars);
        updatedCars
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .photo(UPDATED_PHOTO)
            .airConditioning(UPDATED_AIR_CONDITIONING)
            .radio(UPDATED_RADIO)
            .abs(UPDATED_ABS)
            .electricWindows(UPDATED_ELECTRIC_WINDOWS)
            .centralLocking(UPDATED_CENTRAL_LOCKING)
            .bigTrunk(UPDATED_BIG_TRUNK)
            .fuelEfficiency(UPDATED_FUEL_EFFICIENCY)
            .familySize(UPDATED_FAMILY_SIZE)
            .automaticGearBox(UPDATED_AUTOMATIC_GEAR_BOX)
            .seatsNumber(UPDATED_SEATS_NUMBER)
            .fuel(UPDATED_FUEL)
            .pricePerDay(UPDATED_PRICE_PER_DAY);

        restCarsMockMvc.perform(put("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCars)))
            .andExpect(status().isOk());

        // Validate the Cars in the database
        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeUpdate);
        Cars testCars = carsList.get(carsList.size() - 1);
        assertThat(testCars.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCars.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCars.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCars.isAirConditioning()).isEqualTo(UPDATED_AIR_CONDITIONING);
        assertThat(testCars.isRadio()).isEqualTo(UPDATED_RADIO);
        assertThat(testCars.isAbs()).isEqualTo(UPDATED_ABS);
        assertThat(testCars.isElectricWindows()).isEqualTo(UPDATED_ELECTRIC_WINDOWS);
        assertThat(testCars.isCentralLocking()).isEqualTo(UPDATED_CENTRAL_LOCKING);
        assertThat(testCars.isBigTrunk()).isEqualTo(UPDATED_BIG_TRUNK);
        assertThat(testCars.isFuelEfficiency()).isEqualTo(UPDATED_FUEL_EFFICIENCY);
        assertThat(testCars.isFamilySize()).isEqualTo(UPDATED_FAMILY_SIZE);
        assertThat(testCars.isAutomaticGearBox()).isEqualTo(UPDATED_AUTOMATIC_GEAR_BOX);
        assertThat(testCars.getSeatsNumber()).isEqualTo(UPDATED_SEATS_NUMBER);
        assertThat(testCars.getFuel()).isEqualTo(UPDATED_FUEL);
        assertThat(testCars.getPricePerDay()).isEqualTo(UPDATED_PRICE_PER_DAY);
    }

    @Test
    @Transactional
    public void updateNonExistingCars() throws Exception {
        int databaseSizeBeforeUpdate = carsRepository.findAll().size();

        // Create the Cars

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarsMockMvc.perform(put("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cars)))
            .andExpect(status().isBadRequest());

        // Validate the Cars in the database
        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCars() throws Exception {
        // Initialize the database
        carsRepository.saveAndFlush(cars);

        int databaseSizeBeforeDelete = carsRepository.findAll().size();

        // Delete the cars
        restCarsMockMvc.perform(delete("/api/cars/{id}", cars.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cars> carsList = carsRepository.findAll();
        assertThat(carsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
