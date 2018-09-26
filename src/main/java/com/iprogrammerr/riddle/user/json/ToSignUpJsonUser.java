package com.iprogrammerr.riddle.user.json;

import org.json.JSONObject;

import com.iprogrammerr.riddle.user.ToSignUpUser;

public final class ToSignUpJsonUser implements ToSignUpUser {

    private final JSONObject source;

    public ToSignUpJsonUser(JSONObject source) {
	this.source = source;
    }

    public ToSignUpJsonUser(String json) {
	this(new JSONObject(json));
    }

    @Override
    public String name() throws Exception {
	return source.getString("name");
    }

    @Override
    public String email() throws Exception {
	return source.getString("email");
    }

    @Override
    public String password() throws Exception {
	return source.getString("password");
    }

}
