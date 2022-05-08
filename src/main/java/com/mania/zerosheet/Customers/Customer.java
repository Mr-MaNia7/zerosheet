package com.mania.zerosheet.Customers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

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

  private double totalPrice;
  private double debtBalance;
  private double totalCollateral;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
  private List<Transaction> transactions = new ArrayList<Transaction>();
  
  public void addTransaction(Transaction transaction) {
    this.transactions.add(transaction);
  }

  public Customer(String name, String middleName, String lastName, String email,
    String phoneNumber, String houseNumber, String city, 
    double totalPrice, double debtBalance, double totalCollateral) {
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
    }
}