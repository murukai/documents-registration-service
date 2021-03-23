package com.afrikatek.documentsregistrationservice.repository;

import com.afrikatek.documentsregistrationservice.domain.Applicant;
import com.afrikatek.documentsregistrationservice.domain.NextOfKeen;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NextOfKeen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextOfKeenRepository extends JpaRepository<NextOfKeen, Long> {
    Optional<NextOfKeen> findByApplicant(Applicant applicant);
}
