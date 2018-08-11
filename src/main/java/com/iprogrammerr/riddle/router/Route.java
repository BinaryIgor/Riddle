package com.iprogrammerr.riddle.router;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iprogrammerr.riddle.exception.JsonParsingException;
import com.iprogrammerr.riddle.exception.WrongRequestBodyException;
import com.iprogrammerr.riddle.service.json.JsonService;

public abstract class Route {

    private String mainPath;
    private JsonService jsonService;

    public Route(String mainPath, JsonService jsonService) {
	this.mainPath = mainPath;
	this.jsonService = jsonService;
    }

    public abstract void resolveGetRequest(String path, HttpServletRequest request, HttpServletResponse response);

    public abstract void resolvePostRequest(String path, HttpServletRequest request, HttpServletResponse response);

    public abstract void resolvePutRequest(String path, HttpServletRequest request, HttpServletResponse response);

    public abstract void resolveDeleteRequest(String path, HttpServletRequest request, HttpServletResponse response);

    public String getMainPath() {
	return mainPath;
    }

    protected <T> T getBody(Class<T> clazz, HttpServletRequest request) {
	try {
	    T object = jsonService.deserialize(clazz, request.getInputStream());
	    return object;
	} catch (IOException exception) {
	    exception.printStackTrace();
	    throw new WrongRequestBodyException();
	}
    }

    protected <T> void setBody(T object, HttpServletResponse response) {
	try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
	    String body = jsonService.serialize(object);
	    writer.write(body);
	} catch (IOException exception) {
	    exception.printStackTrace();
	    throw new JsonParsingException(exception);
	}
    }

}
