package com.mania.zerosheet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {
    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemId;
    // @NotBlank(message = "Item Name is required")
    private String itemName;
    // @NotBlank(message = "Quantity ")
    // private String unit;
    private Category category;
}
