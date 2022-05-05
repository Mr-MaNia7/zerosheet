package com.mania.zerosheet;

import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Transaction.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("customer")
public class TransactionFormController {
    @GetMapping("/transactions/newtransaction")
    public String showTransactionForm(Model model) {
        return "Forms/item-transaction";
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
    public String processTransaction(@Valid Transaction transaction, 
    @ModelAttribute Customer order, BindingResult result) {
        if (result.hasErrors()) {
            return "Forms/item-transaction";
        }
        order.addTransaction(transaction);
        // System.out.println(order.getTransactions()); // debug line
        return "redirect:/orders/current";
    }   
}
