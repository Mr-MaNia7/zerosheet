package com.mania.zerosheet;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import com.mania.zerosheet.Transaction.Transaction;
import com.mania.zerosheet.Transaction.TransactionRepository;

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
@SessionAttributes("customerOrder")
public class TransactionFormController {
    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;
    @GetMapping("/transactions/newtransaction")
    public String showTransactionForm(Model model) {
        // model.addAttribute("items", itemRepository.findAll());

        // List<Item> items = new ArrayList<>();
        // this.itemRepository.findAll().forEach(i -> items.add(i));

        return "Forms/item-transaction";
    }

    @ModelAttribute(name = "customerOrder")
    public Customer customer() {
        return new Customer();
    }
    @ModelAttribute(name = "transaction")
    public Transaction transaction() {
        return new Transaction();
    }

    @PostMapping("/addtransaction")
    public String processTransaction(@Valid Transaction transaction, 
    @ModelAttribute Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            return "Forms/item-transaction";
        }
        customer.addTransaction(transaction);
        transactionRepository.save(transaction);
        System.out.println(customer.getTransactions());
        return "redirect:/orders/current";
    }   
}
