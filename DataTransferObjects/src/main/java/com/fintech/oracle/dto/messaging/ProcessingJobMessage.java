package com.fintech.oracle.dto.messaging;

import java.io.Serializable;

/**
 * Created by sasitha messaging on 12/6/16.
 *
 */
public class ProcessingJobMessage implements Serializable{
    private static long serialVersionUID = 1113799434508676095L;
    private String ocrProcessId;

    public String getOcrProcessId() {
        return ocrProcessId;
    }

    public void setOcrProcessId(String ocrProcessId) {
        this.ocrProcessId = ocrProcessId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ocrProcessId == null) ? 0 : ocrProcessId.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProcessingJobMessage other = (ProcessingJobMessage) obj;
        if (ocrProcessId == null) {
            if (other.ocrProcessId != null)
                return false;
        } else if (!ocrProcessId.equals(other.ocrProcessId))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "ProcessingJobMessage [ocrProcessId=" + ocrProcessId + "]";
    }

}
