package com.afrikatek.documentsregistrationservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.afrikatek.documentsregistrationservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentSettingsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointmentSettingsDTO.class);
        AppointmentSettingsDTO appointmentSettingsDTO1 = new AppointmentSettingsDTO();
        appointmentSettingsDTO1.setId(1L);
        AppointmentSettingsDTO appointmentSettingsDTO2 = new AppointmentSettingsDTO();
        assertThat(appointmentSettingsDTO1).isNotEqualTo(appointmentSettingsDTO2);
        appointmentSettingsDTO2.setId(appointmentSettingsDTO1.getId());
        assertThat(appointmentSettingsDTO1).isEqualTo(appointmentSettingsDTO2);
        appointmentSettingsDTO2.setId(2L);
        assertThat(appointmentSettingsDTO1).isNotEqualTo(appointmentSettingsDTO2);
        appointmentSettingsDTO1.setId(null);
        assertThat(appointmentSettingsDTO1).isNotEqualTo(appointmentSettingsDTO2);
    }
}
