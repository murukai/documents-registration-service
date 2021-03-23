package com.afrikatek.documentsregistrationservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsregistrationservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HolidayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Holiday.class);
        Holiday holiday1 = new Holiday();
        holiday1.setId(1L);
        Holiday holiday2 = new Holiday();
        holiday2.setId(holiday1.getId());
        assertThat(holiday1).isEqualTo(holiday2);
        holiday2.setId(2L);
        assertThat(holiday1).isNotEqualTo(holiday2);
        holiday1.setId(null);
        assertThat(holiday1).isNotEqualTo(holiday2);
    }
}
