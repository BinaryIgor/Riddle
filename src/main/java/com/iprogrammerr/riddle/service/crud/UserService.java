package com.iprogrammerr.riddle.service.crud;

import com.iprogrammerr.riddle.dao.Dao;
import com.iprogrammerr.riddle.entity.User;

public class UserService extends CrudService<User> {

    public UserService(Dao<User> dao) {
	super(dao);
    }

}
