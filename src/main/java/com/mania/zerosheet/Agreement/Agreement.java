package com.mania.zerosheet.Agreement;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import com.mania.zerosheet.Customers.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long agreementId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agmnt")
    private List<Article> articles = new ArrayList<Article>();
 
    @OneToOne(mappedBy = "agreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Customer customer;

    public void addArticle(Article article){
        this.articles.add(article);
    }
}
