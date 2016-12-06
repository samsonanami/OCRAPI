package com.fintech.oracle.dataabstraction.repository;

import com.fintech.oracle.dataabstraction.entities.OcrProcessingStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by sasitha on 12/5/16.
 * 
 */
public interface OcrProcessingStatusRepository extends CrudRepository<OcrProcessingStatus, Integer> {
    
    List<OcrProcessingStatus> findOcrProcessingStatusByStatus(String status);
}
