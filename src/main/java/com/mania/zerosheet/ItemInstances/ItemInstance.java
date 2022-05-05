package com.mania.zerosheet.ItemInstances;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;
import com.mania.zerosheet.Items.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ItemInstance implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long instanceId;

    @Positive(message =  "Item Quantity must be a positive number!")
    private int itemQuantity;

    private Status category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;
    
    public ItemInstance(int itemQuantity, Status category, Item item) {
        this.itemQuantity = itemQuantity;
        this.category = category;
        this.item = item;
    }
}
