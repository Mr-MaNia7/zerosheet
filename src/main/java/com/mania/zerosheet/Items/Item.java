package com.mania.zerosheet.Items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

import com.mania.zerosheet.Customers.Customer;
import com.mania.zerosheet.ItemInstance.Instance;
import com.mania.zerosheet.ItemInstance.Instance.Status;
import com.mania.zerosheet.Performa.Performa;
import com.mania.zerosheet.Transaction.Transaction;
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

    @NotBlank(message = "Measurement Unit can not be Blank!")
    private String unit;

    @PositiveOrZero(message = "Unit Price for loan can not be Negative")
    private double unitLoanPrice;

    @PositiveOrZero(message = "Unit Price for sale can not be Negative")
    private double unitPrice;

    @PositiveOrZero(message = "Total Quantity can not be Negative")
    private int totalQuantity;

    private int loanedQuantity;
    private int maintenanceQuantity;
    private int defectedQuantity;

    @Positive(message = "Area Coverage can not be Negative or Zero!")
    private double areaCoverage;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Instance> instances = new ArrayList<Instance>();
    
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<Transaction>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Performa> performas = new ArrayList<Performa>();
    
    public void addInstance(Instance instance) {
        this.instances.add(instance);
    }
    public void updateInstance(long id, Instance instance) {
        this.instances.set((int) id, instance);
    }
    public void removeInstance(Instance instance){
        this.instances.remove(instance);
    }
    public Instance defaultInstance(){
        Instance av_instance = this.findAvailableInstance();
        av_instance.setItemQuantity(this.totalQuantity);
        return av_instance;
    }
    public Instance findAvailableInstance(){
        for (Instance instance : this.instances){
            if (instance.getStatus() == Status.AVAILABLE) {
                return instance;
            }
        }
        Instance defaultInstance = new Instance();
        defaultInstance.setStatus(Status.AVAILABLE);
        defaultInstance.setItem(this);
        return defaultInstance;
    }
    public void updateAvailableInstance(){
        Instance av_instance = this.findAvailableInstance();
        av_instance.setItemQuantity(this.totalQuantity);
        this.addInstance(av_instance);
    }
    public void addMaintenanceInstance(int maintenanceQty, Customer cust){
        Instance m_instance = new Instance(maintenanceQty, Status.MAINTENANCE, this, cust);
        this.addInstance(m_instance);
        this.maintenanceQuantity += maintenanceQty;
    }
    public void addDefectedInstance(int defectedQty, Customer cust){
        Instance d_instance = new Instance(defectedQty, Status.DEFECTED, this, cust);
        this.addInstance(d_instance);
        this.defectedQuantity += defectedQty;
    }

    public int calculateItemQuantity(int old_item_quantity, int trans_item_quantity, int old_trans_quantity){
        return old_item_quantity - trans_item_quantity + old_trans_quantity;
    }
    
    public Item(String itemName, String unit, double unitLoanPrice, double unitPrice, double areaCoverage, int totalQuantity)
    {
        this.itemName = itemName;
        this.unit = unit;
        this.unitLoanPrice = unitLoanPrice;
        this.unitPrice = unitPrice;
        this.totalQuantity = totalQuantity;
        this.areaCoverage = areaCoverage;
    }
}
