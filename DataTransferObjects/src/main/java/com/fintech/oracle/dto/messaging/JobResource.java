package com.fintech.oracle.dto.messaging;

import java.io.Serializable;

/**
 * Created by sasitha on 12/16/16.
 *
 */
public class JobResource implements Serializable {
    private static long serialVersionUID = 1L;
    private String resourceId;
    private String resourceName;
    private byte[] data;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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
        JobResource other = (JobResource) obj;
        if (resourceId == null) {
            if (other.resourceId != null)
                return false;
        } else if (!resourceId.equals(other.resourceId))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "JobResource [resourceId=" + resourceId + "]";
    }

}
