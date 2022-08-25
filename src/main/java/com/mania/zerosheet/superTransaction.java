package com.mania.zerosheet;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;
// import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
// import javax.persistence.Id;
import javax.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// @Entity
public class superTransaction implements Serializable {
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

}
