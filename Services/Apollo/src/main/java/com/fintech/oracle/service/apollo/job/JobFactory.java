package com.fintech.oracle.service.apollo.job;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@Component
public class JobFactory {

    @Autowired
    private IdVerificationJob idVerificationJob;

    @Autowired
    private AddressVerificationJob addressVerificationJob;

    public Job getJob(String jobType){
        String jobTypeString = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, jobType);
        JobType type = JobType.valueOf(jobTypeString);
        Job job = null;
        if (type.equals(JobType.ID_VERIFICATION)){
            job = idVerificationJob;
        }else if (type.equals(JobType.ADDRESS_VERIFICATION)){
            job = addressVerificationJob;
        }
        return job;
    }
}
