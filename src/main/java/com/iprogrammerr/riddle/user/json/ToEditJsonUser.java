package com.iprogrammerr.riddle.user.json;

import org.json.JSONObject;

import com.iprogrammerr.riddle.user.ToEditUser;

public final class ToEditJsonUser implements ToEditUser {

    private final JSONObject source;

    public ToEditJsonUser(JSONObject source) {
	this.source = source;
    }

    public ToEditJsonUser(String json) {
	this(new JSONObject(json));
    }

    @Override
    public String email() throws Exception {
	return source.getString("email");
    }

    @Override
    public boolean hasEmail() {
	return !source.optString("email").isEmpty();
    }

    @Override
    public String name() throws Exception {
	return source.getString("name");
    }

    @Override
    public boolean hasName() {
	return !source.optString("name").isEmpty();
    }

    @Override
    public String password() throws Exception {
	return source.getString("password");
    }

    @Override
    public boolean hasPassword() {
	return !source.optString("password").isEmpty();
    }

}
