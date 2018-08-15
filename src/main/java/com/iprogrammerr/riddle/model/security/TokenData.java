package com.iprogrammerr.riddle.model.security;

public class TokenData {

    private String username;
    private String role;
    private String tokenType;

    public TokenData(String username, String role, String tokenType) {
	this.username = username;
	this.role = role;
	this.tokenType = tokenType;
    }

    public String getUsername() {
	return username;
    }

    public String getRole() {
	return role;
    }

    public String getTokenType() {
	return tokenType;
    }
}
