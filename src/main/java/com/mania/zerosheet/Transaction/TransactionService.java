package com.mania.zerosheet.Transaction;

import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Pagination.Paged;
import com.mania.zerosheet.Pagination.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public Paged<Transaction> getPageByCustomer(int pageNumber, int size, Customer customer) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("dueDate").descending());
        Page<Transaction> postPage = transactionRepository.findAllByCustomer(customer, request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }
    public Paged<Transaction> getPage(int pageNumber, int size, Sort sort) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, sort);
        Page<Transaction> postPage = transactionRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }
    public List<Long> calculateRemainingDays(Customer customer){
        List<Long> remainingDaysList = new ArrayList<Long>();
            for (Transaction transaction : transactionRepository.findAllByCustomer(customer)) {
                remainingDaysList.add(transaction.calculateDayDifference(transaction.getDueBackDate(), new Date()));
            } 
        return remainingDaysList;
    }
    public List<Long> calculateRemainingDays(){
        List<Long> remainingDaysList = new ArrayList<Long>();
            for (Transaction transaction : transactionRepository.findAll()) {
                remainingDaysList.add(transaction.calculateDayDifference(transaction.getDueBackDate(), new Date()));
            } 
        return remainingDaysList;
    }
    public void deleteTransaction(Transaction trans){
        List<Transaction> transs = trans.getCustomer().getTransactions();
        transs.remove(trans);
        trans.getCustomer().setTransactions(transs);
        trans.setCustomer(null);
        this.transactionRepository.delete(trans);
    }
}
