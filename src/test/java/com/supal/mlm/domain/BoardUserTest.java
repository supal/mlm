package com.supal.mlm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.supal.mlm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoardUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoardUser.class);
        BoardUser boardUser1 = new BoardUser();
        boardUser1.setId(1L);
        BoardUser boardUser2 = new BoardUser();
        boardUser2.setId(boardUser1.getId());
        assertThat(boardUser1).isEqualTo(boardUser2);
        boardUser2.setId(2L);
        assertThat(boardUser1).isNotEqualTo(boardUser2);
        boardUser1.setId(null);
        assertThat(boardUser1).isNotEqualTo(boardUser2);
    }
}
