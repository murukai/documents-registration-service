package com.afrikatek.documentsregistrationservice.repository;

import com.afrikatek.documentsregistrationservice.domain.Applicant;
import com.afrikatek.documentsregistrationservice.domain.AppointmentSlot;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Applicant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    @Query("select applicant from Applicant applicant where applicant.user.login = ?#{principal.username}")
    List<Applicant> findByUserIsCurrentUser();

    Optional<List<Applicant>> findByAppointmentSlot(AppointmentSlot appointmentSlot);
}
