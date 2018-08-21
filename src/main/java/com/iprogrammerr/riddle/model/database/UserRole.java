package com.iprogrammerr.riddle.model.database;

public class UserRole {

    private long id;
    private String name;

    public UserRole(long id, String name) {
	this.id = id;
	this.name = name;
    }

    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public enum Role {

	PLAYER("player"), ADMIN("admin");

	public String value;

	Role(String value) {
	    this.value = value;
	}

	public boolean equals(String role) {
	    return value.equals(role);
	}

	public static boolean isAdmin(String role) {
	    return ADMIN.value.equals(role);
	}
    }

    @Override
    public String toString() {
	return "UserRole [id=" + id + ", name=" + name + "]";
    }

}
