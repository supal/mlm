package com.supal.mlm.service.impl;

import com.supal.mlm.domain.MlmUser;
import com.supal.mlm.repository.MlmUserRepository;
import com.supal.mlm.service.MlmUserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MlmUser}.
 */
@Service
@Transactional
public class MlmUserServiceImpl implements MlmUserService {

    private final Logger log = LoggerFactory.getLogger(MlmUserServiceImpl.class);

    private final MlmUserRepository mlmUserRepository;

    public MlmUserServiceImpl(MlmUserRepository mlmUserRepository) {
        this.mlmUserRepository = mlmUserRepository;
    }

    @Override
    public MlmUser save(MlmUser mlmUser) {
        log.debug("Request to save MlmUser : {}", mlmUser);
        return mlmUserRepository.save(mlmUser);
    }

    @Override
    public Optional<MlmUser> partialUpdate(MlmUser mlmUser) {
        log.debug("Request to partially update MlmUser : {}", mlmUser);

        return mlmUserRepository
            .findById(mlmUser.getId())
            .map(
                existingMlmUser -> {
                    if (mlmUser.getUserId() != null) {
                        existingMlmUser.setUserId(mlmUser.getUserId());
                    }
                    if (mlmUser.getPaid() != null) {
                        existingMlmUser.setPaid(mlmUser.getPaid());
                    }
                    if (mlmUser.getActive() != null) {
                        existingMlmUser.setActive(mlmUser.getActive());
                    }
                    if (mlmUser.getRefMlmUserId() != null) {
                        existingMlmUser.setRefMlmUserId(mlmUser.getRefMlmUserId());
                    }

                    return existingMlmUser;
                }
            )
            .map(mlmUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MlmUser> findAll(Pageable pageable) {
        log.debug("Request to get all MlmUsers");
        return mlmUserRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MlmUser> findOne(Long id) {
        log.debug("Request to get MlmUser : {}", id);
        return mlmUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MlmUser : {}", id);
        mlmUserRepository.deleteById(id);
    }
}
