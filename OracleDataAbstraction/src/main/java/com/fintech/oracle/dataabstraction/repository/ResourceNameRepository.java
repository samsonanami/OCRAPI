package com.fintech.oracle.dataabstraction.repository;

import com.fintech.oracle.dataabstraction.entities.ResourceName;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by sasitha on 12/9/16.
 *
 */
public interface ResourceNameRepository extends CrudRepository<ResourceName, Integer>{

    ResourceName findResourceNameByName(String name);
}
