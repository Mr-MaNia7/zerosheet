package com.mania.zerosheet.Saved;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.mania.zerosheet.Customers.Customer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class SavedAgreement implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long agtId;
  @NotBlank(message = "Customer Full Name is required")
  private String fullName;
  @Pattern(regexp = "^[0][9][0-9]{8}$|^[9][0-9]{8}$|[0][7][0-9]{8}$|^[7][0-9]{8}$", message = "Should be 9/10 characters long, beginning with 9/09 or 7/07")
  @NotBlank(message = "Customer Phone Number is required")
  private String phoneNumber;
  private String houseNumber;
  @NotBlank(message = "Customer City or Town is required")
  private String city;
  private int woreda;
  private String subcity;
  private Date agreementDate;

  private double totalPriceP;
  private double totalPriceVATP;
  private double remainingPriceP;
  private double totalCollateralP;
  private double totalCollateralVATP;

  @ManyToOne(fetch = FetchType.EAGER, optional = true)
  @JoinColumn(name = "customer_id", nullable = true)
  private Customer customer;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "savedAgreement")
  private List<SavedTrans> savedTransactions = new ArrayList<SavedTrans>();

  public void addSavedTrans(SavedTrans savedTrans){
    this.savedTransactions.add(savedTrans);
  }

  public SavedAgreement(String fullName, String phoneNumber, String houseNumber, String city,
      double totalPrice, double remainingPrice, double totalCollateral,
      double totalPriceVAT, double totalCollateralVAT, int woreda, String subcity,
      Date agreementDate, Customer customer) {
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.houseNumber = houseNumber;
    this.city = city;
    this.subcity = subcity;
    this.woreda = woreda;
    this.totalPriceP = totalPrice;
    this.remainingPriceP = remainingPrice;
    this.totalCollateralP = totalCollateral;
    this.totalPriceVATP = totalPriceVAT;
    this.totalCollateralVATP = totalCollateralVAT;
    this.agreementDate = agreementDate;
    this.customer = customer;
  }
}