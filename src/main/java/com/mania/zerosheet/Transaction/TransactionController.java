package com.mania.zerosheet.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import com.mania.zerosheet.Performa.Performa;
import com.mania.zerosheet.Performa.PerformaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import lombok.RequiredArgsConstructor;
import com.mania.zerosheet.Company.CompanyRepository;
import com.mania.zerosheet.Customers.CustomerRepository;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@SessionAttributes("customer")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final CompanyRepository companyRepository;
    private final PerformaRepository performaRepository;

    // from home to transactions
    @GetMapping("/transactions")
    public String showTransactions(Transaction transaction, Model model) {
        model.addAttribute("transactions", transactionRepository.findAll());
        return "Transactions/view-transactions";
    }

    @GetMapping("/transactions/newtransaction")
    public String showTransactionForm(Performa performa, Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/item-transaction";
    }
    @ModelAttribute(name = "customer")
    public Customer order() {
        return new Customer();
    }
    @ModelAttribute(name = "transaction")
    public Transaction transaction() {
        return new Transaction();
    }
    @PostMapping("/addtransaction")
    public String processTransaction(@Valid Performa performa, BindingResult result,
    @ModelAttribute Customer order, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("items", itemRepository.findAll());
            return "Forms/item-transaction";
        }
        performa.addNewTransaction();
        order.addPerforma(performa);
        return "redirect:/orders/current";
    }

    // Experimental method
    @GetMapping("/transactions/newtransaction/{transId}")
    public String showTransactionForm2(@PathVariable ("transId") long transId, @ModelAttribute Customer order, Model model) {
        Transaction transn =
        transactionRepository
            .findById(transId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));
        
        // order.addTransaction(transn);
        // System.out.println(transn.getItem().getItemName());
        model.addAttribute("items", itemRepository.findAll());
        // model.addAttribute("item", transn.getItem());
        model.addAttribute("transaction", transn);
        return "Forms/item-transaction";
        // return "redirect:/transactions/newtransaction";
    }

    @GetMapping("transactions/edittransaction/{transId}")
    public String showUpdateTransactionForm(@PathVariable("transId") long transId , Model model){
        Transaction transaction =
            transactionRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));

        model.addAttribute("transaction", transaction);
        model.addAttribute("items", itemRepository.findAll());
        return "Transactions/update-transaction";
    }
    // from update-transaction (post and) redirect to customers
    @PostMapping("transactions/updatetransaction/{transId}")
    public String updateTransaction(@PathVariable("transId") long transId,
    @Valid Transaction new_trans, BindingResult result, Model model){
        if (result.hasErrors()) {
            new_trans.setTransId(transId);
            return "Transactions/update-transaction";
        }
        Item new_item =
            itemRepository
                .findById(new_trans.getItem().getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + new_trans.getItem().getItemId()));
        Transaction old_trans =
            transactionRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));
        
        Item item = old_trans.editTransaction(new_trans, new_item);
        itemRepository.save(item);        
        transactionRepository.save(old_trans);

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(old_trans);
        model.addAttribute("customer", old_trans.getCustomer());
        model.addAttribute("transactions", transactions);
        model.addAttribute("company", companyRepository.findAll());
        model.addAttribute("today", new Date());
        return "Agreements/view-edited-agreement";
    }
    
    @GetMapping("transactions/addtransaction/{id}")
    public String showAddTransactionForm(@PathVariable("id") long id , Model model, Transaction transaction){
        Customer customer =
            customerRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));

        model.addAttribute("customer", customer);
        model.addAttribute("items", itemRepository.findAll());
        return "Forms/customer-transaction";
    }

    @PostMapping("/customer/{id}/addtransaction")
    public String addCustomerTransaction(@PathVariable("id") long id,
    @Valid Performa performa, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "Forms/customer-transaction";
        }
        Customer customer =
            customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        performa.addTrans2ExistinCust(customer);        
        performaRepository.save(performa);

        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
        model.addAttribute("company", companyRepository.findAll());
        model.addAttribute("today", new Date());
        return "Agreements/view-updated-agreement";
    }

    @GetMapping("transactions/remove/{transId}")
    public String deleteTransaction(@PathVariable("transId") long transId, Model model){
        Transaction transaction =
            transactionRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id:" + transId));
        
        transaction.removeTransaction();
        transactionRepository.delete(transaction);
        return "redirect:/customers";
    }

    @GetMapping("transactions/return/{transId}")
    public String returnItems(@PathVariable("transId") long transId, @RequestParam(value="returnQuantity") int returnQuantity, Model model){
        Transaction transaction =
            transactionRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id:" + transId));
        
        boolean isDel = transaction.partialReturn(returnQuantity);
        if (isDel == true) {
            transactionRepository.delete(transaction);
        }
        else {
            transactionRepository.save(transaction);
        }
        return "redirect:/customers";
    }
}