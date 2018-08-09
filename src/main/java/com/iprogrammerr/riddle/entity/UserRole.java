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
    private String role;

    public long getId() {
	return id;
    }

    public String getRole() {
	return role;
    }

    public void setId(long id) {
	this.id = id;
    }

    public void setRole(String role) {
	this.role = role;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((role == null) ? 0 : role.hashCode());
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
	if (role == null) {
	    if (other.role != null)
		return false;
	} else if (!role.equals(other.role))
	    return false;
	return true;
    }

}
