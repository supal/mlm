package com.supal.mlm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.supal.mlm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoardMlmUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardMlmUser.class);
        BoardMlmUser boardMlmUser1 = new BoardMlmUser();
        boardMlmUser1.setId(1L);
        BoardMlmUser boardMlmUser2 = new BoardMlmUser();
        boardMlmUser2.setId(boardMlmUser1.getId());
        assertThat(boardMlmUser1).isEqualTo(boardMlmUser2);
        boardMlmUser2.setId(2L);
        assertThat(boardMlmUser1).isNotEqualTo(boardMlmUser2);
        boardMlmUser1.setId(null);
        assertThat(boardMlmUser1).isNotEqualTo(boardMlmUser2);
    }
}
