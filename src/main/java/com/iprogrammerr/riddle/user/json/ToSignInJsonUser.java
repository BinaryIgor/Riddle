package com.iprogrammerr.riddle.user.json;

import org.json.JSONObject;

import com.iprogrammerr.riddle.user.ToSignInUser;

public final class ToSignInJsonUser implements ToSignInUser {

    private final JSONObject source;

    public ToSignInJsonUser(JSONObject source) {
	this.source = source;
    }

    public ToSignInJsonUser(String json) {
	this(new JSONObject(json));
    }

    @Override
    public String nameOrEmail() throws Exception {
	return this.source.getString("nameOrEmail");
    }

    @Override
    public String password() throws Exception {
	return this.source.getString("password");
    }

}
