package com.iprogrammerr.riddle.controller;

import java.io.IOException;

import com.iprogrammerr.riddle.exception.http.WrongRequestBodyException;
import com.iprogrammerr.riddle.exception.validation.JsonParsingException;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.simple.http.server.model.Header;
import com.iprogrammerr.simple.http.server.model.Request;
import com.iprogrammerr.simple.http.server.model.Response;

public abstract class JsonController {

    protected JsonService jsonService;

    public JsonController(JsonService jsonService) {
	this.jsonService = jsonService;
    }

    protected <T> T getBody(Class<T> clazz, Request request) {
	try {
	    T object = jsonService.deserialize(clazz, request.getBody());
	    return object;
	} catch (IOException exception) {
	    exception.printStackTrace();
	    throw new WrongRequestBodyException();
	}
    }

    protected <T> void setBody(T object, Response response) {
	try {
	    String body = jsonService.serialize(object);
	    response.setBody(body.getBytes());
	    response.setContentTypeHeader(Header.createJsonContentType());
	} catch (IOException exception) {
	    exception.printStackTrace();
	    throw new JsonParsingException(exception);
	}
    }
}
