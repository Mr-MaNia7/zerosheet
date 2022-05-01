package com.mania.zerosheet.Items;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import com.mania.zerosheet.Transaction.Transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemId;
    
    @NotBlank(message = "Item Name can not be Blank!")
    private String itemName;
    @NotBlank(message = "Measurement Unit can not be Blank! ")
    private String unit;
    @PositiveOrZero(message = "Unit-Price can not be Negative")
    private double unitPrice;
    @PositiveOrZero(message = "Total Quantity can not be Negative")
    private int totalQuantity;
    @Positive(message = "Area Coverage can not be Negative or Zero!")
    private double areaCoverage;
    // @NotBlank(message = "Status category can not be Blank!")
    private Status category;

    // Inverse side - One side
    @OneToMany(mappedBy = "item")
    Set<Transaction> transactions;

    // manual constructors for the commandlinerunner
    public Item(String itemName, String unit, double unitPrice, 
    int totalQuantity, double areaCoverage, Status category) {
        this.itemName = itemName;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.totalQuantity = totalQuantity;
        this.areaCoverage = areaCoverage;
        this.category = category;
    }
}
