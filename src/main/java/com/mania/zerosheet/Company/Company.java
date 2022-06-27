package com.mania.zerosheet.Company;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    private String compName;
    private String compPhoneNumber;
    private int compWoreda;
    private String compSubCity;
    private String compHouseNumber;
    private String compCity;
    private String compCountry;
}
