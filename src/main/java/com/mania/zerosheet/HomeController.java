package com.mania.zerosheet;

import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import com.mania.zerosheet.Performa.PerformaRepository;
import com.mania.zerosheet.Transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final PerformaRepository performaRepository;
    
    @GetMapping("/")
    public String showHomePage(Item item, Model model){
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("transactions", transactionRepository.findAll());
        model.addAttribute("performas", performaRepository.findAll());
        return "index";
    }
}
