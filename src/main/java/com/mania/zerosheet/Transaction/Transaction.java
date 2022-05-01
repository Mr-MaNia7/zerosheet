package com.mania.zerosheet.Transaction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Items.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long transId;
    
    @NotBlank(message = "Transaction Name can not be blank!")
    private String transName;
    
    // Owning side - Many side
    @ManyToOne
    @JoinColumn(name = "itemId")
    Item item;
    
    @ManyToOne
    @JoinColumn(name = "custId")
    Customer customer;

    // For Command Line Runner
    // public Transaction(String transName){
    //     this.transName = transName;
    //     // this.item = item;
    //     // this.customer = customer;
    // }
}
