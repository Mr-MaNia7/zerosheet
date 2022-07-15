package com.mania.zerosheet.Transaction;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;

import com.mania.zerosheet.Company.CompanyRepository;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Customers.CustomerRepository;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
// @SessionAttributes("customer")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final CompanyRepository companyRepository;

    // from home to transactions
    @GetMapping("/transactions")
    public String showTransactions(Transaction transaction, Model model) {
        model.addAttribute("transactions", transactionRepository.findAll());
        // System.out.println(transactionRepository.findAll()); // debug line
        return "Transactions/view-transactions";
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
    @Valid Transaction transaction, BindingResult result, Model model){
        if (result.hasErrors()) {
            transaction.setTransId(transId);
            return "Transactions/update-transaction";
        }

        // System.out.println("Incoming Transaction\n" + transaction.getItem().getItemName()); //debug line
        
        Transaction trans =
        transactionRepository
        .findById(transId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id: " + transId));
        
        // Get old object and save it inside the new object
        // Item item = trans.getItem();
        // System.out.println(item.toString());
        int item_quantity = transaction.getItemQuantity();
        Date due_back_date = transaction.getDueBackDate();
        Date due_date = transaction.getDueDate();
        
        // calculating loan price per transaction
        double loan_price_per_day = trans.getItem().getUnitLoanPrice();
        long loan_days = calculateDayDifference(due_back_date, due_date);
        double loan_price_per_item = loan_price_per_day * loan_days;
        
        // calculating collateral price per transaction
        double collateral_price = trans.getItem().getUnitPrice()*item_quantity;
        double trans_price = trans.getItemQuantity()*loan_price_per_item;
        
        double old_collateral_price = trans.getCollateral();
        double old_trans_price = trans.getTransPrice();
        int old_item_quantity = trans.getItemQuantity();
        // System.out.println("Old collateral price" + old_collateral_price);

        // calculate remaining item total quantity
        Item item = trans.getItem();
        int oldQty = item.getTotalQuantity();
        int newQty = oldQty + (old_item_quantity - transaction.getItemQuantity());
        item.setTotalQuantity(newQty);
        transaction.setItem(item);

        itemRepository.save(item);

        // trans.setItem(item);
        trans.setItemQuantity(item_quantity);
        trans.setDueBackDate(due_back_date);
        trans.setDueDate(due_date);
        trans.setDayDifference(loan_days);
        trans.setCollateral(collateral_price);
        trans.setTransPrice(trans_price);

        // System.out.println("New collateral price" + collateral_price);

        Customer cust = trans.getCustomer();
        double old_total_collateral = cust.getTotalCollateral();
        double new_total_collateral = old_total_collateral - old_collateral_price + collateral_price;
        double old_total_price = cust.getTotalPrice();
        double new_total_price = old_total_price - old_trans_price + trans_price;
        double old_debt_balance = cust.getDebtBalance();
        double new_debt_balance = old_debt_balance - old_trans_price + trans_price;
        
        cust.setTotalPrice(new_total_price);
        cust.setTotalCollateral(new_total_collateral);
        cust.setDebtBalance(new_debt_balance);

        // System.out.println("Old Total collateral" + old_total_collateral);
        // System.out.println("New total collateral" + new_total_collateral);


        
        transactionRepository.save(trans);
        // this.customerRepository.save(cust);

        model.addAttribute("customer", cust);
        model.addAttribute("transactions", cust.getTransactions());
        model.addAttribute("company", companyRepository.findAll());
        Date today = new Date();
        model.addAttribute("today", today);
        return "Agreements/view-updated-agreement";
        // return "redirect:/customers";
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
    @Valid Transaction transaction, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "Forms/customer-transaction";
        }

        // System.out.println("Incoming Transaction\n" + transaction.toString()); //debug line

        Customer customer =
        customerRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        
        // Calcualting Logic
        // calculating date difference
        Date fromDate = transaction.getDueBackDate();
        Date toDate = transaction.getDueDate();        
        long loan_days = calculateDayDifference(fromDate, toDate);
        transaction.setDayDifference(loan_days);

        // calculating loan price per transaction
        double loan_price_per_day = transaction.getItem().getUnitLoanPrice();
        double loan_price_per_item = loan_price_per_day * loan_days;
        double trans_price = transaction.getItem().getAreaCoverage()*loan_price_per_item;
        transaction.setTransPrice(trans_price);
        
        // calculating collateral price per transaction 100%
        double collateral_price = transaction.getItem().getUnitPrice()*transaction.getItemQuantity();
        transaction.setCollateral(collateral_price);

        transaction.setCustomer(customer);

        double total_price = customer.getTotalPrice();
        double total_collateral = customer.getTotalCollateral();
        double total_price_VAT = customer.getTotalPriceVAT();   
        double total_collateral_VAT = customer.getTotalCollateralVAT();
        total_price += trans_price;
        total_collateral += collateral_price;
        total_price_VAT = total_price + (total_price * 0.15);
        total_collateral_VAT = total_collateral + (total_collateral * 0.15);
        customer.setTotalPrice(total_price);
        customer.setTotalCollateral(total_collateral);
        customer.setTotalPriceVAT(total_price_VAT);
        customer.setTotalCollateralVAT(total_collateral_VAT);
        customer.setDebtBalance(total_price_VAT);
        
        // calculate remaining item total quantity
        Item item = transaction.getItem();
        int oldQty = item.getTotalQuantity();
        int newQty = oldQty - transaction.getItemQuantity();
        item.setTotalQuantity(newQty);
        transaction.setItem(item);

        itemRepository.save(item);

        transactionRepository.save(transaction);

        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getTransactions());
        model.addAttribute("company", companyRepository.findAll());
        Date today = new Date();
        model.addAttribute("today", today);
        return "Agreements/view-updated-agreement";
    }

    @GetMapping("transactions/remove/{transId}")
    public String deleteTransaction(@PathVariable("transId") long transId, Model model){
        Transaction transaction =
            transactionRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id:" + transId));
        
        int oldQty = transaction.getItem().getTotalQuantity();
        transaction.getItem().setTotalQuantity(oldQty +  transaction.getItemQuantity());

        double oldDebt = transaction.getCustomer().getDebtBalance() / 1.15;
        double newDebt = (oldDebt - transaction.getTransPrice()) * 1.15;
        transaction.getCustomer().setDebtBalance(newDebt);

        double old_total_collateral = transaction.getCustomer().getTotalCollateral();
        double new_total_collateral = old_total_collateral - transaction.getCollateral();
        transaction.getCustomer().setTotalCollateral(new_total_collateral);

        transaction.getCustomer().setTotalCollateralVAT(new_total_collateral * 1.15);

        transactionRepository.delete(transaction);
        return "redirect:/customers";
    }

    public long calculateDayDifference(Date fromDate, Date toDate) {
        long difference_In_Time = fromDate.getTime() - toDate.getTime();
        long difference_In_Days = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time);
        return difference_In_Days;
    }  
}
