package com.mania.zerosheet.Customers;

import java.util.ArrayList;
import java.io.Serializable;
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
  // fields
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
  private List<Transaction> transactions = new ArrayList<Transaction>();
  
  public void addTransaction(Transaction transaction) {
    this.transactions.add(transaction);
  }
}