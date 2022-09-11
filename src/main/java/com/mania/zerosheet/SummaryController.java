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
import com.mania.zerosheet.Saved.SavedAgreement;
import com.mania.zerosheet.Saved.SavedAgreementRepository;
import com.mania.zerosheet.Transaction.Transaction;
import com.mania.zerosheet.Transaction.TransactionRepository;
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
    private final SavedAgreementRepository savedAgreementRepo;
    private final TransactionRepository transactionRepository;

    @GetMapping("/summary/byitem")
    public String showSummaryByItem(Model model){
        model.addAttribute("items", itemRepository.findAll());
        return "Summary/summary-by-item";
    }

    @GetMapping("/summary/bycustomer")
    public String showSummaryByCustomer(Model model){
        model.addAttribute("customers", customerRepository.findAll());
        return "Summary/summary-by-customer";
    }

    @GetMapping("/orders/summary")
    public String showSummaryPage(Model model, @Valid Customer customer) {
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/summary";
    }
    @PostMapping("/orders/finish")
    public String finishSession(Model model, SessionStatus status, @Valid Customer customer) {
        for (Performa performa : customer.getPerformas()) {
            Item new_item = performa.getItem();
            new_item.setTotalQuantity(new_item.calculateItemQuantity(performa.getItem().getTotalQuantity(),
            performa.getItemQuantity(), 0));
            new_item.setLoanedQuantity(new_item.getLoanedQuantity() + performa.getItemQuantity());
            this.itemRepository.save(new_item);
        }
        
        customer.copyPerforma2Transaction();
        List<Performa> performas = new ArrayList<Performa>(customer.getPerformas());
        customer.removePerformas(customer.getPerformas());

        this.customerRepository.save(customer);
        SavedAgreement savedAgreement = customer.saveAgreement(performas);
        this.savedAgreementRepo.save(savedAgreement);
        status.setComplete();
        return "redirect:/customers";
    }
    @GetMapping("/orders/saveperforma")
    public String savePerforma(Model model, SessionStatus status, @Valid Customer customer) {
        this.customerRepository.save(customer);
        status.setComplete();
        return "redirect:/customers";
    }
    @GetMapping("orders/performas/{id}")
    public String continuePerforma(@PathVariable("id") long id, Model model) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/continued-summary";
    }
    @GetMapping("/orders/finishperforma/{id}")
    public String finishPerforma(@PathVariable("id") long id) {
        Customer customer = this.customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));

        for (Performa performa : customer.getPerformas()) {
            Item new_item = performa.getItem();
            new_item.setTotalQuantity(new_item.calculateItemQuantity(performa.getItem().getTotalQuantity(),
                performa.getItemQuantity(), 0));
            new_item.setLoanedQuantity(new_item.getLoanedQuantity() + performa.getItemQuantity());
            this.itemRepository.save(new_item);
        }
        customer.copyPerforma2Transaction();
        List<Performa> performas = new ArrayList<Performa>(customer.getPerformas());
        customer.removePerformas(customer.getPerformas());

        this.customerRepository.save(customer);
        this.performaRepository.deleteAll(performas);
        SavedAgreement savedAgreement = customer.saveAgreement(performas);
        this.savedAgreementRepo.save(savedAgreement);
        return "redirect:/customers";
    }
    @GetMapping("/orders/editedsummary/{id}/{transId}")
    public String showEditedSummaryPage(@PathVariable("id") long id, @PathVariable("transId") long transId, Model model) {
        Customer customer = 
            customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        Transaction transaction =
            transactionRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", transactions);
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/view-edited-summary";
    }
    @PostMapping("/orders/finishedit/{id}/{transId}")
    public String finishEditSession(Model model, SessionStatus status, @PathVariable("id") long id, @PathVariable("transId") long transId) {
        Customer customer = customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        Transaction transaction = transactionRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));
        this.customerRepository.save(customer);
        SavedAgreement savedAgreement = customer.saveEditedAgreement(transaction);
        this.savedAgreementRepo.save(savedAgreement);
        status.setComplete();
        return "redirect:/customers";
    }
    @GetMapping("/orders/updatesummary/{id}")
    public String showUpdateSummaryPage(@PathVariable("id") long id, Model model) {
        Customer customer = customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/update-summary";
    }
    @PostMapping("/orders/updatefinish/{id}")
    public String finishUpdateSession(Model model, SessionStatus status, @PathVariable("id") long id) {
        Customer customer = this.customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));

        for (Performa performa : customer.getPerformas()) {
            Item new_item = performa.getItem();
            new_item.setTotalQuantity(new_item.calculateItemQuantity(performa.getItem().getTotalQuantity(),
                performa.getItemQuantity(), 0));
            new_item.setLoanedQuantity(new_item.getLoanedQuantity() + performa.getItemQuantity());
            this.itemRepository.save(new_item);
        }
        customer.copyPerforma2Transaction();

        List<Performa> performas = new ArrayList<Performa>(customer.getPerformas());
        customer.removePerformas(customer.getPerformas());

        this.customerRepository.save(customer);
        this.performaRepository.deleteAll(performas);
        SavedAgreement savedAgreement = customer.saveAgreement(performas);
        this.savedAgreementRepo.save(savedAgreement);
        status.setComplete();
        return "redirect:/customers";
    }
}
