package com.mania.zerosheet.Saved;

import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Pagination.Paged;
import com.mania.zerosheet.Pagination.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SavedService {
    private final SavedAgreementRepository savedAgreementRepository;

    public SavedService(SavedAgreementRepository savedAgreementRepository){
        this.savedAgreementRepository = savedAgreementRepository;
    }

    public Paged<SavedAgreement> getPage(int pageNumber, int size, Customer customer) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("agreementDate").descending());
        Page<SavedAgreement> postPage = savedAgreementRepository.findAllByCustomer(customer, request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }
    
}
