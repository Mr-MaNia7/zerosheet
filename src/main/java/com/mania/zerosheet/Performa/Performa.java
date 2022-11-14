package com.mania.zerosheet.Performa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import com.mania.zerosheet.Transaction.Transaction;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Performa implements Serializable {
    private final long oneDay = 1000L * 60L * 60L * 24L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transId;
    
    @Min(value = 1, message = "Item Quantity should be atleast 1")
    private int itemQuantity = 10;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueDate = new Date(new Date().getTime());

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueBackDate = new Date(dueDate.getTime() + oneDay * 30L);

    @Min(value = 1, message = "Item Price should be atleast 1")
    private double itemPrice;

    private double transPrice;

    private long dayDifference;

    private double collateral;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "cust_id", nullable = true)
    Customer cust;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "item_id", nullable = true)
    Item item;

    public boolean isDuplicate(List<Transaction> transactions, List<Performa> performas){
        for (Transaction trans : transactions){
            if (
                trans.getItem().getItemId() == this.item.getItemId() &&
                trans.getItemPrice() == this.itemPrice &&
                this.calculateDayDifference(this.dueDate, trans.getDueDate()) == 0 &&
                this.calculateDayDifference(this.dueBackDate, trans.getDueBackDate()) == 0
            ){
                return true;
            }
        }
        for (Performa perf : performas){
            if (
                perf.getItem().getItemId() == this.getItem().getItemId() &&
                perf.getItemPrice() == this.getItemPrice() &&
                perf.getItemQuantity() == this.getItemQuantity() &&
                this.calculateDayDifference(this.dueDate, perf.getDueDate()) == 0 &&
                this.calculateDayDifference(this.dueBackDate, perf.getDueBackDate()) == 0
            ){
                return true;
            }
        }

        return false;
    }
    public void addNewTransaction(double remainingPrice){
        this.setDayDifference();
        this.setTransPrice(remainingPrice);
        this.setCollateral();
    }
    public void addTrans2ExistinCust(Customer customer){
        this.cust = customer;
        this.addNewTransaction(customer.getRemainingPrice()/1.15);
        customer.updateCost(this.transPrice, this.collateral);
    }
    public void setDayDifference() {
        long difference_In_Time = this.dueBackDate.getTime() - this.dueDate.getTime();
        this.dayDifference = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time);
    }
    public void setTransPrice(double remainingPrice){
        this.transPrice = (this.itemPrice * this.dayDifference * this.itemQuantity) - remainingPrice;
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
    public long calculateDayDifference(Date first, Date second){
        long difference_In_Time = first.getTime() - second.getTime();
        long difference_In_Days = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time);

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
        this.setTransPrice(this.cust.getRemainingPrice()/1.15);
        double old_collateral_price = this.collateral;
        this.setCollateral();

        this.cust.editCost(this.transPrice, old_trans_price, this.collateral, old_collateral_price);
    }
    public void reverseDayDifference(long dayDifference){
        this.dueDate =  new Date(new Date().getTime());
        this.dueBackDate =  new Date(this.dueDate.getTime() + oneDay * dayDifference);
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
