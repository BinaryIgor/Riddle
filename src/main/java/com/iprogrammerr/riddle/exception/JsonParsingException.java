package com.iprogrammerr.riddle.exception;

public class JsonParsingException extends RuntimeException {

    public JsonParsingException(Exception exception) {
	super(exception);
    }
}
