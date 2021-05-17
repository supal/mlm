package com.supal.mlm.service.impl;

import com.supal.mlm.domain.Board;
import com.supal.mlm.repository.BoardRepository;
import com.supal.mlm.service.BoardService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Board}.
 */
@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board save(Board board) {
        log.debug("Request to save Board : {}", board);
        return boardRepository.save(board);
    }

    @Override
    public Optional<Board> partialUpdate(Board board) {
        log.debug("Request to partially update Board : {}", board);

        return boardRepository
            .findById(board.getBoardId())
            .map(
                existingBoard -> {
                    if (board.getType() != null) {
                        existingBoard.setType(board.getType());
                    }
                    if (board.getStatus() != null) {
                        existingBoard.setStatus(board.getStatus());
                    }

                    return existingBoard;
                }
            )
            .map(boardRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
        log.debug("Request to get all Boards");
        return boardRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Board> findOne(Long id) {
        log.debug("Request to get Board : {}", id);
        return boardRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Board : {}", id);
        boardRepository.deleteById(id);
    }
}
