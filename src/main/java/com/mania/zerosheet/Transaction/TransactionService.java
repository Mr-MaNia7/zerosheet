package com.mania.zerosheet.Transaction;

import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Pagination.Paged;
import com.mania.zerosheet.Pagination.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public Paged<Transaction> getPage(int pageNumber, int size, Customer customer) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("transId").ascending());
        Page<Transaction> postPage = transactionRepository.findAllByCustomer(customer, request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }
}
