package com.iprogrammerr.riddle.user;

import org.json.JSONObject;

public class ToSignUpJsonUser implements ToSignUpUser {

    private JSONObject jsonObject;

    public ToSignUpJsonUser(JSONObject jsonObject) {
	this.jsonObject = jsonObject;
    }

    public ToSignUpJsonUser(String json) {
	this(new JSONObject(json));
    }

    @Override
    public String name() throws Exception {
	return jsonObject.getString("name");
    }

    @Override
    public String email() throws Exception {
	return jsonObject.getString("email");
    }

    @Override
    public String password() throws Exception {
	return jsonObject.getString("password");
    }

}
