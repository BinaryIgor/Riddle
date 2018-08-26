package com.iprogrammerr.riddle.exception.http;

import com.iprogrammerr.simple.http.server.constants.ResponseCode;
import com.iprogrammerr.simple.http.server.exception.HttpException;

public class WrongRequestBodyException extends HttpException {

    public WrongRequestBodyException() {
	super(ResponseCode.UNPROCESSABLE_ENTITY);
    }

}
