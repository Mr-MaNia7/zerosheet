package com.mania.zerosheet;

import javax.validation.Valid;
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
@SessionAttributes("customerOrder")
public class OrderController {
    private final CustomerRepository customerRepository;

    @GetMapping("/orders/current")
    public String orderForm(Model model) {
        return "Forms/orderForm";
    }
    @PostMapping("/orders")
    public String processOrder(@Valid Customer customer, 
    BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "Forms/orderForm";
        }
        
        this.customerRepository.save(customer);
        status.setComplete();
        return "redirect:/transactions";
    }
}
