package com.mania.zerosheet.Contact;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ContactController {
    private final ContactRepository contactRepository;

    @GetMapping("/contact")
    public String showContactPage(Contact contact){
        return "Contact/contact";
    }
    
    @PostMapping("/addfeedback")
    public String feedbackHandler(@Valid Contact contact, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Contact/contact";
        }
        contactRepository.save(contact);
        model.addAttribute("isCommented", true);
        return "redirect:/";
    }

    @GetMapping("/viewfeedbacks")
    public String showFeedbacks(Model model){
        model.addAttribute("feedbacks", contactRepository.findAll());
        return "Contact/view-contact";
    }
}
