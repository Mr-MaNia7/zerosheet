package com.mania.zerosheet.Items;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class Item implements Serializable{
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

    // @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // private Set<ItemInstance> instances;

    
    public Item(String itemName, String unit, double unitPrice, int totalQuantity, double areaCoverage )
    {
        this.itemName = itemName;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.totalQuantity = totalQuantity;
        this.areaCoverage = areaCoverage;
    }
}
