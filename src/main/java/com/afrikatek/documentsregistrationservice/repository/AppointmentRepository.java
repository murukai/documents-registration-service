package com.afrikatek.documentsregistrationservice.repository;

import com.afrikatek.documentsregistrationservice.domain.Appointment;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Appointment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<List<Appointment>> findByDateOfAppointment(Instant dateOfAppointment);
}
