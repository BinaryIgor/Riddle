package com.iprogrammerr.riddle.model.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ToSignInUser {

    private String nameEmail;
    private String password;

    @JsonCreator
    public ToSignInUser(@JsonProperty("email") String nameEmail, @JsonProperty("password") String password) {
	this.nameEmail = nameEmail;
	this.password = password;
    }

    public String getNameEmail() {
	return nameEmail;
    }

    public String getPassword() {
	return password;
    }
}
