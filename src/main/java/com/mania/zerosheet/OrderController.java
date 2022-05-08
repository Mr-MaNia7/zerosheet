package com.mania.zerosheet;

import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Transaction.Transaction;
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
    
    @GetMapping("/orders/current")
    public String orderForm(Model model) {
        return "Forms/customer-info";
    }
    @PostMapping("/orders")
    public String processOrder(@Valid Customer customer, 
    BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "Forms/orderForm";
        }
        
        // System.out.println(customer.getId()); // debug line
        customer.getTransactions().forEach(transaction -> transaction.setCustomer(customer));
        
        // Calculating Total Price and Collateral Price
        double totalPrice = 0.0;
        double totalCollateral = 0.0;
        for (Transaction transaction : customer.getTransactions()) {
            totalPrice += transaction.getTransPrice();
            totalCollateral += transaction.getCollateral();
        }
        customer.setTotalPrice(totalPrice);
        customer.setDebtBalance(totalPrice);
        customer.setTotalCollateral(totalCollateral);

        this.customerRepository.save(customer);
        status.setComplete();
        return "redirect:/customers";
    }
}
