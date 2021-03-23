package com.afrikatek.documentsregistrationservice.repository.search;

import com.afrikatek.documentsregistrationservice.domain.Holiday;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Holiday} entity.
 */
public interface HolidaySearchRepository extends ElasticsearchRepository<Holiday, Long> {}
