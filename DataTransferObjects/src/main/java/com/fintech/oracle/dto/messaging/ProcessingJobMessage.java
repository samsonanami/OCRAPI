package com.fintech.oracle.dto.messaging;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sasitha messaging on 12/6/16.
 *
 */
public class ProcessingJobMessage implements Serializable{
    private static long serialVersionUID = 2L;
    private String ocrProcessId;
    private String ocrProcessingRequestId;
    private String resourceId;
    private String resourceName;
    private byte[] imageData;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long serialVersionUID) {
        ProcessingJobMessage.serialVersionUID = serialVersionUID;
    }

    public String getOcrProcessingRequestId() {
        return ocrProcessingRequestId;
    }

    public void setOcrProcessingRequestId(String ocrProcessingRequestId) {
        this.ocrProcessingRequestId = ocrProcessingRequestId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

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
        result = prime * result + ((resourceId == null) ? 0 : resourceId.hashCode());
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
        if (resourceId == null) {
            if (other.resourceId != null)
                return false;
        } else if (!resourceId.equals(other.resourceId))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "ProcessingJobMessage [" +
                " ocrProcessId = " + ocrProcessId +
                " ocrProcessingRequestId = " + ocrProcessingRequestId +
                " resourceId = " + resourceId +
                " resourceName = " + resourceName
                +"]";
    }

}
