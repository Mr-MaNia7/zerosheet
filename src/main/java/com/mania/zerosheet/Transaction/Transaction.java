    package com.mania.zerosheet.Transaction;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Positive;
import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.Items.Item;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transId;
    
    @Positive(message = "Item Quantity should be positive")
    private int itemQuantity;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueDate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueBackDate;

    private double transPrice;

    private long dayDifference;

    private double collateral;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id", nullable = true)
    Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "item_id", nullable = true)
    Item item;

    public Transaction(int itemQuantity, Date dueDate, Date dueBackDate, 
    long dayDifference, double collateral, double transPrice, Customer customer, Item item) {
        this.itemQuantity = itemQuantity;
        this.dueDate = dueDate;
        this.dueBackDate = dueBackDate;
        this.dayDifference = dayDifference;
        this.collateral = collateral;
        this.transPrice = transPrice;
        this.customer = customer;
        this.item = item;
    }
}
