package com.mania.zerosheet.Performa;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes("customer")
public class PerformaController {
    private final ItemRepository itemRepository;
    private final PerformaRepository performaRepository;

    @GetMapping("/transactions/performas")
    public String showPerformas(Performa transaction, Model model) {
        model.addAttribute("performas", performaRepository.findAll());
        return "Transactions/view-transactions";
    }

    @GetMapping("transactions/performas/remove/{transId}")
    public String deletePerforma(@PathVariable("transId") long transId, Model model){
        Performa performa =
            performaRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid performa Id:" + transId));
        
        performaRepository.delete(performa);
        return "redirect:/customers";
    }

    @GetMapping("performas/editperforma/{transId}")
    public String editPerforma(@PathVariable("transId") long transId, Model model) {
        Performa performa = 
            performaRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid performa Id: " + transId));
        
        model.addAttribute("transaction", performa);
        model.addAttribute("items", itemRepository.findAll());
        return "Performas/update-performa";
    }
    @PostMapping("performas/updateperforma/{transId}")
    public String updateTransaction(@PathVariable("transId") long transId,
    @Valid Performa new_performa, BindingResult result, Model model){
        if (result.hasErrors()) {
            new_performa.setTransId(transId);
            return "Performas/update-performa";
        }

        Item new_item =
            itemRepository
                .findById(new_performa.getItem().getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + new_performa.getItem().getItemId()));

        Performa old_performa =
            performaRepository
                .findById(transId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid performa Id: " + transId));
        
        // Get old object and save it inside the new object
        int item_quantity = new_performa.getItemQuantity();
        Date due_back_date = new_performa.getDueBackDate();
        Date due_date = new_performa.getDueDate();

        long loan_days = calculateDayDifference(due_back_date, due_date);
        double trans_price = calculateTransactionPrice(new_performa.getItemPrice(), new_performa.getItemQuantity(), loan_days);
        double collateral_price = new_item.getUnitPrice()*item_quantity;        
        
        double old_collateral_price = old_performa.getCollateral();
        double old_performa_price = old_performa.getTransPrice();
        
        // calculate remaining item total quantity
        old_performa.setItem(new_item);
        old_performa.setItemQuantity(item_quantity);
        old_performa.setDueBackDate(due_back_date);
        old_performa.setDueDate(due_date);
        old_performa.setDayDifference(loan_days);
        old_performa.setCollateral(collateral_price);
        old_performa.setTransPrice(trans_price);

        Customer cust = old_performa.getCust();
        double old_total_collateral = cust.getTotalCollateral();
        double new_total_collateral = old_total_collateral - old_collateral_price + collateral_price;
        double old_total_price = cust.getTotalPrice();
        double new_total_price = old_total_price - old_performa_price + trans_price;
        double old_debt_balance = cust.getDebtBalance();
        double new_debt_balance = (old_debt_balance/1.15) - old_performa_price + trans_price;
        
        cust.setTotalPrice(new_total_price);
        cust.setTotalPriceVAT(new_total_price * 1.15);
        cust.setTotalCollateral(new_total_collateral);
        cust.setTotalCollateralVAT(new_total_collateral * 1.15);
        cust.setDebtBalance(new_debt_balance * 1.15);
        
        performaRepository.save(old_performa);
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
    public double calculateTransactionPrice(double loan_price_per_day, int quantity, long loan_days){
        return loan_price_per_day * loan_days * quantity;
    }
    public int calculateItemQuantity(int old_item_quantity, int trans_item_quantity, int old_performa_quantity){
        return old_item_quantity - trans_item_quantity + old_performa_quantity;
    }
}
