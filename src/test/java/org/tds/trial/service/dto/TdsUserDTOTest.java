package org.tds.trial.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.tds.trial.web.rest.TestUtil;

class TdsUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TdsUserDTO.class);
        TdsUserDTO tdsUserDTO1 = new TdsUserDTO();
        tdsUserDTO1.setId(1L);
        TdsUserDTO tdsUserDTO2 = new TdsUserDTO();
        assertThat(tdsUserDTO1).isNotEqualTo(tdsUserDTO2);
        tdsUserDTO2.setId(tdsUserDTO1.getId());
        assertThat(tdsUserDTO1).isEqualTo(tdsUserDTO2);
        tdsUserDTO2.setId(2L);
        assertThat(tdsUserDTO1).isNotEqualTo(tdsUserDTO2);
        tdsUserDTO1.setId(null);
        assertThat(tdsUserDTO1).isNotEqualTo(tdsUserDTO2);
    }
}
