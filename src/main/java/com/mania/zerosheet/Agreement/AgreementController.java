package com.mania.zerosheet.Agreement;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.mania.zerosheet.Company.CompanyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AgreementController {
    private final AgreementRepository agreementRepository;
    private final CompanyRepository companyRepository;

    @GetMapping("/agreements")
    public String showAgreementsPage(Agreement agreement, Model model){
        model.addAttribute("agreements", agreementRepository.findAll());
        model.addAttribute("company", companyRepository.findAll());
        return "Agreements/view-agreements";
    }

    @GetMapping("/agreement")
    public String showAgreementPage(Agreement agreement, Model model){
        // model.addAttribute("agreements", agreementRepository.findAll());
        model.addAttribute("company", companyRepository.findAll());
        return "Agreements/view-agreement";
    }

    @GetMapping("agreements/new")
    public String showAddAgreementForm(Agreement agreement){
        return "Agreements/add-agreement";
    }
    
    @PostMapping("/addagreement")
    public String addItem(@Valid Agreement agreement, BindingResult result, Model model){
        if (result.hasErrors()){
            return "Agreements/add-agreement";
        }
        agreementRepository.save(agreement);
        model.addAttribute("agreements", agreementRepository.findAll());
        return "redirect:/agreements";
    }
}
