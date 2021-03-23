package com.afrikatek.documentsregistrationservice.repository.search;

import com.afrikatek.documentsregistrationservice.domain.NextOfKeen;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link NextOfKeen} entity.
 */
public interface NextOfKeenSearchRepository extends ElasticsearchRepository<NextOfKeen, Long> {}
