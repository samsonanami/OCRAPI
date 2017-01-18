package com.fintech.oracle.service.apollo.job;

import com.fintech.oracle.service.apollo.exception.JobException;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@Component
public class IdVerificationJob extends GeneralJob implements Job{
    @Override
    public void doJob(Serializable message) throws JobException {
        startProcessing(message);
    }

}
