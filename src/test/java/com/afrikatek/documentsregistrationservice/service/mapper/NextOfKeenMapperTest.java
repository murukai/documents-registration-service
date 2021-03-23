package com.afrikatek.documentsregistrationservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NextOfKeenMapperTest {

    private NextOfKeenMapper nextOfKeenMapper;

    @BeforeEach
    public void setUp() {
        nextOfKeenMapper = new NextOfKeenMapperImpl();
    }
}
