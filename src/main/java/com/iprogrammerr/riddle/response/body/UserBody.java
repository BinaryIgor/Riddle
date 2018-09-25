package com.iprogrammerr.riddle.response.body;

import org.json.JSONObject;

public class UserBody implements JsonBody {

    private final String name;
    private final String email;
    private final String password;

    public UserBody(String name, String email, String password) {
	this.name = name;
	this.email = email;
	this.password = password;
    }

    @Override
    public String content() {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("name", name);
	jsonObject.put("email", email);
	jsonObject.put("passwod", password);
	return jsonObject.toString();
    }

}
