package com.mania.zerosheet.Customers;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.mania.zerosheet.Transaction.Transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Customer {
  // fields
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @NotBlank(message = "Customer Name is required")
  private String name;
  @NotBlank(message = "Customer Surname is required")
  private String surname;
  @NotBlank(message = "Customer Email Address is required")
  private String email;
  
  @NotBlank(message = "Customer Phone number is required")
  private String phoneNumber;
  private String houseNumber;
  @NotBlank(message = "Customer City or Town is required")
  private String city;
  
  // Inverse side - One side
  @OneToMany(mappedBy = "customer")
  Set<Transaction> transactions;

  // constructors
  public Customer() {}
  public Customer(String name, String email) {
    this.name = name;
    this.email = email;
  }

// Setters and Getters
//   public void setId(long id) {
//     this.id = id;
//   }
//   public long getId() {
//     return id;
//   }
//   public void setName(String name) {
//     this.name = name;
//   }
//   public String getName() {
//     return name;
//   }
//   public void setEmail(String email) {
//     this.email = email;
//   }
//   public String getEmail() {
//     return email;
//   }
//   public String getSurname() {
//     return surname;
//   }
//   public void setSurname(String surname) {
//     this.surname = surname;
//   }
}