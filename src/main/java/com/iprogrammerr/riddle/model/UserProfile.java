package com.iprogrammerr.riddle.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {

    private String email;
    private String name;
    private int points;

    @JsonCreator
    public UserProfile(@JsonProperty("email") String email, @JsonProperty("name") String name,
	    @JsonProperty("name") int points) {
	this.email = email;
	this.name = name;
	this.points = points;
    }

    public String getEmail() {
	return email;
    }

    public String getName() {
	return name;
    }

    public int getPoints() {
	return points;
    }
}
