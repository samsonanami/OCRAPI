package com.fintech.oracle.service.apollo.job;

import com.fintech.oracle.dataabstraction.entities.OcrProcess;
import com.fintech.oracle.dataabstraction.entities.OcrResult;
import com.fintech.oracle.dataabstraction.entities.Resource;
import com.fintech.oracle.service.common.exception.DataNotFoundException;

/**
 * Created by sasitha on 12/7/16.
 *
 */
public interface JobDetailServiceInterface {

    OcrProcess getOcrProcessDetails(String jobId) throws DataNotFoundException;

    Resource getResourceDetails(Integer resourceId) throws DataNotFoundException;

    void saveOcrResults(OcrResult results);
}
