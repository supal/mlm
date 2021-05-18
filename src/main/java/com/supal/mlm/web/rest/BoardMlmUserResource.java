package com.supal.mlm.web.rest;

import com.supal.mlm.domain.BoardMlmUser;
import com.supal.mlm.repository.BoardMlmUserRepository;
import com.supal.mlm.service.BoardMlmUserService;
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
 * REST controller for managing {@link com.supal.mlm.domain.BoardMlmUser}.
 */
@RestController
@RequestMapping("/api")
public class BoardMlmUserResource {

    private final Logger log = LoggerFactory.getLogger(BoardMlmUserResource.class);

    private static final String ENTITY_NAME = "boardMlmUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BoardMlmUserService boardMlmUserService;

    private final BoardMlmUserRepository boardMlmUserRepository;

    public BoardMlmUserResource(BoardMlmUserService boardMlmUserService, BoardMlmUserRepository boardMlmUserRepository) {
        this.boardMlmUserService = boardMlmUserService;
        this.boardMlmUserRepository = boardMlmUserRepository;
    }

    /**
     * {@code POST  /board-mlm-users} : Create a new boardMlmUser.
     *
     * @param boardMlmUser the boardMlmUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new boardMlmUser, or with status {@code 400 (Bad Request)} if the boardMlmUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/board-mlm-users")
    public ResponseEntity<BoardMlmUser> createBoardMlmUser(@RequestBody BoardMlmUser boardMlmUser) throws URISyntaxException {
        log.debug("REST request to save BoardMlmUser : {}", boardMlmUser);
        if (boardMlmUser.getId() != null) {
            throw new BadRequestAlertException("A new boardMlmUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoardMlmUser result = boardMlmUserService.save(boardMlmUser);
        return ResponseEntity
            .created(new URI("/api/board-mlm-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /board-mlm-users/:id} : Updates an existing boardMlmUser.
     *
     * @param id the id of the boardMlmUser to save.
     * @param boardMlmUser the boardMlmUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardMlmUser,
     * or with status {@code 400 (Bad Request)} if the boardMlmUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the boardMlmUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/board-mlm-users/{id}")
    public ResponseEntity<BoardMlmUser> updateBoardMlmUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoardMlmUser boardMlmUser
    ) throws URISyntaxException {
        log.debug("REST request to update BoardMlmUser : {}, {}", id, boardMlmUser);
        if (boardMlmUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardMlmUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardMlmUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BoardMlmUser result = boardMlmUserService.save(boardMlmUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardMlmUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /board-mlm-users/:id} : Partial updates given fields of an existing boardMlmUser, field will ignore if it is null
     *
     * @param id the id of the boardMlmUser to save.
     * @param boardMlmUser the boardMlmUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated boardMlmUser,
     * or with status {@code 400 (Bad Request)} if the boardMlmUser is not valid,
     * or with status {@code 404 (Not Found)} if the boardMlmUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the boardMlmUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/board-mlm-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BoardMlmUser> partialUpdateBoardMlmUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BoardMlmUser boardMlmUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update BoardMlmUser partially : {}, {}", id, boardMlmUser);
        if (boardMlmUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, boardMlmUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!boardMlmUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BoardMlmUser> result = boardMlmUserService.partialUpdate(boardMlmUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, boardMlmUser.getId().toString())
        );
    }

    /**
     * {@code GET  /board-mlm-users} : get all the boardMlmUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boardMlmUsers in body.
     */
    @GetMapping("/board-mlm-users")
    public ResponseEntity<List<BoardMlmUser>> getAllBoardMlmUsers(Pageable pageable) {
        log.debug("REST request to get a page of BoardMlmUsers");
        Page<BoardMlmUser> page = boardMlmUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /board-mlm-users/:id} : get the "id" boardMlmUser.
     *
     * @param id the id of the boardMlmUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the boardMlmUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/board-mlm-users/{id}")
    public ResponseEntity<BoardMlmUser> getBoardMlmUser(@PathVariable Long id) {
        log.debug("REST request to get BoardMlmUser : {}", id);
        Optional<BoardMlmUser> boardMlmUser = boardMlmUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(boardMlmUser);
    }

    /**
     * {@code DELETE  /board-mlm-users/:id} : delete the "id" boardMlmUser.
     *
     * @param id the id of the boardMlmUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/board-mlm-users/{id}")
    public ResponseEntity<Void> deleteBoardMlmUser(@PathVariable Long id) {
        log.debug("REST request to delete BoardMlmUser : {}", id);
        boardMlmUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
