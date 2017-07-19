package com.fintech.oracle.service.common.exception;

/**
 * Created by sasitha on 12/5/16.
 *
 */
public class ConfigurationDataNotFoundException extends NotFoundException{

    public ConfigurationDataNotFoundException(String message){
        super(message);
    }

    public ConfigurationDataNotFoundException(String message, Exception e){
        super(message, e);
    }

    public ConfigurationDataNotFoundException(Exception e){
        super(e);
    }
}
