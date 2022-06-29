package com.mania.zerosheet.Agreement;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mania.zerosheet.Company.CompanyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AgreementController {
    private final AgreementRepository agreementRepository;
    private final CompanyRepository companyRepository;
    private final ArticleRepository articleRepository;

    @GetMapping("/agreements")
    public String showAgreementsPage(Agreement agreement, Model model){
        model.addAttribute("agreements", agreementRepository.findAll());
        model.addAttribute("company", companyRepository.findAll());
        return "Agreements/view-agreements";
    }

    // @GetMapping("/agreement")
    // public String showAgreementPage(Agreement agreement, Model model){
    //     // model.addAttribute("agreements", agreementRepository.findAll());
    //     model.addAttribute("company", companyRepository.findAll());
    //     return "Agreements/view-agreement";
    // }

    @GetMapping("agreements/new")
    public String showAddAgreementForm(Agreement agreement){
        return "Agreements/add-article";
    }
    
    @ModelAttribute(name = "agreement")
    public Agreement agmnt() {
        return new Agreement();
    }
    @ModelAttribute(name = "article")
    public Article article() {
        return new Article();
    }

    @PostMapping("/addarticle")
    public String addArticle(@Valid Article article, 
    @ModelAttribute Agreement agmnt, BindingResult result, Model model) {
        if (result.hasErrors()){
            return "Agreements/add-article";
        }
        agmnt.addArticle(article);
        agreementRepository.save(agmnt);
        // agmnt.getTransactions().forEach(transaction -> transaction.setCustomer(customer));
        // agmnt.getArticles().forEach(ar -> ar.setAgmnt(agmnt));
        model.addAttribute("articles", agmnt.getArticles());
        return "redirect:/agreements";
    }
}
