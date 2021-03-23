package com.afrikatek.documentsregistrationservice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarriageDetailsMapperTest {

    private MarriageDetailsMapper marriageDetailsMapper;

    @BeforeEach
    public void setUp() {
        marriageDetailsMapper = new MarriageDetailsMapperImpl();
    }
}
