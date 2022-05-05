package com.mania.zerosheet.Items;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
