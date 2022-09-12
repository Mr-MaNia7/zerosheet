package com.mania.zerosheet.Saved;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.mania.zerosheet.Company.CompanyRepository;
import com.mania.zerosheet.Customers.CustomerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SavedController {
    private final SavedAgreementRepository savedAgreementRepo;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;

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
    @GetMapping("/agreements/saved")
    public String showSavedAgreements(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "Saved/view-saved-agreements";
    }
}
