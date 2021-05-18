package com.supal.mlm.service;

import com.supal.mlm.domain.BoardMlmUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link BoardMlmUser}.
 */
public interface BoardMlmUserService {
    /**
     * Save a boardMlmUser.
     *
     * @param boardMlmUser the entity to save.
     * @return the persisted entity.
     */
    BoardMlmUser save(BoardMlmUser boardMlmUser);

    /**
     * Partially updates a boardMlmUser.
     *
     * @param boardMlmUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BoardMlmUser> partialUpdate(BoardMlmUser boardMlmUser);

    /**
     * Get all the boardMlmUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BoardMlmUser> findAll(Pageable pageable);

    /**
     * Get the "id" boardMlmUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BoardMlmUser> findOne(Long id);

    /**
     * Delete the "id" boardMlmUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
