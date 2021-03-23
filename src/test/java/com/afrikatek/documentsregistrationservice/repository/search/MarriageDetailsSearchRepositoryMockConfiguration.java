package com.afrikatek.documentsregistrationservice.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link MarriageDetailsSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MarriageDetailsSearchRepositoryMockConfiguration {

    @MockBean
    private MarriageDetailsSearchRepository mockMarriageDetailsSearchRepository;
}
