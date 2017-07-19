package com.fintech.oracle.dataabstraction.repository;

import com.fintech.oracle.dataabstraction.entities.Resource;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by sasitha on 12/5/16.
 *
 */
public interface ResourceRepository extends CrudRepository<Resource, Integer>{

    Resource findResourcesByResourceIdentificationCode(String code);
}
