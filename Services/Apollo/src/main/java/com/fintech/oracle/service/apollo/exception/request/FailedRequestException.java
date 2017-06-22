package com.fintech.oracle.service.apollo.exception.request;

/**
 * Created by sasitha on 6/20/17.
 *
 */
public class FailedRequestException extends Exception {
    public FailedRequestException(Exception e) {
        super(e);
    }

    public FailedRequestException(String message, Exception e ) {
        super(message, e);
    }
}
