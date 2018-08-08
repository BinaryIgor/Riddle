package com.iprogrammerr.riddle.model;

public class User {

    public String username;
    public String email;
    public String password;

    @Override
    public String toString() {
	return "User [username=" + username + ", email=" + email + ", password=" + password + "]";
    }

}
