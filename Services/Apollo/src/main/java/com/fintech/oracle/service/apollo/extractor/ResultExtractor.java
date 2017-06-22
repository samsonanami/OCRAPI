package com.fintech.oracle.service.apollo.extractor;

import com.fintech.oracle.dataabstraction.entities.OcrProcess;
import com.fintech.oracle.dataabstraction.entities.OcrResult;
import com.fintech.oracle.dataabstraction.entities.ResourceName;

import java.util.List;

/**
 * Created by sasitha on 6/22/17.
 *
 */
public interface ResultExtractor<E>{

    List<OcrResult> extractOcrResultSet(E inputObject, OcrProcess ocrProcess, ResourceName resourceName, String preProcessedStatus);
}
