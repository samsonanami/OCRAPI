package com.fintech.oracle.service.apollo.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@Component
public class JobFactory {

    @Autowired
    IdVerificationJob idVerificationJob;

    @Autowired
    AddressVerificationJob addressVerificationJob;

    public Job getJob(String jobType){
        JobType type = JobType.valueOf(jobType);
        switch (type){
            case idVerification:
                return idVerificationJob;
            case addressVerification:
                return addressVerificationJob;
        }
        return null;
    }
}
