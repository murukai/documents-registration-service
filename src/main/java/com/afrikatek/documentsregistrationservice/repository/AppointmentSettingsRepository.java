package com.afrikatek.documentsregistrationservice.repository;

import com.afrikatek.documentsregistrationservice.domain.AppointmentSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppointmentSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentSettingsRepository extends JpaRepository<AppointmentSettings, Long> {}
