package com.iprogrammerr.riddle.response.body;

import org.json.JSONObject;

import com.iprogrammerr.riddle.security.token.Token;

public final class NewAccessTokenBody implements JsonBody {

    private final Token token;

    public NewAccessTokenBody(Token token) {
	this.token = token;
    }

    @Override
    public String content() {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("value", token.value());
	jsonObject.put("expirationDate", token.expirationDate());
	return jsonObject.toString();
    }

}
