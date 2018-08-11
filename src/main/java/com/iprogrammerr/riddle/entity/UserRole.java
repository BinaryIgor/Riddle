package com.iprogrammerr.riddle.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 3, max = 25)
    private String name;

    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public void setId(long id) {
	this.id = id;
    }

    public void setName(String role) {
	this.name = role;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	UserRole other = (UserRole) obj;
	if (id != other.id)
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
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

}
