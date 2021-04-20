package org.tds.trial.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.tds.trial.web.rest.TestUtil;

class EsimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Esim.class);
        Esim esim1 = new Esim();
        esim1.setId(1L);
        Esim esim2 = new Esim();
        esim2.setId(esim1.getId());
        assertThat(esim1).isEqualTo(esim2);
        esim2.setId(2L);
        assertThat(esim1).isNotEqualTo(esim2);
        esim1.setId(null);
        assertThat(esim1).isNotEqualTo(esim2);
    }
}
