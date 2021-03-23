package com.afrikatek.documentsregistrationservice.repository.search;

import com.afrikatek.documentsregistrationservice.domain.MarriageDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link MarriageDetails} entity.
 */
public interface MarriageDetailsSearchRepository extends ElasticsearchRepository<MarriageDetails, Long> {}
