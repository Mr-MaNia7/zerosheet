package com.mania.zerosheet.Performa;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
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
    private boolean isDuplicate = false;

    @GetMapping("/transactions/performas")
    public String showPerformas(Performa transaction, Model model) {
        model.addAttribute("performas", performaRepository.findAll());
        return "Performas/view-performas";
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
        model.addAttribute("isDuplicate", isDuplicate);
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

        isDuplicate = false;
        if (
            new_performa.getItem().getItemId() == old_performa.getItem().getItemId() &&
            new_performa.getItemPrice() == old_performa.getItemPrice() &&
            new_performa.getItemQuantity() == old_performa.getItemQuantity() &&
            new_performa.getDueDate().equals(old_performa.getDueDate()) &&
            new_performa.getDueBackDate().equals(old_performa.getDueBackDate())
            )
        {
            isDuplicate = true;
            return "redirect:/performas/editperforma/{transId}";
        }
        
        old_performa.editPerforma(new_performa, new_item);
        
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
