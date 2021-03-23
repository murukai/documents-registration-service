package com.afrikatek.documentsregistrationservice.repository.search;

import com.afrikatek.documentsregistrationservice.domain.Applicant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Applicant} entity.
 */
public interface ApplicantSearchRepository extends ElasticsearchRepository<Applicant, Long> {}
