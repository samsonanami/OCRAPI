package com.fintech.oracle.service.apollo.job;

import com.fintech.oracle.dataabstraction.entities.OcrProcess;
import com.fintech.oracle.dto.messaging.JobResource;
import com.fintech.oracle.dto.messaging.ProcessingJobMessage;
import com.fintech.oracle.service.apollo.exception.JobException;
import com.fintech.oracle.service.common.exception.DataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@Component
public class AddressVerificationJob extends GeneralJob implements Job{

    @Override
    public void doJob(Serializable message) throws JobException {
        startProcessing(message);
    }
}
