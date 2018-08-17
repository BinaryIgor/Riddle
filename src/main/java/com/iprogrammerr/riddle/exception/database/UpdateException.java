package com.iprogrammerr.riddle.exception.database;

public class UpdateException extends RuntimeException {

    public UpdateException(Exception exception) {
	super(exception);
    }

}
