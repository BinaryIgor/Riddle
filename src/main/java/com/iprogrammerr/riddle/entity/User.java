package com.iprogrammerr.riddle.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 5, max = 20)
    private String email;

    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @NotNull
    @Size(min = 3, max = 50)
    private String password;

    @Column(name = "image_path")
    private String imagePath;

    private boolean active;

    @OneToOne
    @JoinColumn(name = "user_role_id")
    private UserRole userRole;

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

    public void setId(long id) {
	this.id = id;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getImagePath() {
	return imagePath;
    }

    public void setImagePath(String imagePath) {
	this.imagePath = imagePath;
    }

    public boolean isActive() {
	return active;
    }

    public void setActive(boolean active) {
	this.active = active;
    }

    public UserRole getUserRole() {
	return userRole;
    }

    public void setUserRole(UserRole userRole) {
	this.userRole = userRole;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((email == null) ? 0 : email.hashCode());
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((imagePath == null) ? 0 : imagePath.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((password == null) ? 0 : password.hashCode());
	result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
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
	User other = (User) obj;
	if (email == null) {
	    if (other.email != null)
		return false;
	} else if (!email.equals(other.email))
	    return false;
	if (id != other.id)
	    return false;
	if (imagePath == null) {
	    if (other.imagePath != null)
		return false;
	} else if (!imagePath.equals(other.imagePath))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (password == null) {
	    if (other.password != null)
		return false;
	} else if (!password.equals(other.password))
	    return false;
	if (userRole == null) {
	    if (other.userRole != null)
		return false;
	} else if (!userRole.equals(other.userRole))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "User [id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", imagePath="
		+ imagePath + ", userRole=" + userRole + "]";
    }

}
