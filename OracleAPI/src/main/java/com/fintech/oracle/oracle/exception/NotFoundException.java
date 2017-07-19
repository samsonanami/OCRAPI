package com.fintech.oracle.oracle.exception;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-12-02T04:51:04.641Z")

public class NotFoundException extends ApiException {
	private int code;
	public NotFoundException(int code, String msg) {
		super(code, msg);
		this.code = code;
	}
}
