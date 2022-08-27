package com.mania.zerosheet.Agreement;

// import java.util.List;
// import java.util.ArrayList;
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

    // private List<String> statements = new ArrayList<String>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "agreement_id", nullable = true)
    private Agreement agmnt;

    // public Article(List<String> statements) {
    //     this.statements = statements;
    // }
    
    public Article(String statement) {
        this.statement = statement;
    }
}
