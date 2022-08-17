package com.mania.zerosheet;

import java.util.Date;

import javax.validation.Valid;
import com.mania.zerosheet.Company.CompanyRepository;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Performa.Performa;
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
        customer.getPerformas().forEach(performa -> performa.setCust(customer));

        // Calculating Total Price and Collateral Price
        double totalPrice = 0.0;
        double totalCollateral = 0.0;
        for (Performa performa : customer.getPerformas()) {
            totalPrice += performa.getTransPrice();
            totalCollateral += performa.getCollateral();
        }
        double totalPriceVAT = totalPrice + (totalPrice * 0.15);
        double totalCollateralVAT = totalCollateral + (totalCollateral * 0.15);

        customer.setTotalPriceP(totalPrice);
        customer.setTotalPriceVATP(totalPriceVAT);
        customer.setDebtBalanceP(totalPriceVAT);
        customer.setTotalCollateralP(totalCollateral);
        customer.setTotalCollateralVATP(totalCollateralVAT);

        Date today = new Date();
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
        model.addAttribute("company", companyRepository.findAll());
        model.addAttribute("today", today);
        return "Agreements/view-agreement";

    }
}
