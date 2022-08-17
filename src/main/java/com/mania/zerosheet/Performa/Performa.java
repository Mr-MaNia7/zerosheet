package com.mania.zerosheet.Performa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
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
public class Performa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transId;
    
    @Min(value = 1, message = "Item Quantity should be atleast 1")
    // @Digits(10)
    private int itemQuantity = 10;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueBackDate = new Date(dueDate.getTime() + (1000 * 60 * 60 * 24 * 15) + (1000 * 60 * 60 * 24 * 15));

    private double itemPrice;

    private double transPrice;

    private long dayDifference;

    private double collateral;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "cust_id", nullable = true)
    Customer cust;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "itemp_id", nullable = true)
    Item item;

    public Performa(int itemQuantity, Date dueDate, Date dueBackDate, 
    long dayDifference, double collateral, double transPrice, double itemPrice, Customer customer, Item item) {
        this.itemQuantity = itemQuantity;
        this.dueDate = dueDate;
        this.dueBackDate = dueBackDate;
        this.dayDifference = dayDifference;
        this.collateral = collateral;
        this.transPrice = transPrice;
        this.itemPrice = itemPrice;
        this.cust = customer;
        this.item = item;
    }
}
