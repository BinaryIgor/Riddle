package com.iprogrammerr.riddle.service.json;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonService {

    private final ObjectMapper objectMapper;

    public JsonService() {
	objectMapper = new ObjectMapper();
	objectMapper.enable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES);
    }

    public <T> T deserialize(Class<T> clazz, String json) throws IOException {
	T object = objectMapper.readValue(json, clazz);
	return object;
    }

    public <T> T deserialize(Class<T> clazz, InputStream inputStream) throws IOException {
	T object = objectMapper.readValue(inputStream, clazz);
	return object;
    }

    public <T> String serialize(T object) throws IOException {
	return objectMapper.writeValueAsString(object);
    }
}
