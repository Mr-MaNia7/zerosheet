package com.mania.zerosheet.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransactionController {
    private final TransactionRepository transactionRepository;
    @Autowired
    public TransactionController(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/transactions")
    public String showTransactions(Transaction transaction, Model model) {
        model.addAttribute("transactions", transactionRepository.findAll());
        return "transactions";
    }

}
