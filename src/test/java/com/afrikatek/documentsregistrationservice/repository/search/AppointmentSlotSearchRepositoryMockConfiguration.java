package com.afrikatek.documentsregistrationservice.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AppointmentSlotSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AppointmentSlotSearchRepositoryMockConfiguration {

    @MockBean
    private AppointmentSlotSearchRepository mockAppointmentSlotSearchRepository;
}
