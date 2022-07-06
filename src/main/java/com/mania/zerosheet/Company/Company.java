package com.mania.zerosheet.Company;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @NotBlank(message = "Company Name can not be blank")
    private String compName;
    @Pattern(regexp="^[0][9][0-9]{8}$|^[9][0-9]{8}$", message="Should be 9/10 characters long, beginning with 9/09")  
    @NotBlank(message = "Company Phone Number is required")
    private String compPhoneNumber;
    @NotBlank(message = "Company Woreda number is required")
    private String compWoreda;
    @NotBlank(message = "Company Sub-city is required")
    private String compSubCity;
    @NotBlank(message = "Company House Number is required")
    private String compHouseNumber;
    @NotBlank(message = "Customer City or Town is required")
    private String compCity;
    @NotBlank(message = "Customer Country is required")
    private String compCountry;
}
