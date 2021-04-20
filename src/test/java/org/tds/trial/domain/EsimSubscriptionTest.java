package org.tds.trial.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.tds.trial.web.rest.TestUtil;

class EsimSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EsimSubscription.class);
        EsimSubscription esimSubscription1 = new EsimSubscription();
        esimSubscription1.setId(1L);
        EsimSubscription esimSubscription2 = new EsimSubscription();
        esimSubscription2.setId(esimSubscription1.getId());
        assertThat(esimSubscription1).isEqualTo(esimSubscription2);
        esimSubscription2.setId(2L);
        assertThat(esimSubscription1).isNotEqualTo(esimSubscription2);
        esimSubscription1.setId(null);
        assertThat(esimSubscription1).isNotEqualTo(esimSubscription2);
    }
}
