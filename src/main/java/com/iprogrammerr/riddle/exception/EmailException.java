package com.iprogrammerr.riddle.exception;

public class EmailException extends RuntimeException {

    public EmailException(Exception exception) {
	super(exception);
    }
}
