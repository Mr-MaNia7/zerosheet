package com.mania.zerosheet;

import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Items.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FormController {
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    @Autowired
    public FormController(ItemRepository itemRepository, CustomerRepository customerRepository ) {
        this.itemRepository = itemRepository;
        this.customerRepository = customerRepository;
    }

    // redirected from transactions
    @GetMapping("/form/customer")
    public String showCutomerTransactionForm(Customer customer) {
        return "Forms/add-customer-form";
    }
    // 
    // @PostMapping("/addcustomerform")
    // public String addCustomerTransaction(@Valid Customer customer,BindingResult result) {
    //     if (result.hasErrors()) {
    //         return "add-customer-form";
    //     }
    //     customerRepository.save(customer);
    //     tempcustomer = Customer 
    //         .findById()
    // }
}
