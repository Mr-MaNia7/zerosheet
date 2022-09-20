package com.mania.zerosheet.Customers;

import com.mania.zerosheet.Pagination.Paged;
import com.mania.zerosheet.Pagination.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Paged<Customer> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<Customer> postPage = customerRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }
}
