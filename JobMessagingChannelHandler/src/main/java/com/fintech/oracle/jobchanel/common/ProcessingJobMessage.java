package com.fintech.oracle.jobchanel.common;

import java.io.Serializable;

/**
 * Created by sasitha on 12/6/16.
 *
 */
public class ProcessingJobMessage implements Serializable{

    private String processId;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
