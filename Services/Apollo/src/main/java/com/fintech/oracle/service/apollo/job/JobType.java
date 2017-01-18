package com.fintech.oracle.service.apollo.job;

/**
 * Created by sasitha on 12/7/16.
 *
 */
public enum  JobType {
    ID_VERIFICATION("ID_VERIFICATION"),
    ADDRESS_VERIFICATION("ADDRESS_VERIFICATION")
    ;

    private final String jobName;

    JobType(String jobName){
        this.jobName = jobName;
    }

    public String getJobName(){
        return this.jobName;
    }
}
