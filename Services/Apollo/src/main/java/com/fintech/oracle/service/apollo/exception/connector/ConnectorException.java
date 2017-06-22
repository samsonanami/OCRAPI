package com.fintech.oracle.service.apollo.exception.connector;

/**
 * Created by sasitha on 6/21/17.
 *
 */
public class ConnectorException extends Exception {
    public ConnectorException(String message){
        super(message);
    }

    public ConnectorException(String message, Exception ex){
        super(message, ex);
    }

    public ConnectorException(Exception ex){
        super(ex);
    }
}
