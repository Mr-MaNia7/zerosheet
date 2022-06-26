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
    @GetMapping("transactions/edittransaction/{transId}")
    public String showUpdateTransactionForm(@PathVariable("transId") long transId , Model model){
        Transaction transaction =
            transactionRepository
            .findById(transId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));

        model.addAttribute("transaction", transaction);
        return "Transactions/update-transaction";
    }
    // from update-transaction (post and) redirect to customers
    @PostMapping("transactions/updatetransaction/{transId}")
    public String updateTransaction(@PathVariable("transId") long transId,
        @Valid Transaction transaction, BindingResult result, Model model){
        if (result.hasErrors()) {
            transaction.setTransId(transId);
            return "Transactions/update-transaction";
        }

        System.out.println("Incoming Transaction\n" + transaction.toString()); //debug line
        
        Transaction trans =
        transactionRepository
        .findById(transId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));
        
        // Get old obj and save it inside the new obj
        int item_quantity = transaction.getItemQuantity();
        Date due_back_date = transaction.getDueBackDate();
        Date due_date = transaction.getDueDate();        
        
        // calculating loan price per transaction
        double loan_price_per_day = trans.getItem().getUnitLoanPrice();
        long loan_days = calculateDayDifference(due_back_date, due_date);
        double loan_price_per_item = loan_price_per_day * loan_days;
        
        // calculating collateral price per transaction
        double collateral_price = trans.getItem().getUnitPrice()*item_quantity;
        double trans_price = trans.getItemQuantity()*loan_price_per_item;
        
        double old_collateral_price = trans.getCollateral();
        double old_trans_price = trans.getTransPrice();
        // System.out.println("Old collateral price" + old_collateral_price);
        
        trans.setItemQuantity(item_quantity);
        trans.setDueBackDate(due_back_date);
        trans.setDueDate(due_date);
        trans.setDayDifference(loan_days);
        trans.setCollateral(collateral_price);
        trans.setTransPrice(trans_price);

        // System.out.println("New collateral price" + collateral_price);

        Customer cust = trans.getCustomer();
        double old_total_collateral = cust.getTotalCollateral();
        double new_total_collateral = old_total_collateral - old_collateral_price + collateral_price;
        double old_total_price = cust.getTotalPrice();
        double new_total_price = old_total_price - old_trans_price + trans_price;
        double old_debt_balance = cust.getDebtBalance();
        double new_debt_balance = old_debt_balance - old_trans_price + trans_price;
        
        cust.setTotalPrice(new_total_price);
        cust.setTotalCollateral(new_total_collateral);
        cust.setDebtBalance(new_debt_balance);

        // System.out.println("Old Total collateral" + old_total_collateral);
        // System.out.println("New total collateral" + new_total_collateral);
        
        transactionRepository.save(trans);
        this.customerRepository.save(cust);
        model.addAttribute("customers", customerRepository.findAll());
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
