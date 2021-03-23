package com.afrikatek.documentsregistrationservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointmentSlotMapperTest {

    private AppointmentSlotMapper appointmentSlotMapper;

    @BeforeEach
    public void setUp() {
        appointmentSlotMapper = new AppointmentSlotMapperImpl();
    }
}
