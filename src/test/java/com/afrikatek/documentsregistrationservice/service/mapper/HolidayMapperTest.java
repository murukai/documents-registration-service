package com.afrikatek.documentsregistrationservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HolidayMapperTest {

    private HolidayMapper holidayMapper;

    @BeforeEach
    public void setUp() {
        holidayMapper = new HolidayMapperImpl();
    }
}
