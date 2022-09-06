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
import com.mania.zerosheet.ItemInstance.Instance;
import com.mania.zerosheet.ItemInstance.InstanceRepository;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import com.mania.zerosheet.Performa.Performa;
import com.mania.zerosheet.Transaction.Transaction;
import com.mania.zerosheet.Transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CustomerController {
  private final CustomerRepository customerRepository;
  private final TransactionRepository transactionRepository;
  private final ItemRepository itemRepository;
  private final InstanceRepository instanceRepository;
  private boolean isDuplicate = false;

  @GetMapping("/customers")
  public String showCustomers(Customer customer, Model model){
    List<Long> remainingDaysList = new ArrayList<Long>();
    Date today = new Date();
    for (Transaction transaction : transactionRepository.findAll()) {
      long remainingDays = transaction.calculateDayDifference(transaction.getDueBackDate(), today);
      remainingDaysList.add(remainingDays);
    }
    
    model.addAttribute("remainingDaysList", remainingDaysList);
    model.addAttribute("customers", customerRepository.findAll());
    return "Customers/view-customers";
  }
  @GetMapping("/customerstabular")
  public String showCustomersTabular(Customer customer, Model model){
    List<Long> remainingDaysList = new ArrayList<Long>();
    Date today = new Date();
    for (Transaction transaction : transactionRepository.findAll()) {
      long remainingDays = transaction.calculateDayDifference(transaction.getDueBackDate(), today);
      remainingDaysList.add(remainingDays);
    }
    
    model.addAttribute("remainingDaysList", remainingDaysList);
    model.addAttribute("customers", customerRepository.findAll());
    return "Customers/view-customers-tabular";
  }

  @GetMapping("customers/newcustomer")
  public String showAddCustomerForm(Customer customer, Model model) {
    model.addAttribute("isDuplicate", isDuplicate);
    return "Customers/add-customer";
  }

  @PostMapping("/addcustomer")
  public String addCustomer(@Valid Customer customer, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "Customers/add-customer";
    }
    isDuplicate = false;
    customer.setFullName(customer.getName() + customer.getMiddleName() + customer.getLastName());
    for (Customer cust : customerRepository.findAll()){
      if (
        customer.getFullName().equals(cust.getFullName()) || 
        customer.getEmail().equals(cust.getEmail()) ||
        customer.getPhoneNumber().equals(cust.getPhoneNumber())
      ){
        isDuplicate = true;
        return "redirect:/customers/newcustomer";
      }
    }
    customerRepository.save(customer);
    model.addAttribute("customers", customerRepository.findAll());
    return "redirect:/customers";
  }

  @GetMapping("customers/editcustomer/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
    model.addAttribute("customer", customer);
    return "Customers/update-customer";
  }
    
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
      item.updateAvailableInstance();
      
      Instance instance = transaction.getInstance();
      transaction.setInstance(null);
      instance.getItem().removeInstance(instance);
      this.instanceRepository.delete(instance);
      
      this.itemRepository.save(item);
      transaction.setCustomer(null);
    }
    for (Performa performa : customer.getPerformas()){
      performa.setCust(null);
    }
  
    this.customerRepository.delete(customer);
    return "redirect:/customers";
  }
}
