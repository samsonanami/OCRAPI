package com.fintech.oracle.service.apollo.job;

import com.fintech.oracle.service.apollo.exception.JobException;

import java.io.Serializable;

/**
 * Created by sasitha on 12/7/16.
 *
 */
@FunctionalInterface
public interface Job {

    void doJob(Serializable jobMessage) throws JobException;
}
