package com.mania.zerosheet.ItemInstances;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;
import com.mania.zerosheet.Items.ItemRepository;

@RequiredArgsConstructor
@Controller
public class InstanceController {
    private final InstanceRepository instanceRepository;
    private final ItemRepository itemRepository;

    // from home to instances
    @GetMapping("/instances")
    public String showItemsPage(ItemInstance instance, Model model){
        model.addAttribute("instances", instanceRepository.findAll());
        return "Instances/view-instances";
    }
    
    

    // from instances to instances
    @GetMapping("instances/editinstance/{instanceId}")
    public String showUpdateItemForm(@PathVariable("instanceId") long instanceId, Model model) {
        ItemInstance instance = 
            instanceRepository
                .findById(instanceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid instance Id: " + instanceId));
        model.addAttribute("instance", instance);
        model.addAttribute("items", itemRepository.findAll());
        return "Instances/update-instance";
    }
    // from update-instance (post and) redirect to instances
    @PostMapping(value="/updateinstance/{instanceId}")
    public String updateItem(@PathVariable("instanceId") long instanceId, @Valid ItemInstance instance,
    BindingResult result, Model model) {
        if (result.hasErrors()){
            instance.setInstanceId(instanceId);
            return "Instances/update-instance";
        }
        instanceRepository.save(instance);
        model.addAttribute("instances", instanceRepository.findAll());
        return "redirect:/view-instances";
    }

    // from instances (delete and) redirect to instances
    @GetMapping(value="instances/deleteitem/{instanceId}")
    public String deleteItem(@PathVariable("instanceId") long instanceId, Model model) {
        ItemInstance instance = 
            instanceRepository
                .findById(instanceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid instance Id: " + instanceId));
    instanceRepository.delete(instance);
    model.addAttribute("instances", instanceRepository.findAll());
    return "redirect:/view-instances";
    }   
}
