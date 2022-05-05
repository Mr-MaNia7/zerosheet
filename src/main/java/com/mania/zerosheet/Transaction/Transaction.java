package com.mania.zerosheet.Transaction;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transId;
    
    @NotBlank(message = "Transaction Name can not be blank!")
    private String transName;
    
    @Positive(message =  "Item Quantity must be a negative number!")
    private int itemQuantity;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueDate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueBackDate;

    // @NotNull
    // @Size(min = 1, message = "You must at least choose one item")
    // @ManyToMany
    // private Set<Item> items;

    @ManyToOne
    Customer customer;

    // public void addItem(Item item) {
    //     this.items.add(item);
    // }
}
