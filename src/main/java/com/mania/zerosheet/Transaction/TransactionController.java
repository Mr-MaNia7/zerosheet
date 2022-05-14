package com.mania.zerosheet.Transaction;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Customers.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    // from home to transactions
    @GetMapping("/transactions")
    public String showTransactions(Transaction transaction, Model model) {
        model.addAttribute("transactions", transactionRepository.findAll());
        // System.out.println(transactionRepository.findAll()); // debug line
        return "Transactions/view-transactions";
    }
    @GetMapping("transactions/edittransaction/{id}/{transId}")
    public String showUpdateTransactionForm(@PathVariable("id") long id, @PathVariable("transId") long transId , Model model) {
        Transaction transaction =
            transactionRepository
            .findById(transId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));
            
        Customer customer =
            customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        model.addAttribute("transaction", transaction);
        model.addAttribute("customer", customer);
        return "Transactions/update-transaction";
    }
    // from update-transaction (post and) redirect to customers
    @PostMapping("transactions/updatetransaction/{id}/{transId}")
    public String updateTransaction(@PathVariable("id") long id, @PathVariable("transId") long transId , @Valid Transaction transaction, BindingResult result, Model model){
        if (result.hasErrors()) {
            transaction.setTransId(transId);
            return "Transactions/update-transaction";
        }
        Customer order =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        // Calcualting Logic
        // calculating date difference
        Date fromDate = transaction.getDueBackDate();
        Date toDate = transaction.getDueDate();
        long loan_days = calculateDayDifference(fromDate, toDate);
        transaction.setDayDifference(loan_days);

        // calculating loan price per transaction
        double loan_price_per_day = transaction.getItem().getUnitLoanPrice();
        double loan_price_per_item = loan_price_per_day * loan_days;
        transaction.setTransPrice(transaction.getItemQuantity()*loan_price_per_item);
        
        // calculating collateral price per transaction
        double collateral_price = transaction.getItem().getUnitPrice()*transaction.getItemQuantity();
        transaction.setCollateral(collateral_price);
        
        order.updateTransaction(transaction, id);
        customerRepository.save(order);
        // System.out.println(order.getTransactions()); // debug line
        return "redirect:/customers";
    }
    public long calculateDayDifference(Date fromDate, Date toDate) {
        long difference_In_Time = fromDate.getTime() - toDate.getTime();
        long difference_In_Days = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time);
    
        long difference_In_Years = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time)
          / 365l;
        return difference_In_Days;
    }  
}
