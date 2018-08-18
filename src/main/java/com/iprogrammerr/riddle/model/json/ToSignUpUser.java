package com.iprogrammerr.riddle.model.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ToSignUpUser {

    private String email;
    private String name;
    private String password;

    @JsonCreator
    public ToSignUpUser(@JsonProperty("email") String email, @JsonProperty("name") String name,
	    @JsonProperty("password") String password) {
	this.email = email;
	this.name = name;
	this.password = password;
    }

    public String getEmail() {
	return email;
    }

    public String getName() {
	return name;
    }

    public String getPassword() {
	return password;
    }

}
