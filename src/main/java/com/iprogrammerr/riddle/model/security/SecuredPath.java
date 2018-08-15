package com.iprogrammerr.riddle.model.security;

public class SecuredPath {

    private String role;
    private String path;

    public SecuredPath(String role, String path) {
	this.role = role;
	this.path = path;
    }

    public String getRole() {
	return role;
    }

    public String getPath() {
	return path;
    }
}
