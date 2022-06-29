package com.mania.zerosheet;

import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Items.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@SessionAttributes("customer")
public class SummaryController {
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;

    @PostMapping("/orders/summary")
    public String showSummaryPage(Model model, @Valid Customer customer) {
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getTransactions());
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/summary";
    }

    @PostMapping("/orders/finish")
    public String finishSession(Model model, SessionStatus status, @Valid Customer customer) {
        this.customerRepository.save(customer);
        status.setComplete();
        return "redirect:/customers";
    }

    @GetMapping("/orders/updatesummary/{id}")
    public String showUpdateSummaryPage(@PathVariable("id") long id, Model model) {
        Customer customer =
            customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getTransactions());
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/update-summary";
    }

    @PostMapping("/orders/updatefinish/{id}")
    public String finishUpdateSession(Model model, SessionStatus status, @PathVariable("id") long id) {
        Customer customer =
            customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        this.customerRepository.save(customer);
        status.setComplete();
        return "redirect:/customers";
    }
}
