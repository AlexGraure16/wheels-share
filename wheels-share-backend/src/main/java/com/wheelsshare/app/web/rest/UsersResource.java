package com.wheelsshare.app.web.rest;

import com.wheelsshare.app.domain.UserLogInStatus;
import com.wheelsshare.app.domain.Users;
import com.wheelsshare.app.repository.UsersRepository;
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.wheelsshare.app.domain.Users}.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Transactional
public class UsersResource {

    private final Logger log = LoggerFactory.getLogger(UsersResource.class);

    private static final String ENTITY_NAME = "wheelsShareAppUsers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UsersRepository usersRepository;

    public UsersResource(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * {@code POST  /signUp} : Create a new users.
     *
     * @param user the user to create.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signUp")
    public String createUsers(@Valid @RequestBody Users user) throws URISyntaxException {
        log.debug("REST request to save Users : {}", user.getEmailAddress());

        Optional<Users> existingUser = usersRepository.findById(user.getEmailAddress());
        if (existingUser.isPresent()) {
            return "NOK";
        }

        Users result = usersRepository.save(user);
        if (result != null) {
            return "OK";
        }

        return "Error";
    }

    /**
     * {@code PUT  /users} : Updates an existing users.
     *
     * @param users the users to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated users,
     * or with status {@code 400 (Bad Request)} if the users is not valid,
     * or with status {@code 500 (Internal Server Error)} if the users couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/users")
    public ResponseEntity<Users> updateUsers(@Valid @RequestBody Users users) throws URISyntaxException {
        log.debug("REST request to update Users : {}", users);
        if (users.getEmailAddress() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        Users result = usersRepository.save(users);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, users.getEmailAddress()))
            .body(result);
    }

    /**
     * {@code GET  /users} : get all the users.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of users in body.
     */
    @GetMapping("/users")
    public List<Users> getAllUsers() {
        log.debug("REST request to get all Users");
        return usersRepository.findAll();
    }

    /**
     * {@code GET  /users/:emailAddress} : get the "emailAddress" users.
     *
     * @param emailAddress the emailAddress of the users to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the users, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{emailAddress}")
    public ResponseEntity<Users> getUsers(@PathVariable String emailAddress) {
        log.debug("REST request to get Users : {}", emailAddress);
        Optional<Users> users = usersRepository.findById(emailAddress);
        return ResponseUtil.wrapOrNotFound(users);
    }

    /**
     * {@code DELETE  /users/:emailAddress} : delete the "emailAddress" users.
     *
     * @param emailAddress the id of the users to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{emailAddress}")
    public ResponseEntity<Void> deleteUsers(@PathVariable String emailAddress) {
        log.debug("REST request to delete Users : {}", emailAddress);
        usersRepository.deleteById(emailAddress);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, emailAddress)).build();
    }

    /**
     * {@code GET  /:logIn} : log in
     *
     * @param emailAddress the emailAddress of the user to log in.
     * @param password the password of the user to log in.
     */
    @GetMapping("/logIn/{emailAddress}/{password}")
    public UserLogInStatus logIn(@PathVariable("emailAddress") String emailAddress, @PathVariable("password") String password) {
        log.debug("REST request to log in : {}", emailAddress);
        List<Users> users = usersRepository.findUserAndLogIn(emailAddress, password);
        if (!users.isEmpty()) {
            return new UserLogInStatus(users.get(0), "OK");
        }

        return new UserLogInStatus("NOK");
    }
}
