package com.fintech.oracle.service.apollo.exception;

/**
 * Created by sasitha on 12/7/16.
 *
 */
public class JobException extends Exception{

    public JobException(String  message){
        super(message);
    }

    public JobException(String message, Exception e){
        super(message, e);
    }

    public JobException(Exception e){
        super(e);
    }
}
