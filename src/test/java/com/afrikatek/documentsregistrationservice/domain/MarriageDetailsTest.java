package com.afrikatek.documentsregistrationservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsregistrationservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarriageDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarriageDetails.class);
        MarriageDetails marriageDetails1 = new MarriageDetails();
        marriageDetails1.setId(1L);
        MarriageDetails marriageDetails2 = new MarriageDetails();
        marriageDetails2.setId(marriageDetails1.getId());
        assertThat(marriageDetails1).isEqualTo(marriageDetails2);
        marriageDetails2.setId(2L);
        assertThat(marriageDetails1).isNotEqualTo(marriageDetails2);
        marriageDetails1.setId(null);
        assertThat(marriageDetails1).isNotEqualTo(marriageDetails2);
    }
}
