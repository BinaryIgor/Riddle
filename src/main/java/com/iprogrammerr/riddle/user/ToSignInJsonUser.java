package com.iprogrammerr.riddle.user;

import org.json.JSONObject;

public class ToSignInJsonUser implements ToSignInUser {

    private JSONObject jsonObject;

    public ToSignInJsonUser(JSONObject jsonObject) {
	this.jsonObject = jsonObject;
    }

    public ToSignInJsonUser(String json) {
	this(new JSONObject(json));
    }

    @Override
    public String nameOrEmail() throws Exception {
	return jsonObject.getString("nameOrEmail");
    }

    @Override
    public String password() throws Exception {
	return jsonObject.getString("password");
    }

}
