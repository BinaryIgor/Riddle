package com.iprogrammerr.riddle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class JsonApiTest {

    @Test
    public void jsonUser() throws Exception {
	String json = "{\"name\": \"igor\", \"email\": \"ceigor94@gmail.com\"}";
	StringBuilder builder = new StringBuilder();
	builder.append("[").append(json).append(", ").append(json).append(", ").append(json).append("]");
	JSONArray jsonArray = new JSONArray(builder.toString());
	for (int i = 0; i < jsonArray.length(); i++) {
	    JSONObject jsonObject = jsonArray.getJSONObject(i);
	    String name = jsonObject.optString("name");
	    assertTrue(name.equals("igor"));
	    String email = jsonObject.optString("email");
	    assertTrue(email.equals("ceigor94@gmail.com"));
	    String password = jsonObject.optString("password");
	    assertTrue(password.equals(""));
	    boolean active = jsonObject.optBoolean("active", false);
	    assertFalse(active);
	}
    }

    @Test
    public void map() {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("name", "igor");
	Map<String, Object> map = new HashMap<>();
	map.put("value", "dadadadsc");
	map.put("true", true);
	jsonObject.put("secret", map);
	System.out.println(jsonObject.toString());
    }
}
