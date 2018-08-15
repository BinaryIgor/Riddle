package com.iprogrammerr.riddle.exception.creation;

public class CreationException extends RuntimeException {

    public CreationException(Exception exception) {
	super(exception);
    }

    public CreationException(String message) {
	super(message);
    }

}
