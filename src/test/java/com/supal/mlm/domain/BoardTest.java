package com.supal.mlm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.supal.mlm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Board.class);
        Board board1 = new Board();
        board1.setBoardId(1L);
        Board board2 = new Board();
        board2.setBoardId(board1.getBoardId());
        assertThat(board1).isEqualTo(board2);
        board2.setBoardId(2L);
        assertThat(board1).isNotEqualTo(board2);
        board1.setBoardId(null);
        assertThat(board1).isNotEqualTo(board2);
    }
}
