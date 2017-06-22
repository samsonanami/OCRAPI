package com.fintech.oracle.service.apollo.exception.task;

/**
 * Created by sasitha on 6/21/17.
 *
 */
public class TaskParseException extends Exception{

    public TaskParseException(String message){
        super(message);
    }

    public TaskParseException(String message, Exception e){
        super(message, e);
    }

    public TaskParseException(Exception e){
        super(e);
    }
}
