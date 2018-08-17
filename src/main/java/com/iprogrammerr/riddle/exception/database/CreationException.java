package com.iprogrammerr.riddle.exception.database;

public class CreationException extends RuntimeException {

    public CreationException(Exception exception) {
	super(exception);
    }
}
