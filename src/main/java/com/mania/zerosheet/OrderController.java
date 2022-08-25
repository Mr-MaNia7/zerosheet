package com.mania.zerosheet;

import java.util.Date;
import javax.validation.Valid;
import com.mania.zerosheet.Company.CompanyRepository;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Customers.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@SessionAttributes("customer")
public class OrderController {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    
    @GetMapping("/orders/current")
    public String orderForm(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "Forms/customer-info";
    }
    @PostMapping("/orders/agreement")
    public String processOrder(@Valid Customer customer, 
    BindingResult result, SessionStatus status, Model model) {
        if (result.hasErrors()) {
            return "Forms/customer-info";
        }
        customer.setOrderCost();
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
        model.addAttribute("company", companyRepository.findAll());
        model.addAttribute("today", new Date());
        return "Agreements/view-agreement";
    }
}
