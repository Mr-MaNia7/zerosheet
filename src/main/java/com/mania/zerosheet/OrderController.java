package com.mania.zerosheet;

import java.util.Date;

import javax.validation.Valid;
import com.mania.zerosheet.Company.CompanyRepository;
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
        double totalPriceVAT = totalPrice + (totalPrice * 0.15);
        customer.setTotalPriceVAT(totalPriceVAT);
        
        customer.setDebtBalance(totalPriceVAT);

        customer.setTotalCollateral(totalCollateral);
        double totalCollateralVAT = totalCollateral + (totalCollateral * 0.15);
        customer.setTotalCollateralVAT(totalCollateralVAT);
        
        Date today = new Date();
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getTransactions());
        model.addAttribute("company", companyRepository.findAll());
        model.addAttribute("today", today);
        return "Agreements/view-agreement";

    }
}
