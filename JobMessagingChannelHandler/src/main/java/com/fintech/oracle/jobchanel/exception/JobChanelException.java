package com.fintech.oracle.jobchanel.exception;

/**
 * Created by sasitha on 12/6/16.
 *
 */
public class JobChanelException extends Exception{

    public JobChanelException(Exception e){
        super(e);
    }

    public JobChanelException(String message, Exception e){
        super(message, e);
    }

    public JobChanelException(String message){
        super(message);
    }
}
