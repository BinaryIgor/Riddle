package com.iprogrammerr.riddle.service.json;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iprogrammerr.riddle.exception.creation.JsonParsingException;

public class JsonService {

    private final ObjectMapper objectMapper;

    public JsonService() {
	objectMapper = new ObjectMapper();
	objectMapper.enable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES);
    }

    public <T> T deserialize(Class<T> clazz, String json) {
	try {
	    return objectMapper.readValue(json, clazz);
	} catch (IOException exception) {
	    exception.printStackTrace();
	    throw new JsonParsingException(exception);
	}
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
	try {
	    return objectMapper.readValue(bytes, clazz);
	} catch (IOException exception) {
	    exception.printStackTrace();
	    throw new JsonParsingException(exception);
	}
    }

    public <T> String serialize(T object) {
	try {
	    return objectMapper.writeValueAsString(object);
	} catch (IOException exception) {
	    exception.printStackTrace();
	    throw new JsonParsingException(exception);
	}
    }

}
