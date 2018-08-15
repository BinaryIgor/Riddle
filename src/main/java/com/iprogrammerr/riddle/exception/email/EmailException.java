package com.iprogrammerr.riddle.exception.email;

public class EmailException extends RuntimeException {

    public EmailException(Exception exception) {
	super(exception);
    }
}
