package com.wheelsshare.app.web.rest;

import com.wheelsshare.app.domain.Cars;
import com.wheelsshare.app.repository.CarsRepository;
import com.wheelsshare.app.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.wheelsshare.app.domain.Cars}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Transactional
public class CarsResource {

    private final Logger log = LoggerFactory.getLogger(CarsResource.class);

    private static final String ENTITY_NAME = "wheelsShareAppCars";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarsRepository carsRepository;

    public CarsResource(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    /**
     * {@code POST  /admin/addCar} : Create a new cars.
     *
     * @param cars the cars to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cars, or with status {@code 400 (Bad Request)} if the cars has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/addCar")
    public ResponseEntity<Cars> createCars(@Valid @RequestBody Cars cars) throws URISyntaxException {
        log.debug("REST request to save Cars : {}", cars);
        if (cars.getId() != null) {
            throw new BadRequestAlertException("A new cars cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cars result = carsRepository.save(cars);
        return ResponseEntity.created(new URI("/api/cars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /admin/addCar} : Updates an existing cars.
     *
     * @param cars the cars to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cars,
     * or with status {@code 400 (Bad Request)} if the cars is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cars couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/addCar")
    public ResponseEntity<Cars> updateCars(@Valid @RequestBody Cars cars) throws URISyntaxException {
        log.debug("REST request to update Cars : {}", cars);
        if (cars.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cars result = carsRepository.save(cars);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cars.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cars} : get all the cars.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cars in body.
     */
    @GetMapping("/cars")
    public List<Cars> getAllCars() {
        log.debug("REST request to get all Cars");
        return carsRepository.findAll();
    }

    /**
     * {@code GET  /cars/:id} : get the "id" cars.
     *
     * @param id the id of the cars to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cars, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cars/{id}")
    public ResponseEntity<Cars> getCars(@PathVariable Long id) {
        log.debug("REST request to get Cars : {}", id);
        Optional<Cars> cars = carsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cars);
    }

    /**
     * {@code DELETE  /admin/cars/delete/:id} : delete the "id" cars.
     *
     * @param id the id of the cars to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/cars/delete/{id}")
    public ResponseEntity<Void> deleteCars(@PathVariable Long id) {
        log.debug("REST request to delete Cars : {}", id);
        carsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
