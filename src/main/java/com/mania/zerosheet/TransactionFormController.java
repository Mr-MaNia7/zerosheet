package com.mania.zerosheet;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Items.ItemRepository;
import com.mania.zerosheet.Transaction.Transaction;
import com.mania.zerosheet.Transaction.TransactionRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("customer")
public class TransactionFormController {
    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping("/transactions/newtransaction")
    public String showTransactionForm(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/item-transaction";
    }

    @GetMapping("/transactions/newtransaction/{transId}")
    public String showTransactionForm2(@PathVariable ("transId") long transId, Model model) {
        Transaction transaction =
        transactionRepository
        .findById(transId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));

        model.addAttribute("items", itemRepository.findAll());
        // model.addAttribute("item", transaction.getItem());
        model.addAttribute("transaction", transaction);
        return "Forms/item-transaction";
        // return "redirect:/transactions/newtransaction";
    }

    @ModelAttribute(name = "customer")
    public Customer order() {
        return new Customer();
    }
    @ModelAttribute(name = "transaction")
    public Transaction transaction() {
        return new Transaction();
    }

    @PostMapping("/addtransaction")
    public String processTransaction(@Valid Transaction transaction, BindingResult result,
    @ModelAttribute Customer order, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("items", itemRepository.findAll());
            return "Forms/item-transaction";
        }
        // Calcualting Logic
        // calculating date difference
        Date fromDate = transaction.getDueBackDate();
        Date toDate = transaction.getDueDate();        
        long loan_days = calculateDayDifference(fromDate, toDate);
        transaction.setDayDifference(loan_days);

        // calculating loan price per transaction
        double loan_price_per_day = transaction.getItem().getUnitLoanPrice();
        double loan_price_per_item = loan_price_per_day * loan_days;
        transaction.setTransPrice(transaction.getItem().getAreaCoverage()*loan_price_per_item);
        
        // calculating collateral price per transaction 100%
        double collateral_price = transaction.getItem().getUnitPrice()*transaction.getItemQuantity();
        transaction.setCollateral(collateral_price);

        order.addTransaction(transaction);
        // transactionRepository.save(transaction);
        // System.out.println(order.getTransactions()); // debug line
        return "redirect:/orders/current";
    }
    
    
    public long calculateDayDifference(Date fromDate, Date toDate) {
        long difference_In_Time = fromDate.getTime() - toDate.getTime();
        long difference_In_Days = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time);
    
        // long difference_In_Years = 
        // TimeUnit
        //       .MILLISECONDS
        //       .toDays(difference_In_Time)
        //   / 365l;
        return difference_In_Days;
    }
}
