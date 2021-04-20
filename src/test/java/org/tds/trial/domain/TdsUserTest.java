package org.tds.trial.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.tds.trial.web.rest.TestUtil;

class TdsUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TdsUser.class);
        TdsUser tdsUser1 = new TdsUser();
        tdsUser1.setId(1L);
        TdsUser tdsUser2 = new TdsUser();
        tdsUser2.setId(tdsUser1.getId());
        assertThat(tdsUser1).isEqualTo(tdsUser2);
        tdsUser2.setId(2L);
        assertThat(tdsUser1).isNotEqualTo(tdsUser2);
        tdsUser1.setId(null);
        assertThat(tdsUser1).isNotEqualTo(tdsUser2);
    }
}
