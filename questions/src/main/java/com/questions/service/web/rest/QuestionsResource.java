package com.questions.service.web.rest;

import com.questions.service.domain.Questions;
import com.questions.service.repository.QuestionsRepository;
import com.questions.service.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.questions.service.domain.Questions}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Transactional
public class QuestionsResource {

    private final Logger log = LoggerFactory.getLogger(QuestionsResource.class);

    private static final String ENTITY_NAME = "questionsService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionsRepository questionsRepository;

    public QuestionsResource(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }

    /**
     * {@code POST  /questions} : Create a new questions.
     *
     * @param questions the questions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questions, or with status {@code 400 (Bad Request)} if the questions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questions")
    public ResponseEntity<Questions> createQuestions(@RequestBody Questions questions) throws URISyntaxException {
        log.debug("REST request to save Questions : {}", questions);
        if (questions.getId() != null) {
            throw new BadRequestAlertException("A new questions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Questions result = questionsRepository.save(questions);
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questions} : Updates an existing questions.
     *
     * @param questions the questions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questions,
     * or with status {@code 400 (Bad Request)} if the questions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questions")
    public ResponseEntity<Questions> updateQuestions(@RequestBody Questions questions) throws URISyntaxException {
        log.debug("REST request to update Questions : {}", questions);
        if (questions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Questions result = questionsRepository.save(questions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, questions.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("/questions")
    public List<Questions> getAllQuestions() {
        log.debug("REST request to get all Questions");
        return questionsRepository.findAll();
    }

    /**
     * {@code GET  /questions/:id} : get the "id" questions.
     *
     * @param id the id of the questions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questions/{id}")
    public ResponseEntity<Questions> getQuestions(@PathVariable Long id) {
        log.debug("REST request to get Questions : {}", id);
        Optional<Questions> questions = questionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(questions);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" questions.
     *
     * @param id the id of the questions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestions(@PathVariable Long id) {
        log.debug("REST request to delete Questions : {}", id);
        questionsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
