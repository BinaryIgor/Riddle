package com.iprogrammerr.riddle.exception.security;

public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException() {

    }

    public UnauthenticatedException(String message) {
	super(message);
    }
}
