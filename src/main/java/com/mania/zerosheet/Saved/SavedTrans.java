package com.mania.zerosheet.Saved;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SavedTrans implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transId;

    private long dayDifference;
    @NotBlank(message = "Item Name can not be Blank!")
    private String itemName;
    @NotBlank(message = "Measurement Unit can not be Blank!")
    private String unit;
    @Min(value = 1, message = "Item Quantity should be atleast 1")
    // @Digits(10)
    private int itemQuantity = 10;
    @Positive(message = "Area Coverage can not be Negative or Zero!")
    private double areaCoverage;
    private double itemPrice;
    private double transPrice;
    @PositiveOrZero(message = "Unit Price for sale can not be Negative")
    private double unitPrice;
    private double collateral;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "savedagreement_id", nullable = true)
    SavedAgreement savedAgreement;

    public SavedTrans(long dayDifference, String itemName, String unit,
        int itemQuantity, double areaCoverage, double itemPrice,
        double transPrice, double unitPrice, double collateral, SavedAgreement savedAgreement) {
        this.dayDifference = dayDifference;
        this.itemName = itemName;
        this.unit = unit;
        this.itemQuantity = itemQuantity;
        this.areaCoverage = areaCoverage;
        this.itemPrice = itemPrice;
        this.transPrice = transPrice;
        this.unitPrice = unitPrice;
        this.collateral = collateral;
        this.savedAgreement = savedAgreement;
    }
}
