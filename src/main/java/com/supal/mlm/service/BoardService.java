package com.supal.mlm.service;

import com.supal.mlm.domain.Board;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Board}.
 */
public interface BoardService {
    /**
     * Save a board.
     *
     * @param board the entity to save.
     * @return the persisted entity.
     */
    Board save(Board board);

    /**
     * Partially updates a board.
     *
     * @param board the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Board> partialUpdate(Board board);

    /**
     * Get all the boards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Board> findAll(Pageable pageable);

    /**
     * Get the "id" board.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Board> findOne(Long id);

    /**
     * Delete the "id" board.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
