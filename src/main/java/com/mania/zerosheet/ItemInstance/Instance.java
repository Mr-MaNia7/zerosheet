package com.mania.zerosheet.ItemInstance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Instance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instanceId")
    private long instanceId;
    
    @Min(value = 1, message = "Item Quantity should be atleast 1")
    public int itemQuantity;

    @Enumerated(EnumType.STRING)
    private Status status;
    public static enum Status{
        ONLOAN, AVAILABLE, MAINTENANCE, DEFECTED  
    }
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "item_id", nullable = true)
    private Item item;
    
    @OneToOne(mappedBy = "instance", cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "trans_id", referencedColumnName = "transId")
    private Transaction transaction;

    public void updateItemQuantity(int loanQuantity){
        this.itemQuantity = this.itemQuantity - loanQuantity;
    }
    
    public Instance(int itemQuantity, Status status, Item item)
    {
        this.itemQuantity = itemQuantity;
        this.item = item;
        this.status = status;
    }
}
