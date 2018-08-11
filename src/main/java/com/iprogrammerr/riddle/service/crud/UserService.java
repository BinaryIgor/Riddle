package com.iprogrammerr.riddle.service.crud;

import com.iprogrammerr.riddle.dao.UserDao;
import com.iprogrammerr.riddle.entity.User;

public class UserService extends CrudService<User> {

    public UserService(UserDao userDao) {
	super(userDao);
    }

    public User getUserByName(String name) {
	return ((UserDao) dao).getUserByName(name);
    }

    public User getUserByEmail(String email) {
	return ((UserDao) dao).getUserByEmail(email);
    }

    public User getUserByNameOrEmail(String nameOrEmail) {
	UserDao userDao = (UserDao) dao;
	if (nameOrEmail.contains("@")) {
	    return userDao.getUserByEmail(nameOrEmail);
	} else {
	    return userDao.getUserByName(nameOrEmail);
	}
    }

}
