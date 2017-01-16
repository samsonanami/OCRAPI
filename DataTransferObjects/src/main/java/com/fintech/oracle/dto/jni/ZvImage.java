package com.fintech.oracle.dto.jni;

/**
 * Created by sasitha on 12/7/16.
 *
 */
public class ZvImage {

    private byte[] data;
    private String output;
    private String error;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ZvImage{" +
                "output='" + output + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
