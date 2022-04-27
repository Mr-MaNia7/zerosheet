package com.mania.zerosheet.Items;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ItemController {
    private final ItemRepository itemRepository;
    @Autowired
    public ItemController(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @GetMapping("/items")
    public String showItemsPage(Item item, Model model){
        model.addAttribute("items", itemRepository.findAll());
        return "items";
    }//
    @GetMapping("/newitem")
    public String showItemAddForm(Item item){
        return "add-item";
    }//
    // from items page
    @GetMapping(value="/edititem/{itemId}")
    public String showUpdateItemForm(@PathVariable("itemId") long itemId, @Valid Item item, 
    BindingResult result, Model model) {
        if (result.hasErrors()){
            item.setItemId(itemId);
            return "update-item";
        }
        itemRepository.save(item);
        model.addAttribute("items", itemRepository.findAll());
        return "items";
    }
    // from items page
    @GetMapping(value="/deleteitem/{itemId}")
    public String deleteItem(@PathVariable("itemId") long itemId, Model model) {
        Item item = 
            itemRepository
                .findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + itemId));
    itemRepository.delete(item);
    model.addAttribute("items", itemRepository.findAll());
    return "items";
    }
    
    // from add-item page
    @PostMapping("/additem")
    public String addItem(@Valid Item item, BindingResult result, Model model){
        if (result.hasErrors()){
            return "add-item";
        }
        itemRepository.save(item);
        model.addAttribute("items", itemRepository.findAll());
        return "items";
    }
    // from update-item
    @PostMapping(value="/updateitem/{itemId}")
    public String updateItem(@PathVariable("itemId") long itemId, @Valid Item item,
    BindingResult result, Model model) {
        if (result.hasErrors()){
            item.setItemId(itemId);
            return "update-item";
        }
        itemRepository.save(item);
        model.addAttribute("items", itemRepository.findAll());
        return "items";
    }
    
}
