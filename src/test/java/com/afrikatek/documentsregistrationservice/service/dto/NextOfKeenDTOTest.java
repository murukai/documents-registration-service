package com.afrikatek.documentsregistrationservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsregistrationservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NextOfKeenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOfKeenDTO.class);
        NextOfKeenDTO nextOfKeenDTO1 = new NextOfKeenDTO();
        nextOfKeenDTO1.setId(1L);
        NextOfKeenDTO nextOfKeenDTO2 = new NextOfKeenDTO();
        assertThat(nextOfKeenDTO1).isNotEqualTo(nextOfKeenDTO2);
        nextOfKeenDTO2.setId(nextOfKeenDTO1.getId());
        assertThat(nextOfKeenDTO1).isEqualTo(nextOfKeenDTO2);
        nextOfKeenDTO2.setId(2L);
        assertThat(nextOfKeenDTO1).isNotEqualTo(nextOfKeenDTO2);
        nextOfKeenDTO1.setId(null);
        assertThat(nextOfKeenDTO1).isNotEqualTo(nextOfKeenDTO2);
    }
}
