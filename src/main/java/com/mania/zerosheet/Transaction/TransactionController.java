package com.mania.zerosheet.Transaction;

import java.util.Date;
import java.util.concurrent.TimeUnit;
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
        // calculating date difference
        long loan_days = calculateDayDifference(performa.getDueBackDate(), performa.getDueDate());
        // calculating loan price per performa
        double transPrice = calculateTransactionPrice(performa.getItemPrice(), performa.getItemQuantity(), loan_days);
        // calculating collateral price per performa 100%
        double collateral_price = performa.getItem().getUnitPrice()*performa.getItemQuantity(); 

        // setting calculated values
        performa.setDayDifference(loan_days);
        performa.setCollateral(collateral_price);
        performa.setTransPrice(transPrice);

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
        
        // Get old object and save it inside the new object

        int item_quantity = new_trans.getItemQuantity();
        Date due_back_date = new_trans.getDueBackDate();
        Date due_date = new_trans.getDueDate();

        long loan_days = calculateDayDifference(due_back_date, due_date);
        double trans_price = calculateTransactionPrice(new_trans.getItemPrice(), new_trans.getItemQuantity(), loan_days);
        double collateral_price = new_item.getUnitPrice()*item_quantity;        
        
        double old_collateral_price = old_trans.getCollateral();
        double old_trans_price = old_trans.getTransPrice();
        int old_trans_quantity = old_trans.getItemQuantity();
        
        // calculate remaining item total quantity
        Item old_item = old_trans.getItem();
        if (old_item.getItemId() == new_item.getItemId()) {
            int newQty = calculateItemQuantity(new_item.getTotalQuantity(), new_trans.getItemQuantity(), old_trans_quantity);
            new_item.setTotalQuantity(newQty);
            old_trans.setItem(new_item);
        }
        else {
            int old_item_Qty = old_item.getTotalQuantity() + old_trans_quantity;
            old_item.setTotalQuantity(old_item_Qty);
            itemRepository.save(old_item);

            int newQty = calculateItemQuantity(new_item.getTotalQuantity(), new_trans.getItemQuantity(), 0);
            new_item.setTotalQuantity(newQty);
            old_trans.setItem(new_item);
        }

        old_trans.setItemQuantity(item_quantity);
        old_trans.setDueBackDate(due_back_date);
        old_trans.setDueDate(due_date);
        old_trans.setDayDifference(loan_days);
        old_trans.setCollateral(collateral_price);
        old_trans.setTransPrice(trans_price);

        Customer cust = old_trans.getCustomer();
        double old_total_collateral = cust.getTotalCollateral();
        double new_total_collateral = old_total_collateral - old_collateral_price + collateral_price;
        double old_total_price = cust.getTotalPrice();
        double new_total_price = old_total_price - old_trans_price + trans_price;
        double old_debt_balance = cust.getDebtBalance();
        double new_debt_balance = (old_debt_balance/1.15) - old_trans_price + trans_price;
        
        cust.setTotalPrice(new_total_price);
        cust.setTotalPriceVAT(new_total_price * 1.15);
        cust.setTotalCollateral(new_total_collateral);
        cust.setTotalCollateralVAT(new_total_collateral * 1.15);
        cust.setDebtBalance(new_debt_balance * 1.15);
        
        transactionRepository.save(old_trans);
        // this.customerRepository.save(cust);

        model.addAttribute("customer", cust);
        model.addAttribute("transactions", cust.getTransactions());
        model.addAttribute("company", companyRepository.findAll());
        Date today = new Date();
        model.addAttribute("today", today);
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

        // System.out.println("Incoming Transaction\n" + performa.toString()); //debug line

        Customer customer =
        customerRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id: " + id));
        
        // Calcualting Logic
        // calculating date difference
        Date fromDate = performa.getDueBackDate();
        Date toDate = performa.getDueDate();        
        long loan_days = calculateDayDifference(fromDate, toDate);
        performa.setDayDifference(loan_days);

        // calculating loan price per performa
        double loan_price_per_day = performa.getItemPrice();
        double loan_price_per_item = loan_price_per_day * loan_days;
        double trans_price = performa.getItemQuantity()*loan_price_per_item;
        performa.setTransPrice(trans_price);
        
        // calculating collateral price per performa 100%
        double collateral_price = performa.getItem().getUnitPrice()*performa.getItemQuantity();
        performa.setCollateral(collateral_price);

        performa.setCust(customer);

        double total_price = customer.getTotalPriceP();
        double total_collateral = customer.getTotalCollateralP();
        double total_price_VAT = customer.getTotalPriceVATP();   
        double total_collateral_VAT = customer.getTotalCollateralVATP();

        total_price += trans_price;
        total_collateral += collateral_price;
        total_price_VAT = total_price + (total_price * 0.15);
        total_collateral_VAT = total_collateral + (total_collateral * 0.15);
        
        customer.setTotalPriceP(total_price);
        customer.setTotalCollateralP(total_collateral);
        customer.setTotalPriceVATP(total_price_VAT);
        customer.setTotalCollateralVATP(total_collateral_VAT);
        customer.setDebtBalanceP(total_price_VAT);
        
        performaRepository.save(performa);

        model.addAttribute("customer", customer);
        model.addAttribute("transactions", customer.getPerformas());
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

    @GetMapping("transactions/return/{transId}")
    public String returnItems(@PathVariable("transId") long transId, @RequestParam(value="returnQuantity") int returnQuantity, Model model){
        Transaction transaction =
            transactionRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction Id:" + transId));
        
        
        int new_item_quantity = transaction.getItemQuantity() - returnQuantity;
        double old_trans_price = transaction.getTransPrice();
        double new_trans_price = (old_trans_price  / transaction.getItemQuantity())*new_item_quantity;
        int total_quantity = transaction.getItem().getTotalQuantity();

        double old_collateral_price = transaction.getCollateral();
        double collateral_price = transaction.getItem().getUnitPrice()*new_item_quantity;
        transaction.setCollateral(collateral_price);
        
        double oldDebt = transaction.getCustomer().getDebtBalance() / 1.15;
        double newDebt = oldDebt - old_trans_price + new_trans_price;
        transaction.getCustomer().setDebtBalance(newDebt * 1.15);

        double old_total_collateral = transaction.getCustomer().getTotalCollateral();
        double new_total_collateral = old_total_collateral - old_collateral_price + collateral_price;
        transaction.getCustomer().setTotalCollateral(new_total_collateral);

        transaction.getCustomer().setTotalCollateralVAT(new_total_collateral * 1.15);

        transaction.setItemQuantity(new_item_quantity);
        transaction.setTransPrice(new_trans_price);
        transaction.getItem().setTotalQuantity(total_quantity + returnQuantity);
        transactionRepository.save(transaction);
        return "redirect:/customers";
    }

    public long calculateDayDifference(Date fromDate, Date toDate) {
        long difference_In_Time = fromDate.getTime() - toDate.getTime();
        long difference_In_Days = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time);
    
        // long difference_In_Years = 
        // TimeUnit
        //       .MILLISECONDS
        //       .toDays(difference_In_Time)
        //   / 365l;
        return difference_In_Days;
    }
    public double calculateTransactionPrice(double loan_price_per_day, int quantity, long loan_days){
        return loan_price_per_day * loan_days * quantity;
    }
    public int calculateItemQuantity(int old_item_quantity, int trans_item_quantity, int old_trans_quantity){
        return old_item_quantity - trans_item_quantity + old_trans_quantity;
    }
}