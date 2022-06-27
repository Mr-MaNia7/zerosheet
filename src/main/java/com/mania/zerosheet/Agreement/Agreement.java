package com.mania.zerosheet.Agreement;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 
    private String agreementNumber;

    private String statement;

    public Agreement(String agreementNumber, String statement){
        this.statement = statement;
        this.agreementNumber = agreementNumber;
    }

}
