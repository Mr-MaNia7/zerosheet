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

  @NotBlank(message = "Customer First Name is required")
  private String name;
  @NotBlank(message = "Customer Middle Name is required")
  private String middleName;
  @NotBlank(message = "Customer Last Name is required")
  private String lastName;
  @Email(message = "Please enter a valid e-mail address")
  @NotBlank(message = "Customer Email Address is required")
  private String email;
  @Pattern(regexp = "^[0][9][0-9]{8}$|^[9][0-9]{8}$", message = "Should be 9/10 characters long, beginning with 9/09")
  @NotBlank(message = "Customer Phone Number is required")
  private String phoneNumber;
  private String houseNumber;
  @NotBlank(message = "Customer City or Town is required")
  private String city;
  private int woreda;
  private String subcity;

  private double totalPrice;
  private double totalPriceVAT;
  private double debtBalance;
  private double totalCollateral;
  private double totalCollateralVAT;

  private double totalPriceP;
  private double totalPriceVATP;
  private double debtBalanceP;
  private double totalCollateralP;
  private double totalCollateralVATP;

  private double totalPriceA;
  private double totalPriceVATA;
  private double debtBalanceA;
  private double totalCollateralA;
  private double totalCollateralVATA;

  private double totalPriceAt;
  private double totalPriceVATAt;
  private double debtBalanceAt;
  private double totalCollateralAt;
  private double totalCollateralVATAt;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
  private List<Transaction> transactions = new ArrayList<Transaction>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "cust")
  private List<Performa> performas = new ArrayList<Performa>();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
  private List<SavedAgreement> savedAgreements = new ArrayList<SavedAgreement>();

  @OneToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "agreement_id", nullable = true)
  Agreement agreement;

  public void addTransaction(Transaction transaction) {
    boolean flag = false;
    for (Transaction eachtrans : this.transactions) {
      if (eachtrans.getItem().getItemId() == transaction.getItem().getItemId()) {
        this.transactions.set((int) (eachtrans.getTransId()), transaction);
        flag = true;
      }
    }
    if (flag == false) {
      this.transactions.add(transaction);
    }
  }

  public void addPerforma(Performa performa) {
    boolean flag = false;
    for (Performa eachperforma : this.performas) {
      if (eachperforma.getItem().getItemId() == performa.getItem().getItemId()) {
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
  public void setAgreementCost(double transPrice, double collateral){
    this.totalPriceAt = transPrice;
    this.totalPriceVATAt = transPrice * 1.15;
    this.totalCollateralAt = collateral;
    this.totalCollateralVATAt = collateral * 1.15;
  }
  public void updateCost(double transPrice, double old_trans_price, double collateral, double old_collateral_price){
    this.totalPrice = this.totalPrice - old_trans_price + transPrice;
    this.totalPriceVAT = this.totalPrice * 1.15;
    this.totalCollateral = this.totalCollateral - old_collateral_price + collateral;
    this.totalCollateralVAT = this.totalCollateral * 1.15;
    this.debtBalance = ((this.debtBalance / 1.15) - old_trans_price + transPrice) * 1.15;
    
    this.setAgreementCost(transPrice, collateral);
  }
  public void updateCost(double transPrice, double collateral) {    
    this.totalPriceP += transPrice;
    this.totalPriceVATP = (this.totalPriceP) * 1.15;
    this.totalCollateralP += collateral;
    this.totalCollateralVATP = (this.totalCollateralP) * 1.15;
    this.debtBalanceP = (this.totalPriceP) * 1.15;

    this.setAgreementCost(transPrice, collateral);
  }
  public void editCost(double transPrice, double old_trans_price, double collateral, double old_collateral_price){
    this.totalPriceP = this.totalPriceP - old_trans_price + transPrice;
    this.totalPriceVATP = this.totalPriceP * 1.15;
    this.totalCollateralP = this.totalCollateralP - old_collateral_price + collateral;
    this.totalCollateralVATP = this.totalCollateralP * 1.15;
    this.debtBalanceP = ((this.debtBalanceP / 1.15) - old_trans_price + transPrice) * 1.15;
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
    this.debtBalanceP = this.totalPriceP * 1.15;
    this.totalCollateralP += totalCollateral;
    this.totalCollateralVATP = this.totalCollateralP * 1.15;

    this.setAgreementCost(totalPrice, totalCollateral);
  }
  public SavedAgreement saveAgreement(List<Performa> performas) {
    SavedAgreement savedAgreement = new SavedAgreement(
        this.name, this.middleName, this.lastName,
        this.phoneNumber, this.houseNumber, this.city,
        this.totalPriceAt, this.debtBalanceAt, this.totalCollateralAt,
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
        this.name, this.middleName, this.lastName,
        this.phoneNumber, this.houseNumber, this.city,
        this.totalPriceAt, this.debtBalanceAt, this.totalCollateralAt,
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
  public void copyTransaction() {
    for (Performa performa : this.performas) {
      Transaction transaction = new Transaction(
          performa.getItemQuantity(), performa.getDueDate(), performa.getDueBackDate(), performa.getDayDifference(),
          performa.getCollateral(), performa.getTransPrice(), performa.getItemPrice(), performa.getCust(), performa.getItem()
          );
      this.addTransaction(transaction);
    }

    this.totalPrice += this.totalPriceP;
    this.totalPriceVAT += this.totalPriceVATP;
    this.debtBalance += this.totalPriceVATP;
    this.totalCollateral += this.totalCollateralP;
    this.totalCollateralVAT += this.totalCollateralVATP;

    this.totalPriceA = this.totalPriceP;
    this.totalPriceVATA = this.totalPriceVATP;
    this.debtBalanceA = this.totalPriceVATP;
    this.totalCollateralA = this.totalCollateralP;
    this.totalCollateralVATA = this.totalCollateralVATP;

    this.setTotalPriceP(0);
    this.setTotalPriceVATP(0);
    this.setDebtBalanceP(0);
    this.setTotalCollateralP(0);
    this.setTotalCollateralVATP(0);
  }

  public void updateTransaction(Transaction transaction, long id) {
    this.transactions.set((int) (id - 1), transaction);
  }

  public Customer(String name, String middleName, String lastName, String email,
    String phoneNumber, String houseNumber, String city, double totalPrice,
    double debtBalance, double totalCollateral, double totalPriceVAT,
    double totalCollateralVAT, double totalPriceP, double debtBalanceP, double totalCollateralP,
    double totalPriceVATP, double totalCollateralVATP, int woreda, String subcity) {
    this.name = name;
    this.middleName = middleName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.houseNumber = houseNumber;
    this.city = city;
    this.subcity = subcity;
    this.woreda = woreda;
    this.totalPrice = totalPrice;
    this.debtBalance = debtBalance;
    this.totalCollateral = totalCollateral;
    this.totalPriceVAT = totalPriceVAT;
    this.totalCollateralVAT = totalCollateralVAT;
    this.totalPriceP = totalPriceP;
    this.debtBalanceP = debtBalanceP;
    this.totalCollateralP = totalCollateralP;
    this.totalPriceVATP = totalPriceVATP;
    this.totalCollateralVATP = totalCollateralVATP;
  }
}