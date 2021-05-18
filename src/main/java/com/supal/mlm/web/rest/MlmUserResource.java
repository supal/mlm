package com.supal.mlm.web.rest;

import com.supal.mlm.domain.MlmUser;
import com.supal.mlm.repository.MlmUserRepository;
import com.supal.mlm.service.MlmUserService;
import com.supal.mlm.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.supal.mlm.domain.MlmUser}.
 */
@RestController
@RequestMapping("/api")
public class MlmUserResource {

    private final Logger log = LoggerFactory.getLogger(MlmUserResource.class);

    private static final String ENTITY_NAME = "mlmUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MlmUserService mlmUserService;

    private final MlmUserRepository mlmUserRepository;

    public MlmUserResource(MlmUserService mlmUserService, MlmUserRepository mlmUserRepository) {
        this.mlmUserService = mlmUserService;
        this.mlmUserRepository = mlmUserRepository;
    }

    /**
     * {@code POST  /mlm-users} : Create a new mlmUser.
     *
     * @param mlmUser the mlmUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mlmUser, or with status {@code 400 (Bad Request)} if the mlmUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mlm-users")
    public ResponseEntity<MlmUser> createMlmUser(@RequestBody MlmUser mlmUser) throws URISyntaxException {
        log.debug("REST request to save MlmUser : {}", mlmUser);
        if (mlmUser.getId() != null) {
            throw new BadRequestAlertException("A new mlmUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MlmUser result = mlmUserService.save(mlmUser);
        return ResponseEntity
            .created(new URI("/api/mlm-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mlm-users/:id} : Updates an existing mlmUser.
     *
     * @param id the id of the mlmUser to save.
     * @param mlmUser the mlmUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mlmUser,
     * or with status {@code 400 (Bad Request)} if the mlmUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mlmUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mlm-users/{id}")
    public ResponseEntity<MlmUser> updateMlmUser(@PathVariable(value = "id", required = false) final Long id, @RequestBody MlmUser mlmUser)
        throws URISyntaxException {
        log.debug("REST request to update MlmUser : {}, {}", id, mlmUser);
        if (mlmUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mlmUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mlmUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MlmUser result = mlmUserService.save(mlmUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mlmUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mlm-users/:id} : Partial updates given fields of an existing mlmUser, field will ignore if it is null
     *
     * @param id the id of the mlmUser to save.
     * @param mlmUser the mlmUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mlmUser,
     * or with status {@code 400 (Bad Request)} if the mlmUser is not valid,
     * or with status {@code 404 (Not Found)} if the mlmUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the mlmUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mlm-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MlmUser> partialUpdateMlmUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MlmUser mlmUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update MlmUser partially : {}, {}", id, mlmUser);
        if (mlmUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mlmUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mlmUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MlmUser> result = mlmUserService.partialUpdate(mlmUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mlmUser.getId().toString())
        );
    }

    /**
     * {@code GET  /mlm-users} : get all the mlmUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mlmUsers in body.
     */
    @GetMapping("/mlm-users")
    public ResponseEntity<List<MlmUser>> getAllMlmUsers(Pageable pageable) {
        log.debug("REST request to get a page of MlmUsers");
        Page<MlmUser> page = mlmUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mlm-users/:id} : get the "id" mlmUser.
     *
     * @param id the id of the mlmUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mlmUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mlm-users/{id}")
    public ResponseEntity<MlmUser> getMlmUser(@PathVariable Long id) {
        log.debug("REST request to get MlmUser : {}", id);
        Optional<MlmUser> mlmUser = mlmUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mlmUser);
    }

    /**
     * {@code DELETE  /mlm-users/:id} : delete the "id" mlmUser.
     *
     * @param id the id of the mlmUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mlm-users/{id}")
    public ResponseEntity<Void> deleteMlmUser(@PathVariable Long id) {
        log.debug("REST request to delete MlmUser : {}", id);
        mlmUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
