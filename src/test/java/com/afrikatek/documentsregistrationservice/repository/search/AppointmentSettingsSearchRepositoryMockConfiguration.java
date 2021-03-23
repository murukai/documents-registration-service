package com.afrikatek.documentsregistrationservice.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AppointmentSettingsSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AppointmentSettingsSearchRepositoryMockConfiguration {

    @MockBean
    private AppointmentSettingsSearchRepository mockAppointmentSettingsSearchRepository;
}
