package com.mania.zerosheet.Agreement;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long articleId;
    
    private String statement;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "agreement_id", nullable = true)
    private Agreement agmnt;

    public Article(String statement) {
        this.statement = statement;
    }
}
