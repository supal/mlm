package com.supal.mlm.service;

import com.supal.mlm.domain.MlmUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link MlmUser}.
 */
public interface MlmUserService {
    /**
     * Save a mlmUser.
     *
     * @param mlmUser the entity to save.
     * @return the persisted entity.
     */
    MlmUser save(MlmUser mlmUser);

    /**
     * Partially updates a mlmUser.
     *
     * @param mlmUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MlmUser> partialUpdate(MlmUser mlmUser);

    /**
     * Get all the mlmUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MlmUser> findAll(Pageable pageable);

    /**
     * Get the "id" mlmUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MlmUser> findOne(Long id);

    /**
     * Delete the "id" mlmUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
