package com.fintech.oracle.dataabstraction.repository;

import com.fintech.oracle.dataabstraction.entities.OcrProcessingRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by sasitha on 12/4/16.
 *
 */

public interface OcrProcesingRequestRepositoryInterface extends CrudRepository<OcrProcessingRequest, Integer> {

    
    List<OcrProcessingRequest> findOcrProcessingRequestsByProcessingRequestCode(String code);
}
