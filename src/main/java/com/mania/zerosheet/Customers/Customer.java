package com.mania.zerosheet.Customers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import com.mania.zerosheet.Agreement.Agreement;
import com.mania.zerosheet.ItemInstance.Instance;
import com.mania.zerosheet.ItemInstance.Instance.Status;
import com.mania.zerosheet.Items.Item;
import com.mania.zerosheet.Performa.Performa;
import com.mania.zerosheet.Saved.SavedAgreement;
import com.mania.zerosheet.Saved.SavedTrans;
import com.mania.zerosheet.Transaction.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Customer implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank(message = "Customer Full Name is required")
  private String fullName;
  @Email(message = "Please enter a valid e-mail address")
  private String email;
  @Pattern(regexp = "^[0][9][0-9]{8}$|^[9][0-9]{8}$|[0][7][0-9]{8}$|^[7][0-9]{8}$", message = "Should be 9/10 characters long, beginning with 9/09 or 7/07")
  @NotBlank(message = "Customer Phone Number is required")
  private String phoneNumber;
  private String houseNumber;
  @NotBlank(message = "Customer City or Town is required")
  private String city;
  private int woreda;
  private String subcity;

  private double totalPrice;
  private double totalPriceVAT;
  private double remainingPrice;
  private double totalCollateral;
  private double totalCollateralVAT;

  private double totalPriceP;
  private double totalPriceVATP;
  private double remainingPriceP;
  private double totalCollateralP;
  private double totalCollateralVATP;

  private double totalPriceA;
  private double totalPriceVATA;
  private double remainingPriceA;
  private double totalCollateralA;
  private double totalCollateralVATA;

  private double totalPriceAt;
  private double totalPriceVATAt;
  private double remainingPriceAt;
  private double totalCollateralAt;
  private double totalCollateralVATAt;


  @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", orphanRemoval = true)
  private List<Transaction> transactions = new ArrayList<Transaction>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "cust")
  private List<Performa> performas = new ArrayList<Performa>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
  private List<SavedAgreement> savedAgreements = new ArrayList<SavedAgreement>();

  @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Instance> instances = new ArrayList<Instance>();

  @OneToOne(fetch = FetchType.EAGER, optional = true)
  @JoinColumn(name = "agreement_id", nullable = true)
  Agreement agreement;

  public void clearMonetary(){
    this.totalPrice = 0;
    this.totalPriceVAT = 0;
    this.remainingPrice = 0;
    this.totalCollateral = 0;
    this.totalCollateralVAT = 0;

    this.totalPriceP = 0;
    this.totalPriceVATP = 0;
    this.remainingPriceP = 0;
    this.totalCollateralP = 0;
    this.totalCollateralVATP = 0;

    this.totalPriceA = 0;
    this.totalPriceVATA = 0;
    this.remainingPriceA = 0;
    this.totalCollateralA = 0;
    this.totalCollateralVATA = 0;

    this.totalPriceAt = 0;
    this.totalPriceVATAt = 0;
    this.remainingPriceAt = 0;
    this.totalCollateralAt = 0;
    this.totalCollateralVATAt = 0;
  }

  public void addInstance(Instance instance){
    this.instances.add(instance);
  }
  public Instance findInstance(Status status, Item item){
    for (Instance instance : this.instances){
      if (instance.getStatus().equals(status) &&
          instance.getItem().getItemId() == item.getItemId()){
        return instance;
      }
    }
    Instance defaultInstance = new Instance();
    defaultInstance.setStatus(status);
    defaultInstance.setItem(item);
    defaultInstance.setCustomer(this);
    return defaultInstance;
  }

  public void updateInstance(Status status, int quantity, Item item){
    Instance f_instance = this.findInstance(status, item);
    f_instance.setItemQuantity(f_instance.getItemQuantity() + quantity);
    this.addInstance(f_instance);
  }

  public void addTransaction(Transaction transaction) {
    boolean flag = false;
    for (Transaction eachtrans : this.transactions) {
      if (eachtrans.getItem().getItemId() == transaction.getItem().getItemId() &&
          eachtrans.getDueBackDate() == transaction.getDueBackDate() &&
          eachtrans.getDueDate() == transaction.getDueDate()
        ) {
        this.transactions.set((int) (eachtrans.getTransId()), transaction);
        flag = true;
      }
    }
    if (flag == false) {
      this.transactions.add(transaction);
    }
    Item item  = transaction.getItem();
    
    Instance l_instance = new Instance(transaction.getItemQuantity(), Status.ONLOAN, item, this);
    this.addInstance(l_instance);
    transaction.setInstance(l_instance);
  }

  public void addPerforma(Performa performa) {
    boolean flag = false;
    for (Performa eachperforma : this.performas) {
      if (eachperforma.getItem().getItemId() == performa.getItem().getItemId() &&
          eachperforma.getDueDate() == performa.getDueDate()
      ) {
        this.performas.set((int) (eachperforma.getTransId()), performa);
        flag = true;
      }
    }
    if (flag == false) {
      this.performas.add(performa);
    }
  }

  public void removePerformas(List<Performa> performas) {
    this.performas.removeAll(performas);
  }
  public void removeTransactions(List<Transaction> transactions) {
    this.transactions.removeAll(transactions);
  }
  public void setAgreementCost(double transPrice, double collateral){
    this.totalPriceAt = transPrice;
    this.totalPriceVATAt = this.totalPriceAt * 1.15;
    this.totalCollateralAt = collateral;
    this.totalCollateralVATAt = collateral * 1.15;
  }
  public void updateCost(double transPrice, double old_trans_price, double collateral, double old_collateral_price){
    this.totalPrice = this.totalPrice - old_trans_price + transPrice;
    this.remainingPrice = (this.remainingPrice - (transPrice - old_trans_price) * 1.15 < 0)? 0 : this.remainingPrice - (transPrice - old_trans_price) * 1.15;
    this.totalPriceVAT = this.totalPrice * 1.15;
    this.totalCollateral = this.totalCollateral - old_collateral_price + collateral;
    this.totalCollateralVAT = this.totalCollateral * 1.15;
    
    this.setAgreementCost(transPrice, collateral);
  }
  public void updateCost(double transPrice, double collateral) {
    this.remainingPriceP = this.remainingPrice;
    this.totalPriceP += transPrice;
    this.remainingPriceP = (this.remainingPriceP - transPrice * 1.15 < 0)? 0 : this.remainingPriceP - transPrice * 1.15;
    this.totalPriceVATP = (this.totalPriceP) * 1.15;
    this.totalCollateralP += collateral;
    this.totalCollateralVATP = (this.totalCollateralP) * 1.15;
    
    this.setAgreementCost(transPrice, collateral);
  }
  public void editCost(double transPrice, double old_trans_price, double collateral, double old_collateral_price){
    this.remainingPriceP = this.remainingPrice;
    this.totalPriceP = this.totalPriceP - old_trans_price + transPrice;
    this.remainingPriceP = (this.remainingPriceP - transPrice * 1.15 < 0)? 0 : this.remainingPriceP - transPrice * 1.15;
    this.totalPriceVATP = this.totalPriceP * 1.15;
    this.totalCollateralP = this.totalCollateralP - old_collateral_price + collateral;
    this.totalCollateralVATP = this.totalCollateralP * 1.15;
  }
  public void setOrderCost() {
    this.performas.forEach(performa -> performa.setCust(this));
    double totalPrice = 0.0;
    double totalCollateral = 0.0;
    for (Performa performa : this.performas) {
      totalPrice += performa.getTransPrice();
      totalCollateral += performa.getCollateral();
    }
    this.totalPriceP += totalPrice;
    this.totalPriceVATP = this.totalPriceP * 1.15;
    this.totalCollateralP += totalCollateral;
    this.totalCollateralVATP = this.totalCollateralP * 1.15;

    this.setAgreementCost(totalPrice, totalCollateral);
  }
  public SavedAgreement saveAgreement(List<Performa> performas) {
    SavedAgreement savedAgreement = new SavedAgreement(
        this.fullName, this.phoneNumber, this.houseNumber, this.city,
        this.totalPriceAt, this.remainingPriceAt, this.totalCollateralAt,
        this.totalPriceVATAt, this.totalCollateralVATAt, this.woreda, this.subcity,
        new Date(), this
      );
    this.savedAgreements.add(savedAgreement);

    for (Performa performa : performas) {
      SavedTrans savedTrans = new SavedTrans(
        performa.getDayDifference(), performa.getItem().getItemName(),
        performa.getItem().getUnit(),
        performa.getItemQuantity(), performa.getItem().getAreaCoverage(), performa.getItemPrice(),
        performa.getTransPrice(), performa.getItem().getUnitPrice(), performa.getCollateral(),
        savedAgreement
      );
      savedAgreement.addSavedTrans(savedTrans);
    }
    return savedAgreement;
  }
  public SavedAgreement saveEditedAgreement(Transaction transaction) {
    SavedAgreement savedAgreement = new SavedAgreement(
        this.fullName, this.phoneNumber, this.houseNumber, this.city,
        this.totalPriceAt, this.remainingPriceAt, this.totalCollateralAt,
        this.totalPriceVATAt, this.totalCollateralVATAt, this.woreda, this.subcity,
        new Date(), this
      );
    this.savedAgreements.add(savedAgreement);

    SavedTrans savedTrans = new SavedTrans(
      transaction.getDayDifference(), transaction.getItem().getItemName(),
      transaction.getItem().getUnit(),
      transaction.getItemQuantity(), transaction.getItem().getAreaCoverage(), transaction.getItemPrice(),
      transaction.getTransPrice(), transaction.getItem().getUnitPrice(), transaction.getCollateral(),
      savedAgreement
    );
    savedAgreement.addSavedTrans(savedTrans);
    return savedAgreement;
  }
  public void copyPerforma2Transaction() {
    for (Performa performa : this.performas) {
      performa.reverseDayDifference(performa.getDayDifference());
      Transaction transaction = new Transaction(
          performa.getItemQuantity(), performa.getDueDate(), performa.getDueBackDate(), performa.getDayDifference(),
          performa.getCollateral(), performa.getTransPrice(), performa.getItemPrice(), performa.getCust(), performa.getItem()
          );
      this.addTransaction(transaction);
    }

    this.totalPrice += this.totalPriceP;
    this.totalPriceVAT += this.totalPriceVATP;
    this.remainingPrice = this.remainingPriceP;
    this.totalCollateral += this.totalCollateralP;
    this.totalCollateralVAT += this.totalCollateralVATP;

    this.totalPriceA = this.totalPriceP;
    this.totalPriceVATA = this.totalPriceVATP;
    this.remainingPriceA = this.remainingPriceP;
    this.totalCollateralA = this.totalCollateralP;
    this.totalCollateralVATA = this.totalCollateralVATP;

    this.setTotalPriceP(0);
    this.setTotalPriceVATP(0);
    this.setRemainingPriceP(0);
    this.setTotalCollateralP(0);
    this.setTotalCollateralVATP(0);
  }

  public void updateTransaction(Transaction transaction, long id) {
    this.transactions.set((int) (id - 1), transaction);
  }

  public Customer(String fullName, String email,
    String phoneNumber, String houseNumber, String city, double totalPrice,
    double remainingPrice, double totalCollateral, double totalPriceVAT,
    double totalCollateralVAT, double totalPriceP, double remainingPriceP, double totalCollateralP,
    double totalPriceVATP, double totalCollateralVATP, int woreda, String subcity) {
    this.fullName = fullName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.houseNumber = houseNumber;
    this.city = city;
    this.subcity = subcity;
    this.woreda = woreda;
    this.totalPrice = totalPrice;
    this.remainingPrice = remainingPrice;
    this.totalCollateral = totalCollateral;
    this.totalPriceVAT = totalPriceVAT;
    this.totalCollateralVAT = totalCollateralVAT;
    this.totalPriceP = totalPriceP;
    this.remainingPriceP = remainingPriceP;
    this.totalCollateralP = totalCollateralP;
    this.totalPriceVATP = totalPriceVATP;
    this.totalCollateralVATP = totalCollateralVATP;
  }
}