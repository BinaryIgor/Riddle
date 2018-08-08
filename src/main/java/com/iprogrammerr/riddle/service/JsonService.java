package com.iprogrammerr.riddle.service;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T deserialize(Class<T> clazz, InputStream inputStream) throws IOException {
	T object = objectMapper.readValue(inputStream, clazz);
	return object;
    }
}
