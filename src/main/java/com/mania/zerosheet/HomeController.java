package com.mania.zerosheet;

import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Items.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final ItemRepository itemRepository;
    @Autowired
    public HomeController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    @GetMapping("/")
    public String showHomePage(Item item, Model model){
        model.addAttribute("items", itemRepository.findAll());
        return "index";
    }
}
