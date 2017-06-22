package com.fintech.oracle.service.apollo.exception.config;

/**
 * Created by sasitha on 6/21/17.
 *
 */
public class ConfigurationFileReaderException extends Exception{

    public ConfigurationFileReaderException(String message){
        super(message);
    }

    public ConfigurationFileReaderException(String message, Exception ex){
        super(message, ex);
    }

    public ConfigurationFileReaderException(Exception ex){
        super(ex);
    }
}
