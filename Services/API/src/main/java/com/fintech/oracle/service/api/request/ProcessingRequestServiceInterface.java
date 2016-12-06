package com.fintech.oracle.service.api.request;

import com.fintech.oracle.dataabstraction.entities.ProcessingRequest;
import com.fintech.oracle.dto.request.VerificationRequest;
import com.fintech.oracle.dto.response.OcrResponse;
import com.fintech.oracle.dto.response.VerificationProcessResponse;
import com.fintech.oracle.service.common.exception.ConfigurationDataNotFoundException;
import com.fintech.oracle.service.common.exception.DataNotFoundException;

import javax.jms.JMSException;

/**
 * Created by sasitha on 12/4/16.
 *
 */
public interface ProcessingRequestServiceInterface {

    VerificationProcessResponse saveProcessingRequest(VerificationRequest verificationRequest) throws ConfigurationDataNotFoundException, DataNotFoundException;

    OcrResponse getProcessingResult(String code) throws DataNotFoundException;
}
