package com.afrikatek.documentsregistrationservice.repository;

import com.afrikatek.documentsregistrationservice.domain.Address;
import com.afrikatek.documentsregistrationservice.domain.Applicant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<List<Address>> findByApplicant(Applicant applicant);
}
