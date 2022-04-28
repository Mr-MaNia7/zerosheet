package com.mania.zerosheet.Transaction;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
    private long transId;

    // Owning side - Many side
    @ManyToOne
    @JoinColumn(name = "itemId")
    Item item;
    
    @ManyToOne
    @JoinColumn(name = "custId")
    Customer customer;
    
    private String transName;

}
