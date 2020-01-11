package com.wheelsshare.app.web.rest;

import com.wheelsshare.app.domain.Rents;
import com.wheelsshare.app.repository.RentsRepository;
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
 * REST controller for managing {@link com.wheelsshare.app.domain.Rents}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RentsResource {

    private final Logger log = LoggerFactory.getLogger(RentsResource.class);

    private static final String ENTITY_NAME = "wheelsShareAppRents";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentsRepository rentsRepository;

    public RentsResource(RentsRepository rentsRepository) {
        this.rentsRepository = rentsRepository;
    }

    /**
     * {@code POST  /rents} : Create a new rents.
     *
     * @param rents the rents to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rents, or with status {@code 400 (Bad Request)} if the rents has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rents")
    public ResponseEntity<Rents> createRents(@Valid @RequestBody Rents rents) throws URISyntaxException {
        log.debug("REST request to save Rents : {}", rents);
        if (rents.getId() != null) {
            throw new BadRequestAlertException("A new rents cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rents result = rentsRepository.save(rents);
        return ResponseEntity.created(new URI("/api/rents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rents} : Updates an existing rents.
     *
     * @param rents the rents to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rents,
     * or with status {@code 400 (Bad Request)} if the rents is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rents couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rents")
    public ResponseEntity<Rents> updateRents(@Valid @RequestBody Rents rents) throws URISyntaxException {
        log.debug("REST request to update Rents : {}", rents);
        if (rents.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rents result = rentsRepository.save(rents);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rents.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rents} : get all the rents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rents in body.
     */
    @GetMapping("/rents")
    public List<Rents> getAllRents() {
        log.debug("REST request to get all Rents");
        return rentsRepository.findAll();
    }

    /**
     * {@code GET  /rents/:id} : get the "id" rents.
     *
     * @param id the id of the rents to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rents, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rents/{id}")
    public ResponseEntity<Rents> getRents(@PathVariable Long id) {
        log.debug("REST request to get Rents : {}", id);
        Optional<Rents> rents = rentsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rents);
    }

    /**
     * {@code DELETE  /rents/:id} : delete the "id" rents.
     *
     * @param id the id of the rents to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rents/{id}")
    public ResponseEntity<Void> deleteRents(@PathVariable Long id) {
        log.debug("REST request to delete Rents : {}", id);
        rentsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
