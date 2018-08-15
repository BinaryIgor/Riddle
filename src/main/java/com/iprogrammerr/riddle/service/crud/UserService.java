package com.iprogrammerr.riddle.service.crud;

import com.iprogrammerr.riddle.dao.UserDao;
import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.entity.UserRole;

public class UserService extends CrudService<User> {

    private UserDao userDao;

    public UserService(UserDao userDao) {
	super(userDao);
	this.userDao = userDao;
    }

    public User getUserByName(String name) {
	return userDao.getUserByName(name);
    }

    public User getUserByEmail(String email) {
	return userDao.getUserByEmail(email);
    }

    public User getUserByNameOrEmail(String nameOrEmail) {
	if (nameOrEmail.contains("@")) {
	    return userDao.getUserByEmail(nameOrEmail);
	} else {
	    return userDao.getUserByName(nameOrEmail);
	}
    }

    public UserRole getUserRoleByName(String name) {
	return userDao.getUserRoleByName(name);
    }

    public boolean existsByEmail(String email) {
	return userDao.existsByEmail(email);
    }

    public boolean existsByName(String name) {
	return userDao.existsByName(name);
    }

}
