package com.mania.zerosheet;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import com.mania.zerosheet.Performa.Performa;
import com.mania.zerosheet.Performa.PerformaRepository;
import com.mania.zerosheet.Transaction.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@SessionAttributes("customer")
public class SummaryController {
    private final ItemRepository itemRepository;
    private final CustomerRepository customerRepository;
    private final PerformaRepository performaRepository;

    @GetMapping("/orders/summary")
    public String showSummaryPage(Model model, @Valid Customer customer) {
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/summary";
    }

    @PostMapping("/orders/finish")
    public String finishSession(Model model, SessionStatus status, @Valid Customer customer) {
        // copy the performa into transaction
        for (Performa performa : customer.getPerformas()) {
            Item new_item = performa.getItem();
            int newQty = calculateItemQuantity(performa.getItem().getTotalQuantity(), performa.getItemQuantity(), 0);
            new_item.setTotalQuantity(newQty);
            this.itemRepository.save(new_item);

            Transaction transaction = new Transaction(
                performa.getItemQuantity(), performa.getDueDate(), performa.getDueBackDate(), performa.getDayDifference(), 
                performa.getCollateral(), performa.getTransPrice(), performa.getItemPrice(), performa.getCust(), performa.getItem()
            );
            customer.addTransaction(transaction);
        } 

        customer.setTotalPrice(customer.getTotalPriceP());
        customer.setTotalPriceVAT(customer.getTotalPriceVATP());
        customer.setDebtBalance(customer.getTotalPriceVATP());
        customer.setTotalCollateral(customer.getTotalCollateralP());
        customer.setTotalCollateralVAT(customer.getTotalCollateralVATP());
        
        customer.setTotalPriceP(0);
        customer.setTotalPriceVATP(0);
        customer.setDebtBalanceP(0);
        customer.setTotalCollateralP(0);
        customer.setTotalCollateralVATP(0);

        customer.removePerformas(customer.getPerformas());

        this.customerRepository.save(customer);
        status.setComplete();
        return "redirect:/customers";
    }

    @PostMapping("/orders/saveperforma")
    public String savePerforma(Model model, SessionStatus status, @Valid Customer customer) {
        this.customerRepository.save(customer);
        status.setComplete();
        return "redirect:/customers";
    }

    @GetMapping("orders/performas/{id}")
    public String continuePerforma(@PathVariable("id") long id, Model model) {
        Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/continued-summary";
    }

    @GetMapping("/orders/finishperforma/{id}")
    public String finishPerforma(@PathVariable("id") long id) {
        Customer customer = 
            this.customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));

        // copy the performa into transaction
        for (Performa performa : customer.getPerformas()) {
            Item new_item = performa.getItem();
            int newQty = calculateItemQuantity(performa.getItem().getTotalQuantity(), performa.getItemQuantity(), 0);
            new_item.setTotalQuantity(newQty);
            this.itemRepository.save(new_item);

            Transaction transaction = new Transaction(
                performa.getItemQuantity(), performa.getDueDate(), performa.getDueBackDate(), performa.getDayDifference(), 
                performa.getCollateral(), performa.getTransPrice(), performa.getItemPrice(), performa.getCust(), performa.getItem()
            );
            customer.addTransaction(transaction);
        }
     
        customer.setTotalPrice(customer.getTotalPriceP());
        customer.setTotalPriceVAT(customer.getTotalPriceVATP());
        customer.setDebtBalance(customer.getTotalPriceVATP());
        customer.setTotalCollateral(customer.getTotalCollateralP());
        customer.setTotalCollateralVAT(customer.getTotalCollateralVATP());
        
        customer.setTotalPriceP(0);
        customer.setTotalPriceVATP(0);
        customer.setDebtBalanceP(0);
        customer.setTotalCollateralP(0);
        customer.setTotalCollateralVATP(0);
        
        List<Performa> performas = new ArrayList<Performa>(customer.getPerformas());
        customer.removePerformas(customer.getPerformas());
        
        this.customerRepository.save(customer);
        this.performaRepository.deleteAll(performas);
        System.out.println(customer.getPerformas().isEmpty());
        System.out.println(this.performaRepository.count());
        return "redirect:/customers";
    }

    @GetMapping("/orders/editedsummary/{id}")
    public String showEditedSummaryPage(@PathVariable("id") long id, Model model) {
        Customer customer =
            customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getTransactions());
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/view-edited-summary";
    }
    
    @GetMapping("/orders/updatesummary/{id}")
    public String showUpdateSummaryPage(@PathVariable("id") long id, Model model) {
        Customer customer =
            customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/update-summary";
    }

    @PostMapping("/orders/finishedit/{id}")
    public String finishEditSession(Model model, SessionStatus status, @PathVariable("id") long id) {
        Customer customer =
            customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        this.customerRepository.save(customer);
        status.setComplete();
        return "redirect:/customers";
    }   

    @PostMapping("/orders/updatefinish/{id}")
    public String finishUpdateSession(Model model, SessionStatus status, @PathVariable("id") long id) {
        Customer customer = 
            this.customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));

        // copy the performa into transaction
        for (Performa performa : customer.getPerformas()) {
            Item new_item = performa.getItem();
            int newQty = calculateItemQuantity(performa.getItem().getTotalQuantity(), performa.getItemQuantity(), 0);
            new_item.setTotalQuantity(newQty);
            this.itemRepository.save(new_item);

            Transaction transaction = new Transaction(
                performa.getItemQuantity(), performa.getDueDate(), performa.getDueBackDate(), performa.getDayDifference(), 
                performa.getCollateral(), performa.getTransPrice(), performa.getItemPrice(), performa.getCust(), performa.getItem()
            );
            customer.addTransaction(transaction);
        }
     
        customer.setTotalPrice(customer.getTotalPriceP());
        customer.setTotalPriceVAT(customer.getTotalPriceVATP());
        customer.setDebtBalance(customer.getTotalPriceVATP());
        customer.setTotalCollateral(customer.getTotalCollateralP());
        customer.setTotalCollateralVAT(customer.getTotalCollateralVATP());
        
        customer.setTotalPriceP(0);
        customer.setTotalPriceVATP(0);
        customer.setDebtBalanceP(0);
        customer.setTotalCollateralP(0);
        customer.setTotalCollateralVATP(0);
        
        List<Performa> performas = new ArrayList<Performa>(customer.getPerformas());
        customer.removePerformas(customer.getPerformas());
        
        this.customerRepository.save(customer);
        this.performaRepository.deleteAll(performas);
        status.setComplete();
        return "redirect:/customers";
    }
    public int calculateItemQuantity(int old_item_quantity, int trans_item_quantity, int old_trans_quantity){
        return old_item_quantity - trans_item_quantity + old_trans_quantity;
    }
    public void deletePerforma(long transId) {
        Performa performa =
            this.performaRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid performa Id:" + transId));
        this.performaRepository.delete(performa);
    }
}
