package com.mania.zerosheet.Transaction;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mania.zerosheet.Customers.Customer;

@Repository
public interface TransactionRepository extends 
    JpaRepository<Transaction, Long>{
        Page<Transaction> findAllByCustomer(Customer customer, Pageable pageable);
        List<Transaction> findAllByCustomer(Customer customer);
}
