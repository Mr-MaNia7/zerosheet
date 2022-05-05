package com.mania.zerosheet.ItemInstances;

import java.util.List;

import com.mania.zerosheet.Items.Item;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceRepository extends 
CrudRepository<ItemInstance, Long>{
    ItemInstance findByCategory(Status category);
    List<ItemInstance> findByItem(Item item, Sort sort);
}
