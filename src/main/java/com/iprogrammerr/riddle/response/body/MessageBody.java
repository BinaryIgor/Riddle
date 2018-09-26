package com.iprogrammerr.riddle.response.body;

import org.json.JSONObject;

public final class MessageBody implements JsonBody {

    private final String message;

    public MessageBody(String message) {
	this.message = message;
    }

    @Override
    public String content() {
	return new JSONObject().put("message", message).toString();
    }

}
