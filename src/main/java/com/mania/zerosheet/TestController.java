package com.mania.zerosheet;

import javax.validation.Valid;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController {
    private final ItemRepository itemRepository;

    @Autowired
    public TestController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/test")
    public String showItemsPage(Item item, Model model) {
        model.addAttribute("items", itemRepository.findAll());
        return "form/test";
    }

    // from test page
    @GetMapping(value = "/cancelitem/{itemId}")
    public String showCancelItemForm(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + itemId));
        itemRepository.delete(item);
        model.addAttribute("items", itemRepository.findAll());
        return "index";
    }

    // from test page
    @GetMapping("/previousitem/{itemId}")
    public String showPreviousItemForm(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id: " + itemId));
        model.addAttribute("item", item);
        return "form/add-item-form";
    }
    
    @PostMapping("add-item-form")
    public String addItemForm(@Valid Item item, 
        BindingResult result, Model model){
        if (result.hasErrors()){
            return "add-item-form";
        }
        itemRepository.save(item);
        model.addAttribute("items", itemRepository.findAll());
        return "add-transaction";
    }
    // @GetMapping("next-item")
    // public String showTansactionForm() {
        
    // }
}