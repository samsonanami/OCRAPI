package com.fintech.oracle.service.common.exception;

/**
 * Created by sasitha on 12/5/16.
 *
 */
public class NotFoundException extends Exception{

    public NotFoundException(){

    }

    public NotFoundException(Exception e){
        super(e);
    }

    public NotFoundException(String message, Exception e){
        super(message, e);
    }

    public NotFoundException(String message){
        super(message);
    }
}
