package com.mania.zerosheet.Transaction;

import javax.validation.Valid;

import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Items.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    @Autowired
    public TransactionController(TransactionRepository transactionRepository,
        ItemRepository itemRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
    }

    // from home to transactions
    @GetMapping("/transactions")
    public String showTransactions(Transaction transaction, Model model) {
        model.addAttribute("transactions", transactionRepository.findAll());
        return "Transactions/transactions";
    }
    // from transactions to add-transaction
    @GetMapping("/transactions/newtransaction")
    public String showAddTransactionForm(Transaction transaction, Model model) {
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("customers", customerRepository.findAll());
        return "Transactions/add-transaction";
        // return "redirect:/form/customer";
    }
    // from add-transaction (post and ) redirect to transactions
    @PostMapping("/addtransaction")
    public String addTransaction(@Valid Transaction transaction, BindingResult result, Model model){
        // if (result.hasErrors()) {
        //     model.addAttribute("items", itemRepository.findAll());
        //     model.addAttribute("customers", customerRepository.findAll());
        //     return "Transactions/add-transaction";
        // }
        transactionRepository.save(transaction);
        model.addAttribute("transactions", transactionRepository.findAll());
        return "redirect:/transactions";
    }

    // from transactions (delete and) redirect to transactions
    @GetMapping("transactions/deletetransaction/{transId}")
    public String deleteTransaction(@PathVariable("transId") long transId, Model model) {
        Transaction transaction = 
            transactionRepository
            .findById(transId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Transaction Id: " + transId));
            transactionRepository.delete(transaction);
            model.addAttribute("transactions", transactionRepository.findAll());
            return "redirect:/transactions";
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
        return "redirect:/transactions";
    }
}
