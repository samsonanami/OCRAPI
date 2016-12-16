package com.fintech.oracle.service.apollo.job;

/**
 * Created by sasitha on 12/7/16.
 *
 */
public enum  JobType {
    idVerification ("idVerification"),
    addressVerification ("addressVerification")
    ;

    private final String jobName;

    JobType(String jobName){
        this.jobName = jobName;
    }

    public String getJobName(){
        return this.jobName;
    }
}
