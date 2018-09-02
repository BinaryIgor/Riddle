package com.iprogrammerr.riddle.exception.http;

import com.iprogrammerr.bright.server.constants.ResponseCode;
import com.iprogrammerr.bright.server.exception.HttpException;

public class WrongRequestBodyException extends HttpException {

    public WrongRequestBodyException() {
	super(ResponseCode.UNPROCESSABLE_ENTITY);
    }

}
