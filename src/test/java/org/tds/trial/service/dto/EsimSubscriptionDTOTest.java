package org.tds.trial.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.tds.trial.web.rest.TestUtil;

class EsimSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EsimSubscriptionDTO.class);
        EsimSubscriptionDTO esimSubscriptionDTO1 = new EsimSubscriptionDTO();
        esimSubscriptionDTO1.setId(1L);
        EsimSubscriptionDTO esimSubscriptionDTO2 = new EsimSubscriptionDTO();
        assertThat(esimSubscriptionDTO1).isNotEqualTo(esimSubscriptionDTO2);
        esimSubscriptionDTO2.setId(esimSubscriptionDTO1.getId());
        assertThat(esimSubscriptionDTO1).isEqualTo(esimSubscriptionDTO2);
        esimSubscriptionDTO2.setId(2L);
        assertThat(esimSubscriptionDTO1).isNotEqualTo(esimSubscriptionDTO2);
        esimSubscriptionDTO1.setId(null);
        assertThat(esimSubscriptionDTO1).isNotEqualTo(esimSubscriptionDTO2);
    }
}
