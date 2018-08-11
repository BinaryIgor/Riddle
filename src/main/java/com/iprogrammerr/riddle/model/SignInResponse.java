package com.iprogrammerr.riddle.model;

public class SignInResponse {

    private String role;
    private Token accessToken;
    private Token refreshToken;

    public SignInResponse(String role, Token accessToken, Token refreshToken) {
	this.role = role;
	this.accessToken = accessToken;
	this.refreshToken = refreshToken;
    }

    public String getRole() {
	return role;
    }

    public Token getAccessToken() {
	return accessToken;
    }

    public Token getRefreshToken() {
	return refreshToken;
    }
}
