package com.supal.mlm.web.rest;

import com.supal.mlm.domain.BoardUser;
import com.supal.mlm.repository.BoardUserRepository;
import com.supal.mlm.service.BoardUserService;
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
 * REST controller for managing {@link com.supal.mlm.domain.BoardUser}.
 */
@RestController
@RequestMapping("/api")
public class BoardUserResource {

    private final Logger log = LoggerFactory.getLogger(BoardUserResource.class);

    private static final String ENTITY_NAME = "boardUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardUserService boardUserService;

    private final BoardUserRepository boardUserRepository;

    public BoardUserResource(BoardUserService boardUserService, BoardUserRepository boardUserRepository) {
        this.boardUserService = boardUserService;
        this.boardUserRepository = boardUserRepository;
    }

    /**
     * {@code POST  /board-users} : Create a new boardUser.
     *
     * @param boardUser the boardUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boardUser, or with status {@code 400 (Bad Request)} if the boardUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/board-users")
    public ResponseEntity<BoardUser> createBoardUser(@RequestBody BoardUser boardUser) throws URISyntaxException {
        log.debug("REST request to save BoardUser : {}", boardUser);
        if (boardUser.getId() != null) {
            throw new BadRequestAlertException("A new boardUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoardUser result = boardUserService.save(boardUser);
        return ResponseEntity
            .created(new URI("/api/board-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /board-users/:id} : Updates an existing boardUser.
     *
     * @param id the id of the boardUser to save.
     * @param boardUser the boardUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardUser,
     * or with status {@code 400 (Bad Request)} if the boardUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boardUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/board-users/{id}")
    public ResponseEntity<BoardUser> updateBoardUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoardUser boardUser
    ) throws URISyntaxException {
        log.debug("REST request to update BoardUser : {}, {}", id, boardUser);
        if (boardUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BoardUser result = boardUserService.save(boardUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /board-users/:id} : Partial updates given fields of an existing boardUser, field will ignore if it is null
     *
     * @param id the id of the boardUser to save.
     * @param boardUser the boardUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardUser,
     * or with status {@code 400 (Bad Request)} if the boardUser is not valid,
     * or with status {@code 404 (Not Found)} if the boardUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the boardUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/board-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BoardUser> partialUpdateBoardUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoardUser boardUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update BoardUser partially : {}, {}", id, boardUser);
        if (boardUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BoardUser> result = boardUserService.partialUpdate(boardUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardUser.getId().toString())
        );
    }

    /**
     * {@code GET  /board-users} : get all the boardUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boardUsers in body.
     */
    @GetMapping("/board-users")
    public ResponseEntity<List<BoardUser>> getAllBoardUsers(Pageable pageable) {
        log.debug("REST request to get a page of BoardUsers");
        Page<BoardUser> page = boardUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /board-users/:id} : get the "id" boardUser.
     *
     * @param id the id of the boardUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boardUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/board-users/{id}")
    public ResponseEntity<BoardUser> getBoardUser(@PathVariable Long id) {
        log.debug("REST request to get BoardUser : {}", id);
        Optional<BoardUser> boardUser = boardUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boardUser);
    }

    /**
     * {@code DELETE  /board-users/:id} : delete the "id" boardUser.
     *
     * @param id the id of the boardUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/board-users/{id}")
    public ResponseEntity<Void> deleteBoardUser(@PathVariable Long id) {
        log.debug("REST request to delete BoardUser : {}", id);
        boardUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
