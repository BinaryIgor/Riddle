package com.iprogrammerr.riddle.exception.creation;

public class JsonParsingException extends RuntimeException {

    public JsonParsingException(Exception exception) {
	super(exception);
    }
}
