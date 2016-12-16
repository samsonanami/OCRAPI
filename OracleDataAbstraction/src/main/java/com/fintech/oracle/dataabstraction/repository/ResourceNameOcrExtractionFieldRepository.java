package com.fintech.oracle.dataabstraction.repository;

import com.fintech.oracle.dataabstraction.entities.ResourceName;
import com.fintech.oracle.dataabstraction.entities.ResourceNameOcrExtractionField;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by sasitha on 12/8/16.
 *
 */
public interface ResourceNameOcrExtractionFieldRepository extends CrudRepository<ResourceNameOcrExtractionField, Integer>{

    List<ResourceNameOcrExtractionField> findResourceNameOcrExtractionFieldsByResourceName(ResourceName resourceName);
}
