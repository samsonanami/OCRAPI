package com.fintech.oracle.dataabstraction.repository;

import com.fintech.oracle.dataabstraction.entities.OcrProcessType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by sasitha on 12/5/16.
 *
 */
public interface OcrProcessTypeRepository extends CrudRepository<OcrProcessType, Integer> {

    List<OcrProcessType> findOcrProcessTypesByType(String type);
}
