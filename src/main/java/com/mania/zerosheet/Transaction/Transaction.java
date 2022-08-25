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
public class Transaction implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transId;

    @Min(value = 1, message = "Item Quantity should be atleast 1")
    // @Digits(10)
    public int itemQuantity = 10;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    public Date dueDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));

    @DateTimeFormat(pattern="yyyy-MM-dd")
    public Date dueBackDate = new Date(dueDate.getTime() + (1000 * 60 * 60 * 24 * 15) + (1000 * 60 * 60 * 24 * 15));

    public double itemPrice;

    public double transPrice;

    public long dayDifference;

    public double collateral;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id", nullable = true)
    Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "item_id", nullable = true)
    Item item;

    public void removeTransaction() {
        this.item.setTotalQuantity(this.item.getTotalQuantity() + this.itemQuantity);
        double oldDebt = this.customer.getDebtBalance() / 1.15;
        double newDebt = (oldDebt - this.transPrice) * 1.15;
        this.customer.setDebtBalance(newDebt);
        this.customer.setTotalCollateral(this.customer.getTotalCollateral() - this.collateral);
        this.customer.setTotalCollateralVAT(this.customer.getTotalCollateral() * 1.15);
    }
    public boolean partialReturn(int returnQuantity) {
        if (returnQuantity == this.itemQuantity) {
            this.removeTransaction();
            return true;
        }
        else {
            int new_item_quantity = this.itemQuantity - returnQuantity;
            double old_trans_price = this.transPrice;
            double new_trans_price = (old_trans_price  / this.itemQuantity) * new_item_quantity;
            int total_quantity = this.item.getTotalQuantity();
    
            double old_collateral_price = this.collateral;
            double collateral_price = this.item.getUnitPrice() * new_item_quantity;
            this.collateral = collateral_price;
            
            double oldDebt = this.customer.getDebtBalance() / 1.15;
            double newDebt = oldDebt - old_trans_price + new_trans_price;
            this.customer.setDebtBalance(newDebt * 1.15);
    
            double old_total_collateral = this.customer.getTotalCollateral();
            double new_total_collateral = old_total_collateral - old_collateral_price + collateral_price;
            this.customer.setTotalCollateral(new_total_collateral);
    
            this.customer.setTotalCollateralVAT(new_total_collateral * 1.15);
    
            this.itemQuantity = new_item_quantity;
            this.transPrice = new_trans_price;
            this.item.setTotalQuantity(total_quantity + returnQuantity);
            return false;
        }
    }
    public Item editTransaction(Transaction new_trans, Item new_item) {
        int old_trans_quantity = this.itemQuantity;
        this.itemQuantity = new_trans.getItemQuantity();
        Item old_item = this.item;
        Item saveItem = new_item;
        if (old_item.getItemId() == new_item.getItemId()) {
            int newQty = new_item.calculateItemQuantity(new_item.getTotalQuantity(), new_trans.getItemQuantity(), old_trans_quantity);
            new_item.setTotalQuantity(newQty);
        }
        else {
            int old_item_Qty = old_item.getTotalQuantity() + this.itemQuantity;
            old_item.setTotalQuantity(old_item_Qty);
            saveItem = old_item;

            int newQty = new_item.calculateItemQuantity(new_item.getTotalQuantity(), new_trans.getItemQuantity(), 0);
            new_item.setTotalQuantity(newQty);
        }

        this.item = new_item;
        this.dueDate = new_trans.getDueDate();
        this.dueBackDate = new_trans.getDueBackDate();
        this.setDayDifference();
        this.itemPrice = new_trans.getItemPrice();
        double old_trans_price = this.transPrice;
        this.setTransPrice();
        double old_collateral_price = this.collateral;
        this.setCollateral();

        this.customer.updateCost(this.transPrice, old_trans_price, this.collateral, old_collateral_price);
        return saveItem;
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
    public long calculateDayDifference(Date fromDate, Date toDate) {
        long difference_In_Time = fromDate.getTime() - toDate.getTime();
        long difference_In_Days = 
        TimeUnit
              .MILLISECONDS
              .toDays(difference_In_Time);
        return difference_In_Days;
    }

    public Transaction(int itemQuantity, Date dueDate, Date dueBackDate, 
    long dayDifference, double collateral, double transPrice, double itemPrice, Customer customer, Item item) {
        this.itemQuantity = itemQuantity;
        this.dueDate = dueDate;
        this.dueBackDate = dueBackDate;
        this.dayDifference = dayDifference;
        this.collateral = collateral;
        this.transPrice = transPrice;
        this.itemPrice = itemPrice;
        this.customer = customer;
        this.item = item;
    }
}
