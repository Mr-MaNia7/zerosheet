package com.mania.zerosheet.Transaction;

import javax.validation.Valid;

import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Items.ItemRepository;

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
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;

    // from home to transactions
    @GetMapping("/transactions")
    public String showTransactions(Transaction transaction, Model model) {
        model.addAttribute("transactions", transactionRepository.findAll());
        // System.out.println(transactionRepository.findAll()); // debug line
        return "Transactions/view-transactions";
    }
    
   
    // from transactions to update-transaction
    @GetMapping("transactions/edittransaction/{transId}")
    public String showUpdateTransactionForm(@PathVariable ("transId") long transId, Model model) {
        Transaction transaction =
            transactionRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Transaction Id: " + transId));
        model.addAttribute("transaction", transaction);
        return "Transactions/update-transaction";
    }
    // from update-transaction (post and) redirect to transactions
    @PostMapping("/updatetransaction/{transId}")
    public String updateTransaction(@Valid Transaction transaction, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "Transactions/update-transaction";
        }
        transactionRepository.save(transaction);
        model.addAttribute("transactions", transactionRepository.findAll());
        return "redirect:/view-transactions";
    }

    // @GetMapping("/customers/edittransaction/{id}")
    // public String showAddTransactionForm(@PathVariable ("id") long id, Model model) {
    //     Customer customer =
    //         customerRepository
    //             .findById(id)
    //             .orElseThrow(() -> new IllegalArgumentException("Invalid Customer Id: " + id));
    //     model.addAttribute("customer", customer);
    //     model.addAttribute("items", itemRepository.findAll());
    //     return "Forms/customer-transaction";
    // }    
    // @PostMapping("/customers/updatetransaction/{id}")
    // public String processTransactionExisting(@PathVariable ("id") long id, @Valid Transaction transaction, 
    // BindingResult result) {
    //     if (result.hasErrors()) {
    //         return "Forms/customer-transaction";
    //     }
    //     Customer customer =
    //         customerRepository
    //             .findById(id)
    //             .orElseThrow(() -> new IllegalArgumentException("Invalid Customer Id: " + id));
    //     customer.addTransaction(transaction);
    //     customerRepository.save(customer);
    //     System.out.println(customer); // debug line
    //     return "redirect:/customers";
    // } 
}
