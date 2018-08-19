package com.iprogrammerr.riddle.model.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {

    private String email;
    private String name;

    @JsonCreator
    public UserProfile(@JsonProperty("email") String email, @JsonProperty("name") String name) {
	this.email = email;
	this.name = name;
    }

    public String getEmail() {
	return email;
    }

    public String getName() {
	return name;
    }

}
