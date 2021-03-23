package com.afrikatek.documentsregistrationservice.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ApplicantSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ApplicantSearchRepositoryMockConfiguration {

    @MockBean
    private ApplicantSearchRepository mockApplicantSearchRepository;
}
