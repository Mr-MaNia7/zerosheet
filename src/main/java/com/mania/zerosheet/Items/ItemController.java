package com.mania.zerosheet.Items;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.mania.zerosheet.ItemInstance.Instance;
import com.mania.zerosheet.ItemInstance.InstanceRepository;
import com.mania.zerosheet.Performa.Performa;
import com.mania.zerosheet.Performa.PerformaRepository;
import com.mania.zerosheet.Transaction.Transaction;
import com.mania.zerosheet.Transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final InstanceRepository instanceRepository;
    private final TransactionRepository transactionRepository;
    private final PerformaRepository performaRepository;
        
    @GetMapping("/items")
    public String showItemsPage(Item item, @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
    @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model){
        model.addAttribute("items", itemService.getPage(pageNumber, size));
        return "Items/view-items";
    }
    
    @GetMapping("items/newitem")
    public String showItemAddForm(Item item){
        return "Items/add-item";
    }
    @PostMapping(value="/additem")
    public String addItem(@Valid Item item, BindingResult result, Model model){
        if (result.hasErrors()){
            return "Items/add-item";
        }
        itemRepository.save(item);
        return "redirect:/items";
    }
    
    @GetMapping("items/edititem/{itemId}")
    public String showUpdateItemForm(@PathVariable("itemId") long itemId, Model model) {
        Item item =
            itemRepository
                .findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + itemId));
        model.addAttribute("item", item);
        return "Items/update-item";
    }
    @PostMapping(value="/updateitem/{itemId}")
    public String updateItem(@PathVariable("itemId") long itemId, @Valid Item item,
    BindingResult result, Model model) {
        if (result.hasErrors()){
            item.setItemId(itemId);
            return "Items/update-item";
        }
        itemRepository.save(item);
        model.addAttribute("items", itemRepository.findAll());
        return "redirect:/items";
    }

    @GetMapping(value="items/deleteitem/{itemId}")
    public String deleteItem(@PathVariable("itemId") long itemId, Model model) {
        Item item = 
            itemRepository
                .findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + itemId));

        for (Transaction transaction : item.getTransactions()) {            
        Instance instance = transaction.getInstance();
        transaction.setInstance(null);
        transaction.getCustomer().getInstances().remove(instance);
        instance.setCustomer(null);
        item.removeInstance(instance);
        instance.setItem(null);
        this.instanceRepository.delete(instance);
        
        transaction.getCustomer().clearMonetary();
        transaction.setItem(null);
        transaction.getCustomer().getTransactions().remove(transaction);
        transaction.setCustomer(null);
        this.transactionRepository.delete(transaction);
        }
        for (Performa performa : item.getPerformas()){
            performa.setItem(null);
            performa.getCust().getPerformas().remove(performa);
            performa.setCust(null);
            this.performaRepository.delete(performa);
        }
        item.getInstances().clear();
        item.getTransactions().clear();
        item.getPerformas().clear();
        this.itemRepository.delete(item);
        model.addAttribute("items", itemRepository.findAll());
        return "redirect:/items";
    }   
}
