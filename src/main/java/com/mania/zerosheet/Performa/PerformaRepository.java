package com.mania.zerosheet.Performa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformaRepository extends 
    CrudRepository<Performa, Long>{
        
}
