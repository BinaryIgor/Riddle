package com.iprogrammerr.riddle.exception.database;

public class DeleteException extends RuntimeException {

    public DeleteException(Exception exception) {
	super(exception);
    }

}
