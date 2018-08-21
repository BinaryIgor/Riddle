package com.iprogrammerr.riddle.model.database;

public class User {

    private long id;
    private String email;
    private String name;
    private String password;
    private boolean active;
    private UserRole userRole;

    public User(String email, String name, String password) {
	this.email = email;
	this.name = name;
	this.password = password;
    }

    public User(long id, String email, String name, String password, boolean active, UserRole userRole) {
	this.id = id;
	this.email = email;
	this.name = name;
	this.password = password;
	this.active = active;
	this.userRole = userRole;
    }

    public long getId() {
	return id;
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

    public void setPassword(String password) {
	this.password = password;
    }

    public UserRole getUserRole() {
	return userRole;
    }

    public void setUserRole(UserRole userRole) {
	this.userRole = userRole;
    }

    public boolean isActive() {
	return active;
    }

    @Override
    public String toString() {
	return "User [id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + "]";
    }

}
