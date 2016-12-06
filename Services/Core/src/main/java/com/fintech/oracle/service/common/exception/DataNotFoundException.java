package com.fintech.oracle.service.common.exception;

/**
 * Created by sasitha on 12/5/16.
 *
 */
public class DataNotFoundException extends NotFoundException {

    public DataNotFoundException(){

    }

    public DataNotFoundException(Exception e){
        super(e);
    }

    public DataNotFoundException(String message, Exception e){
        super(message, e);
    }

    public DataNotFoundException(String message){
        super(message);
    }
}
