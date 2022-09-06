package com.mania.zerosheet.ItemInstance;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface InstanceRepository extends CrudRepository<Instance, Long>{
    
}
