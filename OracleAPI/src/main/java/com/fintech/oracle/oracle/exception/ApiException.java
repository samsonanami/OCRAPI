package com.fintech.oracle.oracle.exception;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-02T04:51:04.641Z")

public class ApiException extends Exception{
    private final int code;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }



}
