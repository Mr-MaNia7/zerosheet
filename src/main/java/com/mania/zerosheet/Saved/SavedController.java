package com.mania.zerosheet.Saved;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mania.zerosheet.Company.CompanyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SavedController {
    private final SavedAgreementRepository savedAgreementRepo;
    private final CompanyRepository companyRepository;

    @GetMapping("/agreements/{agtId}")
    public String showSavedAgreement(@PathVariable("agtId") long agtId, Model model){
        SavedAgreement savedAgreement = 
            savedAgreementRepo
                .findById(agtId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid SavedAgreement Id: " + agtId));
        model.addAttribute("customer", savedAgreement);
        model.addAttribute("transactions", savedAgreement.getSavedTransactions());
        model.addAttribute("company", companyRepository.findAll());
        return "Agreements/view-saved-agreement";
    }
}