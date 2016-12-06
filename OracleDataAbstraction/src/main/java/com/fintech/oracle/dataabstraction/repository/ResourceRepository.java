package com.fintech.oracle.dataabstraction.repository;

import com.fintech.oracle.dataabstraction.entities.Resource;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by sasitha on 12/5/16.
 *
 */
public interface ResourceRepository extends CrudRepository<Resource, Integer>{

    List<Resource> findResourcesByResourceIdentificationCode(String code);
}
