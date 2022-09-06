package com.mania.zerosheet.Items;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.mania.zerosheet.ItemInstance.Instance;
import com.mania.zerosheet.ItemInstance.InstanceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ItemRepository itemRepository;
    private final InstanceRepository instanceRepository;
    
    @GetMapping("/items")
    public String showItemsPage(Item item, Model model){
        model.addAttribute("items", itemRepository.findAll());
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
        instanceRepository.save(item.defaultInstance());
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
        Item old_item =
            itemRepository
                .findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + itemId));
        Instance available_instance = old_item.findAvailableInstance();
        available_instance.setItemQuantity(item.getTotalQuantity());
        item.addInstance(available_instance);        
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
    
    itemRepository.delete(item);
    model.addAttribute("items", itemRepository.findAll());
    return "redirect:/items";
    }   
}
