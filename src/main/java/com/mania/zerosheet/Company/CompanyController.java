package com.mania.zerosheet.Company;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyRepository companyRepository;
    
    @GetMapping("/company")
    public String showCompanyInfo(Company company, Model model) {
        model.addAttribute("company", companyRepository.findAll());
        return "Company/view-company";
    }

    @GetMapping("/editcompany/{compName}")
    public String showUpdateCompany(@PathVariable("compName") String compName, Model model) {
        Company company = 
            companyRepository
            .findById(compName)
            .orElseThrow(() -> new IllegalArgumentException("Invalid company Id: " + compName));
        model.addAttribute("company", company);
        return "Company/update-company"; 
    }
    @PostMapping("/updatecompany/{compName}")
    public String updateCompany(@PathVariable("compName") String compName, @Valid Company company, 
                BindingResult result, Model model) {
                    if (result.hasErrors()){
                        company.setCompName(compName);
                        return "Company/update-company";
                    }
                    companyRepository.save(company);
                    return "redirect:/company";
                }

}
