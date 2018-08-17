package com.iprogrammerr.riddle.exception.database;

public class NoResultException extends RuntimeException {

    public NoResultException(Exception exception) {
	super(exception);
    }
}
