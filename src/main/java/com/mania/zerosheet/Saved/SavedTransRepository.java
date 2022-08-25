package com.mania.zerosheet.Saved;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedTransRepository extends CrudRepository<SavedTrans, Long> {

}
