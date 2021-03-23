package com.afrikatek.documentsregistrationservice.repository.search;

import com.afrikatek.documentsregistrationservice.domain.AppointmentSlot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AppointmentSlot} entity.
 */
public interface AppointmentSlotSearchRepository extends ElasticsearchRepository<AppointmentSlot, Long> {}
