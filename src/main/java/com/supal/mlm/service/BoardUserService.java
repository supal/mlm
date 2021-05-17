package com.supal.mlm.service;

import com.supal.mlm.domain.BoardUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link BoardUser}.
 */
public interface BoardUserService {
    /**
     * Save a boardUser.
     *
     * @param boardUser the entity to save.
     * @return the persisted entity.
     */
    BoardUser save(BoardUser boardUser);

    /**
     * Partially updates a boardUser.
     *
     * @param boardUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BoardUser> partialUpdate(BoardUser boardUser);

    /**
     * Get all the boardUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BoardUser> findAll(Pageable pageable);

    /**
     * Get the "id" boardUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BoardUser> findOne(Long id);

    /**
     * Delete the "id" boardUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
