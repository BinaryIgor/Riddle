package com.iprogrammerr.riddle.exception.security;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {

    }

    public UnauthorizedException(String message) {
	super(message);
    }
}
