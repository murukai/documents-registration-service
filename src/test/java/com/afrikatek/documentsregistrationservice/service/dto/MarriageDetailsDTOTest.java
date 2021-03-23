package com.afrikatek.documentsregistrationservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsregistrationservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarriageDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarriageDetailsDTO.class);
        MarriageDetailsDTO marriageDetailsDTO1 = new MarriageDetailsDTO();
        marriageDetailsDTO1.setId(1L);
        MarriageDetailsDTO marriageDetailsDTO2 = new MarriageDetailsDTO();
        assertThat(marriageDetailsDTO1).isNotEqualTo(marriageDetailsDTO2);
        marriageDetailsDTO2.setId(marriageDetailsDTO1.getId());
        assertThat(marriageDetailsDTO1).isEqualTo(marriageDetailsDTO2);
        marriageDetailsDTO2.setId(2L);
        assertThat(marriageDetailsDTO1).isNotEqualTo(marriageDetailsDTO2);
        marriageDetailsDTO1.setId(null);
        assertThat(marriageDetailsDTO1).isNotEqualTo(marriageDetailsDTO2);
    }
}
