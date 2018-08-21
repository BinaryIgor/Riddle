package com.iprogrammerr.riddle.exception.request;

public class RequestParameterException extends RuntimeException {

    public RequestParameterException(String message) {
	super(message);
    }

    public static RequestParameterException createPositiveNumberRequiredException() {
	return new RequestParameterException("Positive number is required.");
    }

}
