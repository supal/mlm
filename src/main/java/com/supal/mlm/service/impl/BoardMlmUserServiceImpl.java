package com.supal.mlm.service.impl;

import com.supal.mlm.domain.BoardMlmUser;
import com.supal.mlm.repository.BoardMlmUserRepository;
import com.supal.mlm.service.BoardMlmUserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BoardMlmUser}.
 */
@Service
@Transactional
public class BoardMlmUserServiceImpl implements BoardMlmUserService {

    private final Logger log = LoggerFactory.getLogger(BoardMlmUserServiceImpl.class);

    private final BoardMlmUserRepository boardMlmUserRepository;

    public BoardMlmUserServiceImpl(BoardMlmUserRepository boardMlmUserRepository) {
        this.boardMlmUserRepository = boardMlmUserRepository;
    }

    @Override
    public BoardMlmUser save(BoardMlmUser boardMlmUser) {
        log.debug("Request to save BoardMlmUser : {}", boardMlmUser);
        return boardMlmUserRepository.save(boardMlmUser);
    }

    @Override
    public Optional<BoardMlmUser> partialUpdate(BoardMlmUser boardMlmUser) {
        log.debug("Request to partially update BoardMlmUser : {}", boardMlmUser);

        return boardMlmUserRepository
            .findById(boardMlmUser.getId())
            .map(
                existingBoardMlmUser -> {
                    if (boardMlmUser.getBoardId() != null) {
                        existingBoardMlmUser.setBoardId(boardMlmUser.getBoardId());
                    }
                    if (boardMlmUser.getMlmUserId() != null) {
                        existingBoardMlmUser.setMlmUserId(boardMlmUser.getMlmUserId());
                    }
                    if (boardMlmUser.getLevel() != null) {
                        existingBoardMlmUser.setLevel(boardMlmUser.getLevel());
                    }

                    return existingBoardMlmUser;
                }
            )
            .map(boardMlmUserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoardMlmUser> findAll(Pageable pageable) {
        log.debug("Request to get all BoardMlmUsers");
        return boardMlmUserRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BoardMlmUser> findOne(Long id) {
        log.debug("Request to get BoardMlmUser : {}", id);
        return boardMlmUserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BoardMlmUser : {}", id);
        boardMlmUserRepository.deleteById(id);
    }
}
