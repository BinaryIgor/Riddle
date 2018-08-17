package com.iprogrammerr.riddle.entity;

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

	private String translation;

	Role(String translation) {
	    this.translation = translation;
	}

	public String getTranslation() {
	    return translation;
	}

	public boolean equalsByTranslation(String role) {
	    return translation.equals(role);
	}

	public static boolean isAdmin(String role) {
	    return ADMIN.translation.equals(role);
	}
    }

    @Override
    public String toString() {
	return "UserRole [id=" + id + ", name=" + name + "]";
    }

}
