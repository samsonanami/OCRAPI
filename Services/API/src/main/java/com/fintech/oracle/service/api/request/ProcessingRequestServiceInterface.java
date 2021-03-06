package com.fintech.oracle.service.api.request;

import com.fintech.oracle.dto.request.VerificationRequest;
import com.fintech.oracle.dto.response.OcrResponse;
import com.fintech.oracle.dto.response.VerificationProcessResponse;
import com.fintech.oracle.service.common.exception.DataNotFoundException;

/**
 * Created by sasitha on 12/4/16.
 *
 */
public interface ProcessingRequestServiceInterface {

    VerificationProcessResponse saveProcessingRequest(VerificationRequest verificationRequest) throws DataNotFoundException;

    OcrResponse getProcessingResult(String code) throws DataNotFoundException;

    void updateJobQueue(String ocrProcessingRequestCode);
}
