package com.mania.zerosheet.ItemInstance;

import org.springframework.stereotype.Repository;
import com.mania.zerosheet.ItemInstance.Instance.Status;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface InstanceRepository extends CrudRepository<Instance, Long>{
    List<Instance> findAllByStatus(Status status);
}
