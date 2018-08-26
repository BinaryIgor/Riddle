package com.iprogrammerr.riddle.exception.security;

import com.iprogrammerr.simple.http.server.constants.ResponseCode;
import com.iprogrammerr.simple.http.server.exception.HttpException;

public class UnauthenticatedException extends HttpException {

    public UnauthenticatedException() {
	super(ResponseCode.UNAUTHORIZED);
    }

    private UnauthenticatedException(String message) {
	super(ResponseCode.UNAUTHORIZED, message);
    }

    public static UnauthenticatedException createInvalidPasswordException() {
	return new UnauthenticatedException("Invalid Password");
    }

    public static UnauthenticatedException createNotActivatedUserException() {
	return new UnauthenticatedException("User has not been activated");
    }
}
