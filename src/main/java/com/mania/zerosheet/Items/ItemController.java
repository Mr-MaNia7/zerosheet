package com.mania.zerosheet.Items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    }
}
