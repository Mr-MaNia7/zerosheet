package com.mania.zerosheet.Saved;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mania.zerosheet.Customers.Customer;

@Repository
public interface SavedAgreementRepository extends JpaRepository<SavedAgreement, Long> {
    Page<SavedAgreement> findAllByCustomer(Customer customer, Pageable pageable);
}
