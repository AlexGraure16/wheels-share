package com.wheelsshare.app.web.rest;

import com.wheelsshare.app.domain.Rentals;
import com.wheelsshare.app.repository.RentalsRepository;
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
 * REST controller for managing {@link com.wheelsshare.app.domain.Rentals}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RentalsResource {

    private final Logger log = LoggerFactory.getLogger(RentalsResource.class);

    private static final String ENTITY_NAME = "wheelsShareAppRentals";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentalsRepository rentalsRepository;

    public RentalsResource(RentalsRepository rentalsRepository) {
        this.rentalsRepository = rentalsRepository;
    }

    /**
     * {@code POST  /rentals} : Create a new rentals.
     *
     * @param rentals the rentals to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rentals, or with status {@code 400 (Bad Request)} if the rentals has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rent")
    public ResponseEntity<Rentals> createRentals(@Valid @RequestBody Rentals rentals) throws URISyntaxException {
        log.debug("REST request to save Rentals : {}", rentals);
        if (rentals.getId() != null) {
            throw new BadRequestAlertException("A new rentals cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rentals result = rentalsRepository.save(rentals);
        return ResponseEntity.created(new URI("/api/rentals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rentals} : Updates an existing rentals.
     *
     * @param rentals the rentals to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentals,
     * or with status {@code 400 (Bad Request)} if the rentals is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentals couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rentals")
    public ResponseEntity<Rentals> updateRentals(@Valid @RequestBody Rentals rentals) throws URISyntaxException {
        log.debug("REST request to update Rentals : {}", rentals);
        if (rentals.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rentals result = rentalsRepository.save(rentals);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rentals.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rentals} : get all the rentals.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rentals in body.
     */
    @GetMapping("/rentals")
    public List<Rentals> getAllRentals() {
        log.debug("REST request to get all Rentals");
        return rentalsRepository.findAll();
    }

    /**
     * {@code GET  /admin/ongoingRentals} : get all the ongoing rentals.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ongoing rentals in body.
     */
    @GetMapping("/admin/ongoingRentals")
    public List<Rentals> getOngoingRentals() {
        log.debug("REST request to get all ongoing Rentals");
        return rentalsRepository.findAll();
    }

    /**
     * {@code GET  /rentals/:id} : get the "id" rentals.
     *
     * @param id the id of the rentals to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentals, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rentals/{id}")
    public ResponseEntity<Rentals> getRentals(@PathVariable Long id) {
        log.debug("REST request to get Rentals : {}", id);
        Optional<Rentals> rentals = rentalsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rentals);
    }

    /**
     * {@code DELETE  /rentals/:id} : delete the "id" rentals.
     *
     * @param id the id of the rentals to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rentals/{id}")
    public ResponseEntity<Void> deleteRentals(@PathVariable Long id) {
        log.debug("REST request to delete Rentals : {}", id);
        rentalsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}