package com.iprogrammerr.riddle;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.iprogrammerr.riddle.exception.creation.JsonParsingException;
import com.iprogrammerr.riddle.model.security.Activator;
import com.iprogrammerr.riddle.service.json.JsonService;

public class JsonServiceTest {

    private JsonService jsonService;

    @Before
    public void setup() {
	this.jsonService = new JsonService();
    }

    @Test
    public void failOnNullDeserializationTest() {
	String json = "{\"id\": 1}";
	boolean exceptionThrown = false;
	try {
	    Activator activator = jsonService.deserialize(Activator.class, json);
	} catch (JsonParsingException exception) {
	    exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
    }
}
