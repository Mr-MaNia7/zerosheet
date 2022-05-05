package com.mania.zerosheet.Transaction;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import com.mania.zerosheet.Customers.Customer;
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
    
    @NotBlank(message = "Transaction Name can not be blank!")
    private String transName;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueDate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueBackDate;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id", nullable = true)
    Customer customer;

    public Transaction(String transName, Date dueDate, Date dueBackDate, Customer customer) {
        this.transName = transName;
        this.dueDate = dueDate;
        this.dueBackDate = dueBackDate;
        this.customer = customer;
    }
}
