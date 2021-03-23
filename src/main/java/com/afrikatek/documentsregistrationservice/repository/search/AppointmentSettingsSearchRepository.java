package com.afrikatek.documentsregistrationservice.repository.search;

import com.afrikatek.documentsregistrationservice.domain.AppointmentSettings;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AppointmentSettings} entity.
 */
public interface AppointmentSettingsSearchRepository extends ElasticsearchRepository<AppointmentSettings, Long> {}
