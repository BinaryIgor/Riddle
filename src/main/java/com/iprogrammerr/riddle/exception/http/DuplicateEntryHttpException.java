package com.iprogrammerr.riddle.exception.http;

import com.iprogrammerr.simple.http.server.constants.ResponseCode;
import com.iprogrammerr.simple.http.server.exception.HttpException;

public class DuplicateEntryHttpException extends HttpException {

    public DuplicateEntryHttpException(String message) {
	super(ResponseCode.CONFLICT, message);
    }
}
