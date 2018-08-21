package com.iprogrammerr.riddle.exception.security;

public class UnauthenticatedException extends RuntimeException {

    public UnauthenticatedException() {

    }

    private UnauthenticatedException(String message) {
	super(message);
    }

    public static UnauthenticatedException createInvalidPasswordException() {
	return new UnauthenticatedException("Invalid Password");
    }

    public static UnauthenticatedException createNotActivatedUserException() {
	return new UnauthenticatedException("User has not been activated");
    }
}
