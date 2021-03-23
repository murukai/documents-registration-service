package com.afrikatek.documentsregistrationservice.repository;

import com.afrikatek.documentsregistrationservice.domain.Appointment;
import com.afrikatek.documentsregistrationservice.domain.AppointmentSlot;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppointmentSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
    Optional<List<AppointmentSlot>> findByAppointment(Appointment appointment);
}
