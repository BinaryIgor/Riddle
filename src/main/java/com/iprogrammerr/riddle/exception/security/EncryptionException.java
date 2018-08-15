package com.iprogrammerr.riddle.exception.security;

public class EncryptionException extends RuntimeException {

    public EncryptionException(Exception exception) {
	super(exception);
    }
}
