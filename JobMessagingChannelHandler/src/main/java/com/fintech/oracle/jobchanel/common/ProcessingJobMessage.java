package com.fintech.oracle.jobchanel.common;

import java.io.Serializable;

/**
 * Created by sasitha on 12/6/16.
 *
 */
public class ProcessingJobMessage implements Serializable{

   private Integer ocrProcessId;

    public Integer getOcrProcessId() {
        return ocrProcessId;
    }

    public void setOcrProcessId(Integer ocrProcessId) {
        this.ocrProcessId = ocrProcessId;
    }
}
