package com.iprogrammerr.riddle.exception.http;

import com.iprogrammerr.bright.server.constants.ResponseCode;
import com.iprogrammerr.bright.server.exception.HttpException;

public class DuplicateEntryHttpException extends HttpException {

    public DuplicateEntryHttpException(String message) {
	super(ResponseCode.CONFLICT, message);
    }
}
