package com.iprogrammerr.riddle.response.body;

import org.json.JSONObject;

public class UserProfileBody implements JsonBody {

    private final String name;
    private final String email;
    private final int points;

    public UserProfileBody(String name, String email, int points) {
	this.name = name;
	this.email = email;
	this.points = points;
    }

    @Override
    public String content() {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("name", name);
	jsonObject.put("email", email);
	jsonObject.put("points", points);
	return jsonObject.toString();
    }

}
