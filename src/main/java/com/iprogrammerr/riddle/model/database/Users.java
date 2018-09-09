package com.iprogrammerr.riddle.model.database;

public interface Users {

    Iterable<User> all();

    Iterable<User> active();

    User create(String name, String email, String password) throws Exception;

    User create(String json) throws Exception;
}
