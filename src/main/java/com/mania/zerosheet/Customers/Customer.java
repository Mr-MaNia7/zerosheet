package com.mania.zerosheet.Customers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToOne;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.mania.zerosheet.Agreement.Agreement;
import com.mania.zerosheet.Transaction.Transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Customer implements Serializable{
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank(message = "Customer First Name is required")
  private String name;
  @NotBlank(message = "Customer Middle Name is required")
  private String middleName;
  @NotBlank(message = "Customer Last Name is required")
  private String lastName;
  @NotBlank(message = "Customer Email Address is required")
  private String email;
  @NotBlank(message = "Customer Phone number is required")
  private String phoneNumber;
  private String houseNumber;
  @NotBlank(message = "Customer City or Town is required")
  private String city;
  private int woreda;
  private String subcity;

  private double totalPrice;
  private double totalPriceVAT;
  private double debtBalance;
  private double totalCollateral;
  private double totalCollateralVAT;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
  private List<Transaction> transactions = new ArrayList<Transaction>();
     
  @OneToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "agreement_id", nullable = true)
  Agreement agreement;
  
  public void addTransaction(Transaction transaction) {
    boolean flag = false;
    for (Transaction eachtrans : this.transactions){
      if(eachtrans.getItem().getItemId() == transaction.getItem().getItemId()) {
        this.transactions.set((int)(eachtrans.getTransId()), transaction);
        flag = true;
      }
    }
    if (flag == false) {
      this.transactions.add(transaction);
    }
  }
  public void updateTransaction(Transaction transaction, long id) {
    this.transactions.set((int)(id - 1), transaction);
  }

  public Customer(String name, String middleName, String lastName, String email,
    String phoneNumber, String houseNumber, String city, 
    double totalPrice, double debtBalance, double totalCollateral, double totalPriceVAT,
    double totalCollateralVAT,int woreda, String subcity) {
      this.name = name;
      this.middleName = middleName;
      this.lastName = lastName;
      this.email = email;
      this.phoneNumber = phoneNumber;
      this.houseNumber = houseNumber;
      this.city = city;
      this.totalPrice = totalPrice;
      this.debtBalance = debtBalance;
      this.totalCollateral = totalCollateral;
      this.subcity = subcity;
      this.woreda = woreda;
      this.totalPriceVAT = totalPriceVAT;
      this.totalCollateralVAT = totalCollateralVAT;
    }
}