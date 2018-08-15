package com.iprogrammerr.riddle.exception.router;

public class NotSupportedMethodException extends RuntimeException {

    public NotSupportedMethodException(String method) {
	super(method + " is not supported");
    }

}
