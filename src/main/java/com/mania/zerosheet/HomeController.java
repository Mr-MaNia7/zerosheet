package com.mania.zerosheet;

import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import com.mania.zerosheet.Transaction.TransactionRepository;

import lombok.RequiredArgsConstructor;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    // @Autowired
    // public HomeController(ItemRepository itemRepository) {
    //     this.itemRepository = itemRepository;
    //     this.
    // }
    @GetMapping("/")
    public String showHomePage(Item item, Model model){
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("transactions", transactionRepository.findAll());
        return "index";
    }
    @GetMapping("/contact")
    public String showContactPage(Model model){
        // model.addAttribute(arg0, arg1)
        return "Contact/contact";
    }
}
