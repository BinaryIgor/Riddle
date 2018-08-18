package com.iprogrammerr.riddle.entity;

public class User {

    private long id;
    private String email;
    private String name;
    private String password;
    private String imagePath;
    private boolean active;
    public UserRole userRole;

    public User(long id, String email, String name, String password, String imagePath, boolean active,
	    UserRole userRole) {
	this.id = id;
	this.email = email;
	this.name = name;
	this.password = password;
	this.imagePath = imagePath;
	this.active = active;
	this.userRole = userRole;
    }

    public User(String email, String name, String password) {
	this.email = email;
	this.name = name;
	this.password = password;
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

    public String getImagePath() {
	return imagePath;
    }

    public boolean isActive() {
	return active;
    }

    public UserRole getUserRole() {
	return userRole;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public void setUserRole(UserRole userRole) {
	this.userRole = userRole;
    }

    @Override
    public String toString() {
	return "User [id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", imagePath="
		+ imagePath + ", userRole=" + userRole + "]";
    }

}
