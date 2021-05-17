package com.supal.mlm.service.impl;

import com.supal.mlm.domain.BoardUser;
import com.supal.mlm.repository.BoardUserRepository;
import com.supal.mlm.service.BoardUserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BoardUser}.
 */
@Service
@Transactional
public class BoardUserServiceImpl implements BoardUserService {

    private final Logger log = LoggerFactory.getLogger(BoardUserServiceImpl.class);

    private final BoardUserRepository boardUserRepository;

    public BoardUserServiceImpl(BoardUserRepository boardUserRepository) {
        this.boardUserRepository = boardUserRepository;
    }

    @Override
    public BoardUser save(BoardUser boardUser) {
        log.debug("Request to save BoardUser : {}", boardUser);
        return boardUserRepository.save(boardUser);
    }

    @Override
    public Optional<BoardUser> partialUpdate(BoardUser boardUser) {
        log.debug("Request to partially update BoardUser : {}", boardUser);

        return boardUserRepository
            .findById(boardUser.getId())
            .map(
                existingBoardUser -> {
                    if (boardUser.getBoardId() != null) {
                        existingBoardUser.setBoardId(boardUser.getBoardId());
                    }
                    if (boardUser.getUserId() != null) {
                        existingBoardUser.setUserId(boardUser.getUserId());
                    }
                    if (boardUser.getLevel() != null) {
                        existingBoardUser.setLevel(boardUser.getLevel());
                    }
                    if (boardUser.getStatus() != null) {
                        existingBoardUser.setStatus(boardUser.getStatus());
                    }

                    return existingBoardUser;
                }
            )
            .map(boardUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoardUser> findAll(Pageable pageable) {
        log.debug("Request to get all BoardUsers");
        return boardUserRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BoardUser> findOne(Long id) {
        log.debug("Request to get BoardUser : {}", id);
        return boardUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BoardUser : {}", id);
        boardUserRepository.deleteById(id);
    }
}
