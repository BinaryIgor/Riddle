package com.iprogrammerr.riddle.exception;

public class NotSupportedMethodException extends RuntimeException {

    public NotSupportedMethodException(String method) {
	super(method + " is not supported");
    }

}
