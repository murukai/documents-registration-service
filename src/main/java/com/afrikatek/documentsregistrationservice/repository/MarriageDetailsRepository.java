package com.afrikatek.documentsregistrationservice.repository;

import com.afrikatek.documentsregistrationservice.domain.Applicant;
import com.afrikatek.documentsregistrationservice.domain.MarriageDetails;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MarriageDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarriageDetailsRepository extends JpaRepository<MarriageDetails, Long> {
    Optional<MarriageDetails> findByApplicant(Applicant applicant);
}
