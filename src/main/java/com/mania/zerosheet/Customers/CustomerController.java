package com.mania.zerosheet.Customers;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CustomerController {
  private final CustomerRepository customerRepository;

  // from index, or redirect to customers
  @GetMapping("/customers")
  public String showCustomers(Customer customer, Model model){
    model.addAttribute("customers", customerRepository.findAll());
    return "Customers/view-customers";
  }

  // from customers to add-customer
  @GetMapping("customers/newcustomer")
  public String showAddCustomerForm(Customer customer) {
    return "Customers/add-customer";
  }

  // from add-customer redirect to customers 
  @PostMapping("/addcustomer")
  public String addCustomer(@Valid Customer customer, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "Customers/add-customer";
    }
    customerRepository.save(customer);
    model.addAttribute("customers", customerRepository.findAll());
    return "redirect:/view-customers"; //redirected to @GetMapping(/customers)
  }

  // from customers to update-customer
  @GetMapping("customers/editcustomer/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
    model.addAttribute("customer", customer);
    return "Customers/update-customer";
  }
    
  // from update-customer redirect to customers
  @PostMapping("/update/{id}")
  public String updateUser(
    @PathVariable("id") long id, @Valid Customer customer, BindingResult result, Model model) {
    if (result.hasErrors()) {
      customer.setId(id);
      return "Customers/update-customer";
    }
    customerRepository.save(customer);
    model.addAttribute("customers", customerRepository.findAll());
    return "redirect:/view-customers";
  }

  // from customers page
  @GetMapping("customers/deletecustomer/{id}")
  public String deleteUser(@PathVariable("id") long id, Model model) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
    customerRepository.delete(customer);
    model.addAttribute("customers", customerRepository.findAll());
    return "redirect:/view-customers";
  }
}
