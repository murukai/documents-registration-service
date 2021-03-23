package com.afrikatek.documentsregistrationservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsregistrationservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NextOfKeenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOfKeen.class);
        NextOfKeen nextOfKeen1 = new NextOfKeen();
        nextOfKeen1.setId(1L);
        NextOfKeen nextOfKeen2 = new NextOfKeen();
        nextOfKeen2.setId(nextOfKeen1.getId());
        assertThat(nextOfKeen1).isEqualTo(nextOfKeen2);
        nextOfKeen2.setId(2L);
        assertThat(nextOfKeen1).isNotEqualTo(nextOfKeen2);
        nextOfKeen1.setId(null);
        assertThat(nextOfKeen1).isNotEqualTo(nextOfKeen2);
    }
}
