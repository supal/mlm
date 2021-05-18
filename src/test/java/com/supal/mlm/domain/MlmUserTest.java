package com.supal.mlm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.supal.mlm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MlmUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MlmUser.class);
        MlmUser mlmUser1 = new MlmUser();
        mlmUser1.setId(1L);
        MlmUser mlmUser2 = new MlmUser();
        mlmUser2.setId(mlmUser1.getId());
        assertThat(mlmUser1).isEqualTo(mlmUser2);
        mlmUser2.setId(2L);
        assertThat(mlmUser1).isNotEqualTo(mlmUser2);
        mlmUser1.setId(null);
        assertThat(mlmUser1).isNotEqualTo(mlmUser2);
    }
}
