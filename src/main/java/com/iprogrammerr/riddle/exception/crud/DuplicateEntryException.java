package com.iprogrammerr.riddle.exception.crud;

public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException(String message) {
	super(message);
    }
}
