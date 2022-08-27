package com.mania.zerosheet.Performa;

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
    @JoinColumn(name = "item_id", nullable = true)
    Item item;

    public void addNewTransaction(){
        this.setDayDifference();
        this.setTransPrice();
        this.setCollateral();
    }
    public void addTrans2ExistinCust(Customer customer){
        this.cust = customer;
        this.addNewTransaction();
        customer.updateCost(this.transPrice, this.collateral);
    }
    public void setDayDifference() {
        long difference_In_Time = this.dueBackDate.getTime() - this.dueDate.getTime();
        this.dayDifference = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time);
    }
    public void setTransPrice(){
        this.transPrice = this.itemPrice * this.dayDifference * this.itemQuantity;
    }
    public void setCollateral(){
        this.collateral = this.item.getUnitPrice() * this.itemQuantity;
    }
    public long calculateDayDifference() {
        long difference_In_Time = this.dueBackDate.getTime() - this.dueDate.getTime();
        long difference_In_Days = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time);
    
        // long difference_In_Years = 
        // TimeUnit
        //       .MILLISECONDS
        //       .toDays(difference_In_Time)
        //   / 365l;
        return difference_In_Days;
    }
    public void editPerforma(Performa new_performa, Item new_item) {
        this.itemQuantity = new_performa.getItemQuantity();
        this.item = new_item;
        this.dueDate = new_performa.getDueDate();
        this.dueBackDate = new_performa.getDueBackDate();
        this.setDayDifference();
        this.itemPrice = new_performa.getItemPrice();
        double old_trans_price = this.transPrice;
        this.setTransPrice();
        double old_collateral_price = this.collateral;
        this.setCollateral();

        this.cust.editCost(this.transPrice, old_trans_price, this.collateral, old_collateral_price);
    }
    
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
