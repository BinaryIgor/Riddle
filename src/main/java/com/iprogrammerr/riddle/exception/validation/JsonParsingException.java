package com.iprogrammerr.riddle.exception.validation;

public class JsonParsingException extends RuntimeException {

    public JsonParsingException(Exception exception) {
	super(exception);
    }
}
