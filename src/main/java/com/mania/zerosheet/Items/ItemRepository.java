package com.mania.zerosheet.Items;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ItemRepository extends 
JpaRepository<Item, Long>{    
    Item findByItemName(String itemName);
}
