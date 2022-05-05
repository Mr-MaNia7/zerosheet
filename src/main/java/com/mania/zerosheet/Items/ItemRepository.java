package com.mania.zerosheet.Items;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends 
CrudRepository<Item, Long>{
    Item findByItemName(String itemName);
}
