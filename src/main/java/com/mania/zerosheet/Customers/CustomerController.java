package com.mania.zerosheet.Customers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomerController {
  private final CustomerRepository customerRepository;
  @Autowired
  public CustomerController(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }
  
  // from everywhere
  @GetMapping("/")
  public String showHomePage(Customer customer, Model model){
    model.addAttribute("customers", customerRepository.findAll());
    return "index";
  }

  // from index
  @GetMapping("/new")
  public String showSignUpForm(Customer customer) {
    return "add-customer";
  }

  // from index
  @GetMapping("/edit/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
    model.addAttribute("customer", customer);
    return "update-customer";
  }
  
  // from index
  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable("id") long id, Model model) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
    customerRepository.delete(customer);
    model.addAttribute("customers", customerRepository.findAll());
    return "index";
  }

  // from add-customer
  @PostMapping("/addcustomer")
  public String addUser(@Valid Customer customer, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "add-customer";
    }
    customerRepository.save(customer);
    model.addAttribute("customers", customerRepository.findAll());
    return "index";
  }

  // from update-customer
  @PostMapping("/update/{id}")
  public String updateUser(
      @PathVariable("id") long id, @Valid Customer customer, BindingResult result, Model model) {
    if (result.hasErrors()) {
      customer.setId(id);
      return "update-customer";
    }
    customerRepository.save(customer);
    model.addAttribute("customers", customerRepository.findAll());
    return "index";
  }
}
