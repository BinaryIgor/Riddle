package com.iprogrammerr.riddle.service.json;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T deserialize(Class<T> clazz, InputStream inputStream) throws IOException {
	T object = objectMapper.readValue(inputStream, clazz);
	return object;
    }

    public <T> String serialize(T object) throws IOException {
	return objectMapper.writeValueAsString(object);
    }
}
