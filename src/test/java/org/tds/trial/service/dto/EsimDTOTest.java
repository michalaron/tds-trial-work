package org.tds.trial.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.tds.trial.web.rest.TestUtil;

class EsimDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EsimDTO.class);
        EsimDTO esimDTO1 = new EsimDTO();
        esimDTO1.setId(1L);
        EsimDTO esimDTO2 = new EsimDTO();
        assertThat(esimDTO1).isNotEqualTo(esimDTO2);
        esimDTO2.setId(esimDTO1.getId());
        assertThat(esimDTO1).isEqualTo(esimDTO2);
        esimDTO2.setId(2L);
        assertThat(esimDTO1).isNotEqualTo(esimDTO2);
        esimDTO1.setId(null);
        assertThat(esimDTO1).isNotEqualTo(esimDTO2);
    }
}
