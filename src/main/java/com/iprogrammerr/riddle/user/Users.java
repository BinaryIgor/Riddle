package com.iprogrammerr.riddle.user;

public interface Users {

    Iterable<User> all();

    Iterable<User> active();

    User user(String nameOrEmail) throws Exception;

    boolean exists(String nameOrEmail);

    long createPlayer(String name, String email, String password) throws Exception;
}
