package com.afrikatek.documentsregistrationservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsregistrationservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentSettingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentSettings.class);
        AppointmentSettings appointmentSettings1 = new AppointmentSettings();
        appointmentSettings1.setId(1L);
        AppointmentSettings appointmentSettings2 = new AppointmentSettings();
        appointmentSettings2.setId(appointmentSettings1.getId());
        assertThat(appointmentSettings1).isEqualTo(appointmentSettings2);
        appointmentSettings2.setId(2L);
        assertThat(appointmentSettings1).isNotEqualTo(appointmentSettings2);
        appointmentSettings1.setId(null);
        assertThat(appointmentSettings1).isNotEqualTo(appointmentSettings2);
    }
}
