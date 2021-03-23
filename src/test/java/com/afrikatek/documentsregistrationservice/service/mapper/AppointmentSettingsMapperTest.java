package com.afrikatek.documentsregistrationservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointmentSettingsMapperTest {

    private AppointmentSettingsMapper appointmentSettingsMapper;

    @BeforeEach
    public void setUp() {
        appointmentSettingsMapper = new AppointmentSettingsMapperImpl();
    }
}
