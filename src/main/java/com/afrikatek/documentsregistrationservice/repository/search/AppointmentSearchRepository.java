package com.afrikatek.documentsregistrationservice.repository.search;

import com.afrikatek.documentsregistrationservice.domain.Appointment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Appointment} entity.
 */
public interface AppointmentSearchRepository extends ElasticsearchRepository<Appointment, Long> {}
