package com.mania.zerosheet.Customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import com.mania.zerosheet.Transaction.Transaction;
import com.mania.zerosheet.Transaction.TransactionRepository;
import org.springframework.web.bind.annotation.SessionAttributes;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@SessionAttributes("customer")
public class CustomerController {
  private final CustomerRepository customerRepository;
  private final TransactionRepository TransactionRepository;
  private final ItemRepository itemRepository;

  // from index, or redirect to customers
  @GetMapping("/customers")
  public String showCustomers(Customer customer, Model model, SessionStatus status){
    // status.setComplete();
    List<Long> remainingDaysList = new ArrayList<Long>();
    Date today = new Date();
    for (Transaction transaction : TransactionRepository.findAll()) {
      long remainingDays = transaction.calculateDayDifference(transaction.getDueBackDate(), today);
      remainingDaysList.add(remainingDays);
    }
    
    model.addAttribute("remainingDaysList", remainingDaysList);
    model.addAttribute("customers", customerRepository.findAll());
    return "Customers/view-customers";
  }
  @GetMapping("/customerstabular")
  public String showCustomersTabular(Customer customer, Model model, SessionStatus status){
    List<Long> remainingDaysList = new ArrayList<Long>();
    Date today = new Date();
    for (Transaction transaction : TransactionRepository.findAll()) {
      long remainingDays = transaction.calculateDayDifference(transaction.getDueBackDate(), today);
      remainingDaysList.add(remainingDays);
    }
    
    model.addAttribute("remainingDaysList", remainingDaysList);
    model.addAttribute("customers", customerRepository.findAll());
    return "Customers/view-customers-tabular";
  }

  @GetMapping("/cancel")
  public String cancelTransaction(SessionStatus status){
    status.setComplete();
    return "redirect:/customers";
  }
  // from customers to add-customer
  @GetMapping("customers/newcustomer")
  public String showAddCustomerForm(Customer customer, Model model) {
    model.addAttribute(customer);
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
    // System.out.println(customerRepository.findAll()); //debug line causes StackOverflow
    return "redirect:/customers"; //redirected to @GetMapping(/customers)
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
    return "redirect:/customers";
  }

  // from customers page
  @GetMapping("customers/deletecustomer/{id}")
  public String deleteUser(@PathVariable("id") long id, Model model) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
    
    for (Transaction transaction : customer.getTransactions()) {
      Item item = transaction.getItem();
      int oldQty = item.getTotalQuantity();
      item.setTotalQuantity(oldQty +  transaction.getItemQuantity());
      itemRepository.save(item);
    }
    customerRepository.delete(customer);
    model.addAttribute("customers", customerRepository.findAll());
    return "redirect:/customers";
  }
}
