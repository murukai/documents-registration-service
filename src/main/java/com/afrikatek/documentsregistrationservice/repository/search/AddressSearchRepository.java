package com.afrikatek.documentsregistrationservice.repository.search;

import com.afrikatek.documentsregistrationservice.domain.Address;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Address} entity.
 */
public interface AddressSearchRepository extends ElasticsearchRepository<Address, Long> {}
